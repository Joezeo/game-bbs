package com.joezeo.joefgame.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringGetter implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static <T> Optional<T> getBean(Class<T> type) {
        if (applicationContext == null) {
            return Optional.empty();
        }
        return Optional.of(applicationContext.getBean(type));
    }

    public static <T> Optional<T> getBean(String name, Class<T> type) {
        if (applicationContext == null) {
            return Optional.empty();
        }
        return Optional.of(applicationContext.getBean(name, type));
    }
}
