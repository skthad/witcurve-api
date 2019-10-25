package com.witcurve.service;

import com.witcurve.domain.enumeration.FeeDetailsType;
import com.witcurve.service.dto.FeeDetailsDTO;
import com.witcurve.web.rest.errors.WitcurveException;

import java.util.List;

public interface FeeDetailsService {

    List<FeeDetailsDTO> saveOrUpdateFeeDetails(List<FeeDetailsDTO> feeDetailsDTOs, Long schoolInfoId) throws WitcurveException;

    List<FeeDetailsDTO> getFeeDetailsBySchoolInfoId(Long schoolInfoId, FeeDetailsType type) throws WitcurveException;

    FeeDetailsDTO getFeeDetailsById(Long feeDetailsId) throws WitcurveException;

    void deleteFeeDetails(Long feeDetailsId);

}
