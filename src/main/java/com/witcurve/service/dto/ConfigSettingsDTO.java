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

    @NotNull
    private Long schoolId;

    @NotNull
    private ConfigType configType;

    @NotNull
    private ConfigFieldType fieldType;

    @NotNull
    private ConfigFieldName fieldName;

    @NotNull
    private String displayFieldName;

    @NotNull
    private String fieldValue;

    private String fieldDescription;

    @NotNull
    private Integer displayOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
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

    public String getDisplayFieldName() {
        return displayFieldName;
    }

    public void setDisplayFieldName(String displayFieldName) {
        this.displayFieldName = displayFieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
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
