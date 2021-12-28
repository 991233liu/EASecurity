package com.easecurity.admin.auth

import grails.compiler.GrailsCompileStatic
import groovy.transform.ToString

@GrailsCompileStatic
//@ToString(includes = 'key', includeNames = true, includePackage = false)
class GifCaptcha extends com.easecurity.core.basis.s.GifCaptcha{

    static constraints = {
        sessionId nullable: true
        key2 nullable: true
        value nullable: true
        validTime nullable: true
    }

    static mapping = {
        table 's_gif_captcha'
        version false
    }
}