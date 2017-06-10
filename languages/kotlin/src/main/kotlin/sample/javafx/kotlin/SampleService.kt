package sample.javafx.kotlin

import griffon.core.artifact.GriffonService
import griffon.metadata.ArtifactProviderFor
import griffon.util.GriffonNameUtils.isBlank
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService

@ArtifactProviderFor(GriffonService::class)
class SampleService : AbstractGriffonService() {
    fun sayHello(input: String?): String {
        return if (isBlank(input)) {
            application.messageSource.getMessage("greeting.default")
        } else {
            application.messageSource.getMessage("greeting.parameterized", listOf(input))
        }
    }
}