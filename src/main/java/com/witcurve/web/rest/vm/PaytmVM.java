package com.witcurve.web.rest.vm;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class PaytmVM implements Serializable {
    
    private Integer errorCode;
    
    private StudentDetail studentDetails;
    
    private List<FeeTypeDetail> feeTypeDetails;

    private Double totalAmount;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public StudentDetail getStudentDetails() {
        return studentDetails;
    }

    public void setStudentDetails(StudentDetail studentDetails) {
        this.studentDetails = studentDetails;
    }

    public List<FeeTypeDetail> getFeeTypeDetails() {
        return feeTypeDetails;
    }

    public void setFeeTypeDetails(List<FeeTypeDetail> feeTypeDetails) {
        this.feeTypeDetails = feeTypeDetails;
    }

    public Double getTotalAmount() { return totalAmount; }

    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }

    public static class StudentDetail implements Serializable {
        
        private String studentName;

        private String rollNo;

        private String fatherName;

        private String motherName;

        private String dateOfBirth;

        private String admissionId;

        private String note;

        private String standard;

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public String getRollNo() {
            return rollNo;
        }

        public void setRollNo(String rollNo) {
            this.rollNo = rollNo;
        }

        public String getAdmissionId() {
            return admissionId;
        }

        public void setAdmissionId(String admissionId) { this.admissionId = admissionId; }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getStandard() { return standard; }

        public void setStandard(String standard) { this.standard = standard; }

        public String getFatherName() { return fatherName; }

        public void setFatherName(String fatherName) { this.fatherName = fatherName; }

        public String getMotherName() { return motherName; }

        public void setMotherName(String motherName) { this.motherName = motherName; }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        @Override
        public String toString() {
            return "StudentDetail{" +
                "studentName='" + studentName + '\'' +
                ", rollNo='" + rollNo + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", motherName='" + motherName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", admissionId='" + admissionId + '\'' +
                ", note='" + note + '\'' +
                ", standard='" + standard + '\'' +
                '}';
        }
    }

    public static class FeeTypeDetail implements Serializable {
        
        private String name;
        
        private Double amount;
        
        private Boolean editable;
        
        private Boolean required;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public Boolean getEditable() {
            return editable;
        }

        public void setEditable(Boolean editable) {
            this.editable = editable;
        }

        public Boolean getRequired() {
            return required;
        }

        public void setRequired(Boolean required) {
            this.required = required;
        }

        @Override
        public String toString() {
            return "FeeTypeDetail{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                ", editable=" + editable +
                ", required=" + required +
                '}';
        }
    }

    @Override
    public String toString() {
        return "PaytmVM{" +
            "errorCode=" + errorCode +
            ", studentDetails=" + studentDetails +
            ", feeTypeDetails=" + feeTypeDetails +
            ", totalAmount=" + totalAmount +
            '}';
    }
}
