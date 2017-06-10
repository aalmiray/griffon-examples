package sample.javafx.kotlin

import griffon.core.artifact.GriffonController
import griffon.core.controller.ControllerAction
import griffon.inject.MVCMember
import griffon.metadata.ArtifactProviderFor
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController
import javax.annotation.Nonnull
import javax.inject.Inject

@ArtifactProviderFor(GriffonController::class)
class SampleController : AbstractGriffonController() {
    @set:[MVCMember Nonnull]
    lateinit var model: SampleModel

    @Inject
    lateinit var sampleService: SampleService

    @ControllerAction
    fun sayHello() {
        model.output = sampleService.sayHello(model.input)
    }
}