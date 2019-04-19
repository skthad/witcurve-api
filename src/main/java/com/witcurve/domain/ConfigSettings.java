package com.witcurve.domain;

import com.witcurve.domain.enumeration.ConfigFieldName;
import com.witcurve.domain.enumeration.ConfigFieldType;
import com.witcurve.domain.enumeration.ConfigType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="config_settings")
public class ConfigSettings extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long instituteId;

    @Column
    private Long schoolId;

    @Column
    private Long schoolInfoId;

    @NotNull
    @Column(name = "config_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConfigType configType;

    @NotNull
    @Column(name = "field_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConfigFieldType fieldType;

    @NotNull
    @Column(name = "field_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private ConfigFieldName fieldName;

    @NotNull
    @Column(name = "display_field_name", nullable = false)
    private String displayFieldName;

    @NotNull
    @Column(name = "field_value", nullable = false)
    private String fieldValue;

    @NotNull
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

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

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConfigSettings)) return false;
        ConfigSettings institute = (ConfigSettings) o;
        return Objects.equals(getId(), institute.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "ConfigSettings{" +
            "id=" + id +
            '}';
    }
}
