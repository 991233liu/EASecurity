package com.easecurity.admin.auth

import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
@ToString(includes = 'gkey', includeNames = true, includePackage = false)
class GifCaptcha extends com.easecurity.core.basis.s.GifCaptcha{

    static constraints = {
        gkey nullable: true
        gvalue nullable: true
        validTime nullable: true
        dateCreated nullable: true
    }

    static mapping = {
        table 's_gif_captcha'
        version false
    }
}