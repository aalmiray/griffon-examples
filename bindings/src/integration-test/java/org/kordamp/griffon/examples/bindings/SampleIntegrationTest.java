package org.kordamp.griffon.examples.bindings;

import griffon.javafx.test.GriffonTestFXRule;
import org.junit.Rule;
import org.junit.Test;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class SampleIntegrationTest {
    @Rule
    public GriffonTestFXRule testfx = new GriffonTestFXRule("mainWindow");

    @Test
    public void clickButton() {
        // given:
        verifyThat("#clickLabel", hasText("0"));

        // when:
        testfx.clickOn("#clickActionTarget");

        // then:
        verifyThat("#clickLabel", hasText("1"));
    }
}
