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

import griffon.core.artifact.ArtifactManager
import griffon.core.test.GriffonUnitRule
import griffon.core.test.TestFor
import javafx.embed.swing.JFXPanel
import org.junit.Rule
import org.junit.Test

import javax.inject.Inject

import static java.util.concurrent.TimeUnit.SECONDS
import static org.awaitility.Awaitility.await
import static org.hamcrest.Matchers.notNullValue

@TestFor(SampleController)
class SampleControllerTest {
    static {
        System.setProperty('org.slf4j.simpleLogger.defaultLogLevel', 'trace')
        System.setProperty('griffon.full.stacktrace', 'true')
        // force initialization JavaFX Toolkit
        new JFXPanel()
    }

    @Inject
    private ArtifactManager artifactManager

    private SampleController controller

    @Rule
    public final GriffonUnitRule griffon = new GriffonUnitRule()

    @Test
    void executeSayHelloActionWithNoInput() {
        SampleModel model = artifactManager.newInstance(SampleModel)

        controller.model = model
        controller.invokeAction('sayHello')

        await().atMost(2, SECONDS)
            .until({ model.output }, notNullValue())
        assert 'Howdy stranger!' == model.output
    }

    @Test
    void executeSayHelloActionWithInput() {
        final SampleModel model = artifactManager.newInstance(SampleModel)
        model.input = 'Griffon'

        controller.model = model
        controller.invokeAction('sayHello')

        await().atMost(2, SECONDS)
            .until({ model.output }, notNullValue())
        assert 'Hello Griffon' == model.output
    }
}
