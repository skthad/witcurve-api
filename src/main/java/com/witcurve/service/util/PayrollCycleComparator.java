package com.witcurve.service.util;

import com.witcurve.service.dto.PayrollCycleDTO;

import java.util.Comparator;

public class PayrollCycleComparator implements Comparator<PayrollCycleDTO> {
    @Override
    public int compare(PayrollCycleDTO o1, PayrollCycleDTO o2) {
        if (o1.getYear().intValue() == o2.getYear().intValue()) {
            if (o1.getMonth().getValue() == o2.getMonth().getValue()) {
                return 0;
            } else if (o1.getMonth().getValue() > o2.getMonth().getValue()) {
                return 1;
            } else {
                return -1;
            }
        } else if (o1.getYear().intValue() > o2.getYear().intValue()) {
            return 1;
        } else {
            return -1;
        }
    }
}
