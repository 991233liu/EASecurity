/*
 * Copyright 2023 Puneet Behl.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package scaffolding

import com.easecurity.admin.CommandUtils
import grails.build.logging.ConsoleLogger
import grails.build.logging.GrailsConsole
import grails.codegen.model.Model
import grails.core.GrailsApplication
import grails.dev.commands.GrailsApplicationCommand
import grails.plugin.scaffolding.CommandLineHelper
import grails.plugin.scaffolding.SkipBootstrap
import org.grails.io.support.Resource

/**
 * Generates an asynchronous controller that performs CRUD operations.
 * Usage: <code>./gradlew runCommand "-Pargs=generate-async-controller [DOMAIN_CLASS_NAME]|*"</code>
 *
 * @author Puneet Behl
 * @since 5.0.0
 */
class GenerateAsyncControllerCommand implements GrailsApplicationCommand, CommandLineHelper, SkipBootstrap {

    String description = 'Generates an asynchronous controller that performs CRUD operations.'

    @Delegate
    ConsoleLogger consoleLogger = GrailsConsole.getInstance()
    GrailsApplication grailsApplication

    @Override
    boolean handle() {
        if (!args) {
            error("No domain-class specified")
            return FAILURE
        }

        List<String> domainClassNames
        if (args[0].indexOf('*') > -1) {
            def find = args[0].substring(0, args[0].indexOf('*'))
            domainClassNames = resources("file:grails-app/domain/**/*.groovy")
                    .collect { className(it) }.findAll { it.indexOf(find) > -1 }
        } else {
            domainClassNames = args
        }

        boolean overwrite = isFlagPresent('force')
        int failureCount = 0
        for (String domainClassName in domainClassNames) {
            final Resource sourceClass = source(domainClassName)
            if (sourceClass) {
                final Model model = model(sourceClass)
                def modelMap = CommandUtils.getModel(sourceClass, model, grailsApplication)
                render(template: template('scaffolding/AsyncController.groovy'),
                        destination: file("grails-app/controllers/${model.packagePath}/${model.convention('Controller')}.groovy"),
                        model: modelMap,
                        overwrite: overwrite)

                render(template: template('scaffolding/AsyncSpec.groovy'),
                        destination: file("src/test/groovy/${model.packagePath}/${model.convention('ControllerSpec')}.groovy"),
                        model: modelMap,
                        overwrite: overwrite)

                addStatus("Scaffolding complete for ${projectPath(sourceClass)}")
            } else {
                error "No domain-class not found for name: $domainClassName"
                failureCount++
            }
        }
        return failureCount ? FAILURE : SUCCESS
    }
}
