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
    //@SequenceGenerator(name = "configSettingsIdSeq", sequenceName="config_settings_id_seq", allocationSize = 0)
    private Long id;

    @NotNull
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
    private ConfigFieldName fieldName;

    @NotNull
    @Column(name = "field_value", nullable = false)
    private String fieldValue;

    @NotNull
    @Column(name = "order", nullable = false)
    private Integer order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
