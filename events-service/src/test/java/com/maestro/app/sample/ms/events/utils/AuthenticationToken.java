package com.maestro.app.sample.ms.events.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthenticationToken {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private Integer expires_in;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" AccessToken: " + (this.access_token != null ? this.access_token : "?"));
        sb.append("\n TokenType: " + (this.token_type != null ? this.token_type : "?"));
        sb.append("\n RefreshToken " + (this.refresh_token != null ? this.refresh_token : "?"));
        return sb.toString();
    }
}
