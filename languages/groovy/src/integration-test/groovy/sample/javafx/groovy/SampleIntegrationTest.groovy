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
package sample.javafx.groovy

import griffon.javafx.test.GriffonTestFXRule
import groovy.transform.CompileStatic
import org.junit.Rule
import org.junit.Test

import static org.testfx.api.FxAssert.verifyThat
import static org.testfx.matcher.control.LabeledMatchers.hasText

@CompileStatic
class SampleIntegrationTest {
    static {
        System.setProperty('org.slf4j.simpleLogger.defaultLogLevel', 'trace')
        System.setProperty('griffon.full.stacktrace', 'true')
    }

    @Rule
    public GriffonTestFXRule testfx = new GriffonTestFXRule('mainWindow')

    @Test
    void doNotTypeNameAndClickButton() {
        // given:
        testfx.clickOn('#input').write('')

        // when:
        testfx.clickOn('#sayHelloActionTarget')

        // then:
        verifyThat('#output', hasText('Howdy stranger!'))
    }

    @Test
    void typeNameAndClickButton() {
        // given:
        testfx.clickOn('#input').write('Griffon')

        // when:
        testfx.clickOn('#sayHelloActionTarget')

        // then:
        verifyThat('#output', hasText('Hello Griffon'))
    }
}