package com.maestro.app.sample.ms.auth.security;

import com.maestro.app.sample.ms.auth.entities.User;
import com.maestro.app.sample.ms.auth.services.UserInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserInformationService userInfoService;

    @Override
    public void onApplicationEvent(final AuthenticationSuccessEvent e) {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        final Authentication auth =  e.getAuthentication();

        if (auth.getPrincipal() instanceof User && auth.getDetails() instanceof WebAuthenticationDetails) {
            log.debug("onApplicationEvent {} {}", auth.getPrincipal(), xfHeader);
            userInfoService.verifyUserInformation(((User)auth.getPrincipal()), request);
        }
    }
}
