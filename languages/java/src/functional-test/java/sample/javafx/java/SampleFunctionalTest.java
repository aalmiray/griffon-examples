/*
 * Copyright 2016-2017 Andres Almiray
 *
 * This file is part of Griffon Examples
 *
 * Griffon Examples is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Griffon Examples is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Griffon Examples. If not, see <http://www.gnu.org/licenses/>.
 */
package sample.javafx.java;

import griffon.javafx.test.FunctionalJavaFXRunner;
import griffon.javafx.test.GriffonTestFXClassRule;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

@RunWith(FunctionalJavaFXRunner.class)
public class SampleFunctionalTest {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
        System.setProperty("griffon.full.stacktrace", "true");
    }

    @ClassRule
    public static GriffonTestFXClassRule testfx = new GriffonTestFXClassRule("mainWindow");

    @Test
    public void _01_doNotTypeNameAndClickButton() {
        // given:
        testfx.clickOn("#input").write("");

        // when:
        testfx.clickOn("#sayHelloActionTarget");

        // then:
        verifyThat("#output", hasText("Howdy stranger!"));
    }

    @Test
    public void _02_typeNameAndClickButton() {
        // given:
        testfx.clickOn("#input").write("Griffon");

        // when:
        testfx.clickOn("#sayHelloActionTarget");

        // then:
        verifyThat("#output", hasText("Hello Griffon"));
    }
}