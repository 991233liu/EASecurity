import grails.util.BuildSettings
import grails.util.Environment
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.StandardCharsets

conversionRule 'clr', ColorConverter
conversionRule 'wex', WhitespaceThrowableProxyConverter

// See http://logback.qos.ch/manual/groovy.html for details on configuration
appender('STDOUT', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = StandardCharsets.UTF_8

        pattern = '%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(---){faint} %clr([%thread]){faint} %clr(%-40.40logger{39}){faint}%clr([%F:%L]){cyan} %clr(:){faint} %m%n%wex'
    }
}

def targetDir = BuildSettings.TARGET_DIR
if (Environment.isDevelopmentMode() && targetDir != null) {
    appender("FULL_STACKTRACE", FileAppender) {
        file = "${targetDir}/stacktrace.log"
        append = true
        encoder(PatternLayoutEncoder) {
            charset = StandardCharsets.UTF_8
            pattern = "%level %logger - %msg%n"
        }
    }
    logger("StackTrace", INFO, ['FULL_STACKTRACE'], false)
}

root(INFO, ['STDOUT'])
if (Environment.isDevelopmentMode()) {
    logger("com.framework.auth", debug, ['STDOUT'], false)
    logger("com.liulf", debug, ['STDOUT'], false)
}