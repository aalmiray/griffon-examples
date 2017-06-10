package sample.javafx.kotlin

import griffon.core.artifact.ArtifactManager
import griffon.core.test.GriffonUnitRule
import griffon.core.test.TestFor
import javafx.embed.swing.JFXPanel
import org.junit.Rule
import org.junit.Test

import javax.inject.Inject

import java.util.concurrent.TimeUnit.SECONDS
import org.awaitility.Awaitility.await
import org.hamcrest.Matchers.notNullValue
import org.junit.Assert.assertEquals

@TestFor(SampleController::class)
class SampleControllerTest {
    companion object {
        init {
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace")
            System.setProperty("griffon.full.stacktrace", "true")
            // force initialization JavaFX Toolkit
            JFXPanel()
        }
    }

    @Inject
    lateinit private var artifactManager: ArtifactManager

    lateinit private var controller: SampleController

    @Rule @JvmField
    val griffon = GriffonUnitRule()

    @Test
    fun executeSayHelloActionWithNoInput() {
        val model = artifactManager.newInstance(SampleModel::class.java)

        controller.model = model
        controller.invokeAction("sayHello")

        await().atMost(2, SECONDS)
                .until({ model.output }, notNullValue())
        assertEquals("Howdy stranger!", model.output)
    }

    @Test
    fun executeSayHelloActionWithInput() {
        val model = artifactManager.newInstance(SampleModel::class.java)
        model.input = "Griffon"

        controller.model = model
        controller.invokeAction("sayHello")

        await().atMost(2, SECONDS)
                .until({ model.output }, notNullValue())
        assertEquals("Hello Griffon", model.output)
    }
}
