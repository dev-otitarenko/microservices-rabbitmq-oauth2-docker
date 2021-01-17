package com.maestro.app.sample.ms.messages.utils;

import com.maestro.app.utils.auth.AuthJwtOrganization;
import com.maestro.app.utils.auth.AuthUser;
import com.maestro.app.utils.types.TypeAdmin;

public class TestUtils {
    public static AuthUser queryAuthUser(String idUser, String idOrg, TypeAdmin typeAdmin) {
        AuthUser ret = new AuthUser();
        ret.setId(idUser);
        ret.setUsername("USER #" + idUser);
        ret.setTypeAdmin(typeAdmin.getValue());
        AuthJwtOrganization retOrg = new AuthJwtOrganization();
        retOrg.setId(idOrg);
        retOrg.setName("MY ORGANIZATION");
        ret.setOrganization(retOrg);

        return ret;
    }
}
