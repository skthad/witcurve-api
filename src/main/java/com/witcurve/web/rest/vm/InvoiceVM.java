package com.witcurve.web.rest.vm;

import com.witcurve.domain.Student;

import java.util.Map;

public class InvoiceVM {

    private Student student;

    private Map<String, Double> feeDescriptions;

    private String invoiceNo;

    public Student getStudent() { return student; }

    public void setStudent(Student student) { this.student = student; }

    public Map<String, Double> getFeeDescriptions() {
        return feeDescriptions;
    }

    public void setFeeDescriptions(Map<String, Double> feeDescriptions) {
        this.feeDescriptions = feeDescriptions;
    }

    public String getInvoiceNo() { return invoiceNo; }

    public void setInvoiceNo(String invoiceNo) { this.invoiceNo = invoiceNo; }

    @Override
    public String toString() {
        return "InvoiceVM{" +
            "student=" + student +
            ", feeDescriptions=" + feeDescriptions +
            ", invoiceNo='" + invoiceNo + '\'' +
            '}';
    }
}

