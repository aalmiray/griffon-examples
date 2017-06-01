package org.kordamp.griffon.examples.bindings;

import griffon.javafx.test.FunctionalJavaFXRunner;
import griffon.javafx.test.GriffonTestFXClassRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

@RunWith(FunctionalJavaFXRunner.class)
public class SampleFunctionalTest {
    @ClassRule
    public static GriffonTestFXClassRule testfx = new GriffonTestFXClassRule("mainWindow");

    @Test
    public void _01_clickButton() {
        // given:
        verifyThat("#clickLabel", hasText("0"));

        // when:
        testfx.clickOn("#clickActionTarget");

        // then:
        verifyThat("#clickLabel", hasText("1"));
    }
}
