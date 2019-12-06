package com.witcurve.web.rest.vm;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class PaytmVM implements Serializable {
    @NotNull
    private Integer errorCode;
    @NotNull
    private StudentDetail studentDetails;
    @NotNull
    private List<FeeTypeDetail> feeTypeDetails;

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

    public class StudentDetail implements Serializable {
        @NotNull
        private String studentName;
        @NotNull
        private String rollNo;
        @NotNull
        private String admissionId;
        @NotNull
        private String note;
        @NotNull
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

        public void setAdmissionId(String admissionId) {
            this.admissionId = admissionId;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getStandard() {
            return standard;
        }

        public void setStandard(String standard) {
            this.standard = standard;
        }

        @Override
        public String toString() {
            return "StudentDetail{" +
                "studentName='" + studentName + '\'' +
                ", rollNo='" + rollNo + '\'' +
                ", admissionId='" + admissionId + '\'' +
                ", note='" + note + '\'' +
                ", standard='" + standard + '\'' +
                '}';
        }
    }

    public class FeeTypeDetail implements Serializable {
        @NotNull
        private String name;
        @NotNull
        private Double amount;
        @NotNull
        private Boolean editable;
        @NotNull
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
}
