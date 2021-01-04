package com.maestro.app.utils.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueueLogConnectEvt {
    private String iduser;
    private String username;
    private String ip_address;
    private String countryCode;
    private String countryName;
    private String city;
    private String deviceDetails;
}
