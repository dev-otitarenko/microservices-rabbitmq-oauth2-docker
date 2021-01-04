package com.maestro.app.utils.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthJwtUser {
    private String id;
    private String username;
    private AuthJwtOrganization organization;
    private int typeAdmin;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n [");
        sb.append(String.format("\n\t Id: %s", this.id));
        sb.append(String.format("\n\t Username: %s", this.username));
          sb.append(String.format("\n\t typeAdmin: %d", this.typeAdmin));
        sb.append(String.format("\n\t Organization: %s", this.organization));
        sb.append("\n ]");
        return sb.toString();
    }
}
