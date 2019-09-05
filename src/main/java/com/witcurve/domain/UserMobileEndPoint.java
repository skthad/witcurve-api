package com.witcurve.domain;

import com.witcurve.domain.enumeration.MobileOsType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "user_mobile_end_point", uniqueConstraints = {
    @UniqueConstraint(name = "user_endPoint_UK",
        columnNames = {"user_id", "endPoint"})
})
public class UserMobileEndPoint extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MobileOsType type;

    @NotNull
    @Column(nullable = false, length = 1000)
    private String deviceToken;

    @NotNull
    @Column(nullable = false)
    private String endPoint;

    @NotNull
    @Column(nullable = false)
    private String schoolInfoSubscriptionEndPoint;

    @NotNull
    @Column(nullable = false)
    private String globalSubscriptionEndPoint;

    @Column
    private String staffInfoSubscriptionEndPoint;

    @Column
    private String standardSubscriptionEndPoint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MobileOsType getType() {
        return type;
    }

    public void setType(MobileOsType type) {
        this.type = type;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getSchoolInfoSubscriptionEndPoint() {
        return schoolInfoSubscriptionEndPoint;
    }

    public void setSchoolInfoSubscriptionEndPoint(String schoolInfoSubscriptionEndPoint) {
        this.schoolInfoSubscriptionEndPoint = schoolInfoSubscriptionEndPoint;
    }

    public String getGlobalSubscriptionEndPoint() {
        return globalSubscriptionEndPoint;
    }

    public void setGlobalSubscriptionEndPoint(String globalSubscriptionEndPoint) {
        this.globalSubscriptionEndPoint = globalSubscriptionEndPoint;
    }

    public String getStaffInfoSubscriptionEndPoint() {
        return staffInfoSubscriptionEndPoint;
    }

    public void setStaffInfoSubscriptionEndPoint(String staffInfoSubscriptionEndPoint) {
        this.staffInfoSubscriptionEndPoint = staffInfoSubscriptionEndPoint;
    }

    public String getStandardSubscriptionEndPoint() {
        return standardSubscriptionEndPoint;
    }

    public void setStandardSubscriptionEndPoint(String standardSubscriptionEndPoint) {
        this.standardSubscriptionEndPoint = standardSubscriptionEndPoint;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMobileEndPoint that = (UserMobileEndPoint) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserMobileEndPoint{" +
            "id=" + id +
            '}';
    }
}
