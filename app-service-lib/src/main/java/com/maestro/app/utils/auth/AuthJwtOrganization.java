package com.maestro.app.utils.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthJwtOrganization {
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
