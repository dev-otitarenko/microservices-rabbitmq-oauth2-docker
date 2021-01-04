package com.maestro.app.sample1.ms.auth.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table
@NoArgsConstructor
public class DeviceMetadata {
    @Id
    private String id;
    @Column(nullable = false)
    private String userId;
    private String ipAddress;
    private String deviceDetails;
    private String country;
    private String countryCode;
    private String city;
    private Date lastLoggedIn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceMetadata that = (DeviceMetadata) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(ipAddress, that.ipAddress) &&
                Objects.equals(deviceDetails, that.deviceDetails) &&
                Objects.equals(country, that.country) &&
                Objects.equals(countryCode, that.countryCode) &&
                Objects.equals(city, that.city) &&
                Objects.equals(lastLoggedIn, that.lastLoggedIn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, deviceDetails, country, countryCode, city, lastLoggedIn);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DeviceMetadata {");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", ip_address='").append(ipAddress).append('\'');
        sb.append(", deviceDetails='").append(deviceDetails).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append(", countryCode='").append(countryCode).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", lastLoggedIn=").append(lastLoggedIn);
        sb.append('}');
        return sb.toString();
    }
}
