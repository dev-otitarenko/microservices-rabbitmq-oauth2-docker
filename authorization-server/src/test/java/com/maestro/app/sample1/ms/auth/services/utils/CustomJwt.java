package com.maestro.app.sample1.ms.auth.services.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Maestro on Apr-2020.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomJwt {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private JwtUser user;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\n AccessToken: %s", this.access_token));
        sb.append(String.format("\n TokenType: %s", this.token_type));
        sb.append(String.format("\n RefreshToken: %s", this.refresh_token));
        sb.append(this.user);
        return sb.toString();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JwtUser {
        private String id;
        private String username;
        private int typeAdmin;
        private JwtOrganization organization;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n [");
            sb.append(String.format("\n\t Id: %s", this.id));
            sb.append(String.format("\n\t Username: %s", this.username));
            sb.append(String.format("\n\t typeAdmin: %s", this.typeAdmin));
            sb.append(String.format("\n\t Organization: %s", this.organization));
            sb.append("\n ]");
            return sb.toString();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JwtOrganization {
        private String id;
        private String name;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("\n\t [");
            sb.append(String.format("\n\t\t Id: %s", this.id));
            sb.append(String.format("\n\t\t Name: %s", this.name));
            sb.append("\n\t ]");
            return sb.toString();
        }
    }
}
