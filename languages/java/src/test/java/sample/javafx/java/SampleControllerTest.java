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

import griffon.core.artifact.ArtifactManager;
import griffon.core.test.GriffonUnitRule;
import griffon.core.test.TestFor;
import javafx.embed.swing.JFXPanel;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

@TestFor(SampleController.class)
public class SampleControllerTest {
    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
        System.setProperty("griffon.full.stacktrace", "true");
        // force initialization JavaFX Toolkit
        new JFXPanel();
    }

    @Inject
    private ArtifactManager artifactManager;

    private SampleController controller;

    @Rule
    public final GriffonUnitRule griffon = new GriffonUnitRule();

    @Test
    public void executeSayHelloActionWithNoInput() {
        final SampleModel model = artifactManager.newInstance(SampleModel.class);

        controller.setModel(model);
        controller.invokeAction("sayHello");

        await().atMost(2, SECONDS)
            .until(() -> model.getOutput(), notNullValue());
        assertEquals("Howdy stranger!", model.getOutput());
    }

    @Test
    public void executeSayHelloActionWithInput() {
        final SampleModel model = artifactManager.newInstance(SampleModel.class);
        model.setInput("Griffon");

        controller.setModel(model);
        controller.invokeAction("sayHello");

        await().atMost(2, SECONDS)
            .until(() -> model.getOutput(), notNullValue());
        assertEquals("Hello Griffon", model.getOutput());
    }
}
