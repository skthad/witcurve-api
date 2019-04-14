package com.witcurve.service.dto.csv;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class ImportResponse implements Serializable {

    private int successCount;

    private int failedCount;

    private Map<Integer, String> rowErrorMessages;

    private String errorFileStream;

    public ImportResponse(int successCount, int failedCount, Map<Integer, String> rowErrorMessages, String errorFileStream) {
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.rowErrorMessages = rowErrorMessages;
        this.errorFileStream = errorFileStream;
    }

    public ImportResponse() {

    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public Map<Integer, String> getRowErrorMessages() {
        return rowErrorMessages;
    }

    public void setRowErrorMessages(Map<Integer, String> rowErrorMessages) {
        this.rowErrorMessages = rowErrorMessages;
    }

    public String getErrorFileStream() {
        return errorFileStream;
    }

    public void setErrorFileStream(String errorFileStream) {
        this.errorFileStream = errorFileStream;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportResponse that = (ImportResponse) o;
        return successCount == that.successCount &&
            failedCount == that.failedCount &&
            Objects.equals(rowErrorMessages, that.rowErrorMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(successCount, failedCount, rowErrorMessages);
    }

    @Override
    public String toString() {
        return "ImportResponse{" +
            "successCount=" + successCount +
            ", failedCount=" + failedCount +
            ", rowErrorMessages=" + rowErrorMessages +
            '}';
    }
}
