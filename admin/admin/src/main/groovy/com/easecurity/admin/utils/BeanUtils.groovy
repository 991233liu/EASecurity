package com.easecurity.admin.utils

import grails.core.GrailsApplication
import grails.util.Holders
import org.springframework.context.ApplicationContext

class BeanUtils {

    public static GrailsApplication grailsApplication = Holders.grailsApplication
    public static ApplicationContext context = grailsApplication.mainContext
//    public static def grailsApplication
//    public static GrailsWebApplicationContext context

    /**
     * 获得Spring Bean<p>
     *
     * @param name	Spring中注册的名称
     * @return bean
     */
    static Object getBean(String name) {
        return context.getBean(name)
//        try {
//			return GrailsUtils.context ? GrailsUtils.context.getBean(name) : null
//		} catch ( NoSuchBeanDefinitionException e ) {
//			return null
//		}
    }

    /**
     * 获得Spring Bean<p>
     *
     * @param name	Spring中注册的名称
     * @return bean
     */
    static Object getBean(Class name) {
        return context.getBean(name)
//        try {
//			return GrailsUtils.context ? GrailsUtils.context.getBean(name) : null
//		} catch ( NoSuchBeanDefinitionException e ) {
//			return null
//		}
    }
}
