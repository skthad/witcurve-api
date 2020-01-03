package com.witcurve.service;

import com.witcurve.web.rest.vm.PaytmStatusCheckVM;
import com.witcurve.web.rest.vm.PaytmVM;

import java.util.Map;

public interface PaytmCallBackService {

    PaytmVM getStudentFee(String instituteName, String admissionId, String type);

    Map<String, String> save(PaytmStatusCheckVM paytmStatusCheckVM, String orderId, String admissionId, String instituteName);
}
