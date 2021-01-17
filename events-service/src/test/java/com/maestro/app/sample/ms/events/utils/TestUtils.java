package com.maestro.app.sample.ms.events.utils;

import com.maestro.app.utils.auth.AuthJwtOrganization;
import com.maestro.app.utils.auth.AuthUser;
import com.maestro.app.utils.types.TypeAdmin;
import lombok.AllArgsConstructor;
import lombok.Data;

public class TestUtils {
    public static AuthUser queryAuthUser(String idUser, String idOrg, TypeAdmin typeAdmin) {
        AuthUser ret = new AuthUser();
        ret.setId(idUser);
        ret.setUsername("USER");
        ret.setTypeAdmin(typeAdmin.getValue());
        AuthJwtOrganization retOrg = new AuthJwtOrganization();
        retOrg.setId(idOrg);
        retOrg.setName("MY ORGANIZATION");
        ret.setOrganization(retOrg);

        return ret;
    }

    @Data
    @AllArgsConstructor
    public static class TUser {
        private String iduser;
        private String userName;
        private String idOrg;
    }

    @Data
    @AllArgsConstructor
    public static class TOrg {
        private String orgName;
        private int orgLevel;
    }

    @Data
    @AllArgsConstructor
    public static class TRole {
        private long idrole;
    }
}
