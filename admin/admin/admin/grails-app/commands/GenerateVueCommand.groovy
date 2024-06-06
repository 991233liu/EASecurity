package scaffolding

import com.easecurity.admin.CommandUtils
import grails.build.logging.ConsoleLogger
import grails.build.logging.GrailsConsole
import grails.core.GrailsApplication
import grails.dev.commands.GrailsApplicationCommand
import grails.plugin.scaffolding.CommandLineHelper
import grails.plugin.scaffolding.SkipBootstrap
import org.grails.io.support.Resource

/**
 * Generates vue views for the specified domain class
 * Usage: <code>./gradlew runCommand "-Pargs=generate-vue [DOMAIN_CLASS_NAME]|*"</code>
 *
 */
class GenerateVueCommand implements GrailsApplicationCommand, CommandLineHelper, SkipBootstrap {

    GrailsApplication grailsApplication

    @Delegate
    ConsoleLogger consoleLogger = GrailsConsole.getInstance()

    @Override
    boolean handle() {
        if (!args) {
            error("No domain-class specified")
            return FAILURE
        }
        List<String> domainClassesNames
        if (args[0].indexOf('*') > -1) {
            def find = args[0].substring(0, args[0].indexOf('*'))
            domainClassesNames = resources("file:grails-app/domain/**/*.groovy")
                    .collect { className(it) }.findAll { it.indexOf(find) > -1 }
        } else {
            domainClassesNames = args
        }
        final List<String> viewNames = resolveViewNames()
        boolean overwrite = isFlagPresent('force')
        int failureCount = 0
        for (String domainClassName in domainClassesNames) {
            final Resource sourceClass = source(domainClassName)
            if (sourceClass) {
                def model = CommandUtils.getModel(sourceClass, model(sourceClass), grailsApplication)
                viewNames.each { String view ->
				    String des = view
				    if (view.indexOf('i18n.vue') > -1) {
					    des = "i18n-${model.propertyName}.js"
				    }
                    render(template: template("scaffolding/$view"),
                            destination: file("vue/$model.propertyName/$des"),
                            model: model,
                            overwrite: overwrite)
                }
                addStatus("Views generated for ${projectPath(sourceClass)}")
            } else {
                error("Domain-class not found for name $domainClassName")
                failureCount++
            }
        }
        return failureCount ? FAILURE : SUCCESS
    }

    private List<String> resolveViewNames() {
        List<String> viewNames = resources("file:src/main/templates/scaffolding/*.vue")
                .collect { it.filename }
        if (!viewNames) {
            viewNames = resources("classpath*:META-INF/templates/scaffolding/*.vue")
                    .collect { it.filename }
        }
        viewNames
    }
}
