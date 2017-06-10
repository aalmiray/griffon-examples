package sample.javafx.kotlin

import griffon.javafx.test.FunctionalJavaFXRunner
import griffon.javafx.test.GriffonTestFXClassRule
import javafx.scene.Node
import org.junit.ClassRule
import org.junit.Test
import org.junit.runner.RunWith

import org.testfx.api.FxAssert.verifyThat
import org.testfx.matcher.control.LabeledMatchers.hasText

@RunWith(FunctionalJavaFXRunner::class)
class SampleFunctionalTest {
    companion object {
        init {
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace")
            System.setProperty("griffon.full.stacktrace", "true")
        }

        @ClassRule @JvmField
        val testfx = GriffonTestFXClassRule("mainWindow")
    }

    @Test
    fun _01_doNotTypeNameAndClickButton() {
        // given:
        testfx.clickOn("#input").write("")

        // when:
        testfx.clickOn("#sayHelloActionTarget")

        // then:
        verifyThat<Node>("#output", hasText("Howdy stranger!"))
    }

    @Test
    fun _02_typeNameAndClickButton() {
        // given:
        testfx.clickOn("#input").write("Griffon")

        // when:
        testfx.clickOn("#sayHelloActionTarget")

        // then:
        verifyThat<Node>("#output", hasText("Hello Griffon"))
    }
}