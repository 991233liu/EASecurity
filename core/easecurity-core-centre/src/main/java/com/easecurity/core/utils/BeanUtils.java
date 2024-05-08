package com.easecurity.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanUtils implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    /**
     * 获得Spring Bean
     * <p>
     *
     * @param name Spring中注册的名称
     * @return bean
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 获得Spring Bean
     * <p>
     *
     * @param name Spring中注册的名称
     * @return bean
     */
    public static Object getBean(Class<?> name) {
        return applicationContext.getBean(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanUtils.applicationContext = applicationContext;
    }
}
