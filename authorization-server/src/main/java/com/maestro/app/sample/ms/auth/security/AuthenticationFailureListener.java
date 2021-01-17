package com.maestro.app.sample.ms.auth.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    @Autowired
    private HttpServletRequest request;

    @Override
    public void onApplicationEvent(final AuthenticationFailureBadCredentialsEvent e) {
       final String xfHeader = request.getHeader("X-Forwarded-For");
       final Authentication auth =  e.getAuthentication();
       if (auth != null) {
             log.info(String.format("** [%s] onApplicationEvent::AuthenticationFailureBadCredentialsEvent - (%s)", auth.getName(), xfHeader == null ? request.getRemoteAddr() : xfHeader.split(",")[0] ));
       }
    }
}
