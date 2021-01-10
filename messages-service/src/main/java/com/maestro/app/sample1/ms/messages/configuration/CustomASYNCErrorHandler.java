package com.maestro.app.sample1.ms.messages.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

@Slf4j
public class CustomASYNCErrorHandler implements AsyncUncaughtExceptionHandler {
    private ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void handleUncaughtException(final Throwable throwable, final Method method, final Object... obj) {
        log.error(" * [AsyncExceptionHandler] Method name {}, {}: {}", method.getName(), method.getDeclaringClass().toString(), throwable.getMessage());
        if (method.getName().equalsIgnoreCase("your")) {
            //
            // Processing a specific method with @async annotation
            for (final Object o : obj) {
//                if (o instanceof QueueTemplateEvent) {
//                    prm = (QueueTemplateEvent)o;
//                    break;
//                }
            }
        } else {
            for (final Object param : obj) {
//                log.error("Param - " + param);
            }
        }
    }
}
