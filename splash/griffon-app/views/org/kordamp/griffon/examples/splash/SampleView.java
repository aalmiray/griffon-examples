/*
 * Copyright 2016-2018 Andres Almiray
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
package org.kordamp.griffon.examples.splash;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import javax.annotation.Nonnull;
import java.util.Collections;

@ArtifactProviderFor(GriffonView.class)
public class SampleView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull
    private SampleController controller;

    @MVCMember @Nonnull
    private SampleModel model;

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication()
            .createApplicationContainer(Collections.<String, Object>emptyMap());
        stage.setTitle(getApplication().getConfiguration().getAsString("application.title"));
        stage.setScene(init());
        stage.sizeToScene();
        getApplication().getWindowManager().attach("mainWindow", stage);
        getApplication().getWindowManager().show("mainWindow");
    }

    // build the UI
    private Scene init() {
        Parent node = (Parent) loadFromFXML();

        connectActions(node, controller);
        connectMessageSource(node);

        Scene scene = new Scene(node);
        scene.setFill(Color.WHITE);
        scene.getStylesheets().add("bootstrapfx.css");

        return scene;
    }
}
