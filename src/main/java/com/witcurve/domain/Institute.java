package com.witcurve.domain;

import com.witcurve.domain.enumeration.SubscriptionModel;
import com.witcurve.domain.enumeration.SubscriptionPackage;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name="institute", uniqueConstraints = {
    @UniqueConstraint(name = "institute_name_UK",
        columnNames = {"name"})
})
public class Institute extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(name="primary_phone", length = 50, nullable = false)
    private String primaryPhone;

    @Column(name="secondary_phone", length = 50)
    private String secondaryPhone;

    @NotNull
    @Column(name = "primary_email", nullable = false, length = 50)
    private String primaryEmail;

    @Column(name = "secondary_email", length = 50)
    private String secondaryEmail;

    @Column(name="fax", length = 50)
    private String fax;

    @NotNull
    @Column(name="sms_signature", length = 6, nullable = false)
    private String smsSignature;

    @NotNull
    @Column(name="sub_domain_name", length = 6, nullable = false, unique = true, columnDefinition = "varchar(6) default 'random'")
    private String subDomainName;

    @NotNull
    @Column(name="android_short_url", length = 50, nullable = false, unique = true)
    private String androidUrl;

    @NotNull
    @Column(name="ios_short_url", length = 50, nullable = false, unique = true)
    private String iosUrl;

    @NotNull
    @Column(name="mobile_app_name", length = 50, nullable = false, unique = true)
    private String mobileAppName;

    @NotNull
    @Column(name = "subscription_model", nullable=false)
    @Enumerated(EnumType.STRING)
    private SubscriptionModel subscriptionModel;

    @Column(name = "subscription_start_date")
    private LocalDate subscriptionStartDate;

    @Column(name = "subscription_end_date")
    private LocalDate subscriptionEndDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyEnumerated(value = EnumType.STRING)
    @MapKeyColumn(name = "subscription_package")
    @Column(name = "subscription_cost")
    @CollectionTable(name = "subscription_pricing", joinColumns=@JoinColumn(name="institute_id"))
    private Map<SubscriptionPackage, Double> pricing = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getSecondaryEmail() {
        return secondaryEmail;
    }

    public void setSecondaryEmail(String secondaryEmail) {
        this.secondaryEmail = secondaryEmail;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getSmsSignature() {
        return smsSignature;
    }

    public void setSmsSignature(String smsSignature) {
        this.smsSignature = smsSignature;
    }

    public SubscriptionModel getSubscriptionModel() {
        return subscriptionModel;
    }

    public void setSubscriptionModel(SubscriptionModel subscriptionModel) {
        this.subscriptionModel = subscriptionModel;
    }

    public LocalDate getSubscriptionStartDate() {
        return subscriptionStartDate;
    }

    public void setSubscriptionStartDate(LocalDate subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    public LocalDate getSubscriptionEndDate() {
        return subscriptionEndDate;
    }

    public void setSubscriptionEndDate(LocalDate subscriptionEndDate) {
        this.subscriptionEndDate = subscriptionEndDate;
    }

    public String getAndroidUrl() {
        return androidUrl;
    }

    public void setAndroidUrl(String androidUrl) {
        this.androidUrl = androidUrl;
    }

    public String getIosUrl() {
        return iosUrl;
    }

    public void setIosUrl(String iosUrl) {
        this.iosUrl = iosUrl;
    }

    public String getMobileAppName() {
        return mobileAppName;
    }

    public void setMobileAppName(String mobileAppName) {
        this.mobileAppName = mobileAppName;
    }

    public Map<SubscriptionPackage, Double> getPricing() {
        return pricing;
    }

    public void setPricing(Map<SubscriptionPackage, Double> pricing) {
        this.pricing = pricing;
    }

    public String getSubDomainName() {
        return subDomainName;
    }

    public void setSubDomainName(String subDomainName) {
        this.subDomainName = subDomainName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Institute)) return false;
        Institute institute = (Institute) o;
        return Objects.equals(getId(), institute.getId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Institute{" +
            "id=" + id +
            '}';
    }
}
