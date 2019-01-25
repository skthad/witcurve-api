package com.witcurve.service.util;

import com.witcurve.service.dto.PayrollDTO;

import java.util.Comparator;

public class PayrollComparator implements Comparator<PayrollDTO> {
    @Override
    public int compare(PayrollDTO o1, PayrollDTO o2) {
        if (o1.getPayrollDetails().getPayrollCycle().getYear() == o2.getPayrollDetails().getPayrollCycle().getYear()) {
            if (o1.getPayrollDetails().getPayrollCycle().getMonth().getValue() == o2.getPayrollDetails().getPayrollCycle().getMonth().getValue()) {
                return 0;
            } else if (o1.getPayrollDetails().getPayrollCycle().getMonth().getValue() > o2.getPayrollDetails().getPayrollCycle().getMonth().getValue()) {
                return 1;
            } else {
                return -1;
            }
        } else if (o1.getPayrollDetails().getPayrollCycle().getYear() > o2.getPayrollDetails().getPayrollCycle().getYear()) {
            return 1;
        } else {
            return -1;
        }
    }
}
