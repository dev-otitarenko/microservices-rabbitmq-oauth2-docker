package com.maestro.app.utils.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthJwt {
    private AuthJwtUser user;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.user);
        return sb.toString();
    }
}
