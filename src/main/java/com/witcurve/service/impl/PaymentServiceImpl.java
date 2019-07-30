package com.witcurve.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paytm.pg.merchant.CheckSumServiceHelper;
import com.witcurve.config.ApplicationProperties;
import com.witcurve.config.Constants;
import com.witcurve.domain.PaymentOrder;
import com.witcurve.domain.Student;
import com.witcurve.domain.enumeration.PaymentGateway;
import com.witcurve.domain.enumeration.SubscriptionPackage;
import com.witcurve.domain.enumeration.TransactionMode;
import com.witcurve.domain.enumeration.TransactionStatus;
import com.witcurve.repository.PaymentOrderRepository;
import com.witcurve.repository.StudentRepository;
import com.witcurve.service.MailService;
import com.witcurve.service.PaymentService;
import com.witcurve.service.SmsService;
import com.witcurve.service.dto.PaytmRequestDTO;
import com.witcurve.service.dto.PaytmResponseDTO;
import com.witcurve.service.dto.PaytmVerificationRequestDTO;
import com.witcurve.service.util.RestClientUtil;
import com.witcurve.service.util.WitcurveUtil;
import com.witcurve.web.rest.errors.WitcurveException;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@SuppressWarnings("unchecked")
public class PaymentServiceImpl implements PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private MailService mailService;

    @Autowired
    private SmsService smsService;

    /**
     * Creates paytm payment order
     *
     * @param studentId
     * @param mobileNumber
     * @param subscriptionPackage
     * @return param with checksum
     * @throws WitcurveException
     */
    @Override
    public PaytmRequestDTO createPaytmOrder(Long studentId, String mobileNumber, SubscriptionPackage subscriptionPackage)
        throws WitcurveException {

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new WitcurveException("Invalid Request, student does not exist by the given id"));

        Double subscriptionCost = student.getSchoolInfo().getSchool().getInstitute().getPricing().get(subscriptionPackage);

        if (subscriptionCost == null) {
            throw new WitcurveException("Invalid Request, given pricing does not exist for the institute");
        }

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setStudent(student);
        paymentOrder.setSubscriptionPackage(subscriptionPackage);
        paymentOrder.setTransactionAmount(subscriptionCost);
        paymentOrder.setTransactionStatus(TransactionStatus.PENDING);
        paymentOrder.setPaymentGateway(PaymentGateway.PAYTM);
        paymentOrderRepository.save(paymentOrder);

        try {
            PaytmRequestDTO paytmRequestDTO = new PaytmRequestDTO();
            paytmRequestDTO.setMerchantMid(applicationProperties.getPaytm().getMerchantId());
            paytmRequestDTO.setOrderId(paymentOrder.getOrderId());
            paytmRequestDTO.setChannelId("WEB");
            paytmRequestDTO.setCustomerId(String.valueOf(paymentOrder.getStudent().getId()));
            if (StringUtils.isNotBlank(mobileNumber)) {
                paytmRequestDTO.setMobileNo(mobileNumber);
            }
            paytmRequestDTO.setTransactionAmount(String.valueOf(paymentOrder.getTransactionAmount()));
            paytmRequestDTO.setWebsite(applicationProperties.getPaytm().getWebsite());
            paytmRequestDTO.setIndustryTypeId(applicationProperties.getPaytm().getIndustryTypeId());
            paytmRequestDTO.setCallbackUrl(applicationProperties.getPaytm().getCallbackUrl() + paytmRequestDTO.getOrderId());

            Set<ConstraintViolation<PaytmRequestDTO>> violations = (Validation.buildDefaultValidatorFactory())
                .getValidator().validate(paytmRequestDTO);
            if (!CollectionUtils.isEmpty(violations)) {
                violations.forEach(violation -> log.error(violation.getMessage()));
                throw new WitcurveException("Validation for Paytm Request Order DTO Failed");
            }

            TreeMap paytmParams = (new ObjectMapper()).convertValue(paytmRequestDTO, TreeMap.class);
            String checkSumHash = CheckSumServiceHelper.getCheckSumServiceHelper()
                .genrateCheckSum(applicationProperties.getPaytm().getSecretKey(), paytmParams);

            paytmRequestDTO.setCheckSumHash(checkSumHash);
//            paytmRequestDTO.setEnvironment("staging");
            return paytmRequestDTO;
        } catch (Exception e) {
            log.error("Payment request for paytm failed", e);
            throw new WitcurveException("Payment request for paytm failed");
        }
    }

    /**
     * Creates verifies payment and updates related field if completed
     * only called for the first time
     *
     * @param orderId            the id of the order entity
     * @param updateSubscription as true is the subscription has to be updated
     * @return true if complete and false if else
     * @throws WitcurveException
     */
    @Override
    public Boolean isTransactionComplete(String orderId, Boolean updateSubscription) throws WitcurveException {
        try {
            PaymentOrder paymentOrder = paymentOrderRepository.findByOrderId(orderId);

            if (paymentOrder == null) {
                throw new WitcurveException("No order with given id");
            }

            PaytmVerificationRequestDTO request = new PaytmVerificationRequestDTO();
            request.setMerchantId(applicationProperties.getPaytm().getMerchantId());
            request.setOrderId(paymentOrder.getOrderId());

            TreeMap paytmParams = (new ObjectMapper()).convertValue(request, TreeMap.class);
            String checkSumHash = CheckSumServiceHelper.getCheckSumServiceHelper()
                .genrateCheckSum(applicationProperties.getPaytm().getSecretKey(), paytmParams);
            request.setCheckumHash(checkSumHash);

            Response response = RestClientUtil.post(applicationProperties.getPaytm().getTransactionStatusApi(), request);
            PaytmResponseDTO paytmResponseDTO = (new ObjectMapper()).readValue(response.body().string(), PaytmResponseDTO.class);

            if (StringUtils.isNotBlank(paytmResponseDTO.getPaymentMode())) {
                paymentOrder.setTransactionMode(TransactionMode.valueOf(paytmResponseDTO.getPaymentMode()));
                paymentOrder.setTransactionCharge(getPaytmTransactionCharge(paymentOrder.getTransactionAmount(), paymentOrder.getTransactionMode()));
            }

            paymentOrder.setTransactionStatus(TransactionStatus.valueOf(paytmResponseDTO.getStatus()));
            paymentOrder.setTransactionId(paytmResponseDTO.getTransactionId());

            boolean isSuccess = paymentOrder.getTransactionStatus() == TransactionStatus.TXN_SUCCESS;

            if (isSuccess && Boolean.TRUE.equals(updateSubscription)) {
                subscribeStudent(paymentOrder.getStudent().getId(), paymentOrder.getSubscriptionPackage());
            }

            return isSuccess;
        } catch (Exception e) {
            log.error("Unable to verify paytm request", e);
            throw new WitcurveException("Unable to process the request at the moment");
        }
    }

    private void subscribeStudent(Long studentId, SubscriptionPackage subscriptionPackage) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new WitcurveException("No student with given id"));

        if (student.getSubscriptionEndDate() == null) {
            student.setSubscriptionStartDate(LocalDate.now());
            student.setSubscriptionEndDate(LocalDate.now().plusMonths(subscriptionPackage.getMonths()));
        } else {
            student.setSubscriptionEndDate(student.getSubscriptionEndDate().plusMonths(subscriptionPackage.getMonths()));
        }

        Map paramsMap = new HashMap();
        paramsMap.put(Constants.PARAM_FULL_NAME, student.getFirstName() + " " + student.getLastName() + " parent");
        paramsMap.put(Constants.PARAM_SUBSCRIPTION_END_DATE, WitcurveUtil.format(student.getSubscriptionEndDate()));
        paramsMap.put(Constants.PARAM_INSTITUTE_NAME, student.getSchoolInfo().getSchool().getInstitute().getName());

        try {
            smsService.sendSms(student.getRegisteredMobileNumber(),
                "Dear " + paramsMap.get(Constants.PARAM_FULL_NAME) +
                    ", Your have been subscribed to Witcurve, it will expire on " + paramsMap.get(Constants.PARAM_SUBSCRIPTION_END_DATE) +
                    ".", student.getSchoolInfo().getSchool().getInstitute().getSmsSignature());
        } catch (UnsupportedEncodingException e) {
            log.error("Unable to send Subscription message", e);
        }

        if (student.getUser().getEmail() != null) {
            mailService.sendEmailFromTemplate(student.getUser().getEmail(), paramsMap, "mail/subscriptionTransactionEmail", "email.subscription.transaction.title", student.getSchoolInfo().getSchool().getInstitute().getSmsSignature());
        }
    }

    private Double getPaytmTransactionCharge(Double transactionAmount, TransactionMode transactionMode) {
        double paytmCharge;
        switch (transactionMode) {
            case PPI:
                paytmCharge = transactionAmount * .015;
                break;
            case CC:
                paytmCharge = transactionAmount * .011;
                break;
            case NB:
                paytmCharge = 16.0;
                break;
            case PAYTM_DIGITAL_CREDIT:
                paytmCharge = transactionAmount * .0185;
                break;
            default:
                paytmCharge = 0.0;
        }

        return paytmCharge + (paytmCharge * .18);
    }

    /**
     * scheduled process runs at midnight IST
     * updates the transactions and subscription dates for all pending payment transactions
     */
    @Override
    @Scheduled(cron = "0 0 0 * * *", zone = "IST")
    public void processPendingTransactions() {
        List<PaymentOrder> paymentOrders = paymentOrderRepository.findByTransactionStatus(TransactionStatus.PENDING);

        if (!CollectionUtils.isEmpty(paymentOrders)) {
            for (PaymentOrder paymentOrder: paymentOrders) {
                try {
                    boolean isSuccess = isTransactionComplete(paymentOrder.getOrderId(), true);
                    log.info("Transaction with order id {} " + (isSuccess ? "Succeeded" : "Failed"), paymentOrder.getOrderId());
                } catch (WitcurveException e) {
                    log.error("Unable to update payment status for order id : {}", paymentOrder.getId(), e);
                }
            }
        }
    }
}
