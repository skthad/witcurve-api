package com.witcurve.service.util;

import com.witcurve.service.dto.PayrollDTO;

import java.util.Comparator;

public class PayrollComparator implements Comparator<PayrollDTO> {
    @Override
    public int compare(PayrollDTO o1, PayrollDTO o2) {
        if (o1.getPayrollCycle().getYear() == o2.getPayrollCycle().getYear()) {
            if (o1.getPayrollCycle().getMonth().getValue() == o2.getPayrollCycle().getMonth().getValue()) {
                return 0;
            } else if (o1.getPayrollCycle().getMonth().getValue() > o2.getPayrollCycle().getMonth().getValue()) {
                return 1;
            } else {
                return -1;
            }
        } else if (o1.getPayrollCycle().getYear() > o2.getPayrollCycle().getYear()) {
            return 1;
        } else {
            return -1;
        }
    }
}
