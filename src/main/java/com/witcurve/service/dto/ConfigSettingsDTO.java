package com.witcurve.service.dto;

import com.witcurve.domain.enumeration.ConfigFieldName;
import com.witcurve.domain.enumeration.ConfigFieldType;
import com.witcurve.domain.enumeration.ConfigType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class ConfigSettingsDTO extends AbstractAuditingDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long instituteId;

    private Long schoolId;

    private Long schoolInfoId;

    @NotNull
    private ConfigType configType;

    @NotNull
    private ConfigFieldType fieldType;

    @NotNull
    private ConfigFieldName fieldName;

    @NotNull
    private String fieldValue;

    @NotNull
    private Integer order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(Long instituteId) {
        this.instituteId = instituteId;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Long getSchoolInfoId() {
        return schoolInfoId;
    }

    public void setSchoolInfoId(Long schoolInfoId) {
        this.schoolInfoId = schoolInfoId;
    }

    public ConfigType getConfigType() {
        return configType;
    }

    public void setConfigType(ConfigType configType) {
        this.configType = configType;
    }

    public ConfigFieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(ConfigFieldType fieldType) {
        this.fieldType = fieldType;
    }

    public ConfigFieldName getFieldName() {
        return fieldName;
    }

    public void setFieldName(ConfigFieldName fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigSettingsDTO)) return false;
        ConfigSettingsDTO that = (ConfigSettingsDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "ConfigSettingsDTO{" +
            "id=" + id +
            '}';
    }
}
