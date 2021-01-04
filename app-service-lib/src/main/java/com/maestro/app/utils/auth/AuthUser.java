package com.maestro.app.utils.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthUser {
    protected String id;
    protected String username;
    protected AuthJwtOrganization organization;
    protected Integer typeAdmin;

    public AuthUser(AuthJwt token) {
        this.id = token.getUser().getId();
        this.username = token.getUser().getUsername();
        this.typeAdmin = token.getUser().getTypeAdmin();
        this.organization = token.getUser().getOrganization() != null ? token.getUser().getOrganization() : new AuthJwtOrganization();
    }

    @Override
    public String toString() {
        return "\n User [" +
                "\n\tid=" + id +
                "\n\t username=" + username +
                 "\n\t typeAdmin='" + typeAdmin + '\'' +
                "\n\t, organizations=" + (organization == null ? "" : organization.toString()) +
                "\n\t]";
    }
}
