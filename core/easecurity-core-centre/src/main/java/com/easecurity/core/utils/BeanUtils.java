package com.easecurity.core.utils;


import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

@Component
public class BeanUtils {

    public static ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();

    /**
     * 获得Spring Bean<p>
     *
     * @param name	Spring中注册的名称
     * @return bean
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 获得Spring Bean<p>
     *
     * @param name	Spring中注册的名称
     * @return bean
     */
    public static Object getBean(Class<?> name) {
        return applicationContext.getBean(name);
    }
}
