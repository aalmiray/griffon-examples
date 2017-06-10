package sample.javafx.kotlin

import griffon.javafx.test.GriffonTestFXRule
import javafx.embed.swing.JFXPanel
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import org.junit.Rule
import org.junit.Test

import org.testfx.api.FxAssert.verifyThat
import org.testfx.matcher.control.LabeledMatchers.hasText

class SampleIntegrationTest {
    companion object {
        init {
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace")
            System.setProperty("griffon.full.stacktrace", "true")
        }
    }

    @Rule @JvmField
    val testfx = GriffonTestFXRule("mainWindow")

    @Test
    fun doNotTypeNameAndClickButton() {
        // given:
        testfx.clickOn("#input").write("")

        // when:
        testfx.clickOn("#sayHelloActionTarget")

        // then:
        verifyThat<Node>("#output", hasText("Howdy stranger!"))
    }

    @Test
    fun typeNameAndClickButton() {
        // given:
        testfx.clickOn("#input").write("Griffon")

        // when:
        testfx.clickOn("#sayHelloActionTarget")

        // then:
        verifyThat<Node>("#output", hasText("Hello Griffon"))
    }
}