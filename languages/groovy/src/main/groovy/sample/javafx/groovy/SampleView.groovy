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

import griffon.core.artifact.GriffonView
import griffon.inject.MVCMember
import griffon.metadata.ArtifactProviderFor
import groovy.transform.TypeChecked
import javafx.fxml.FXML
import javafx.scene.Group
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.paint.Color
import javafx.stage.Stage
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView

import javax.annotation.Nonnull

import static griffon.javafx.beans.binding.UIThreadAwareBindings.uiThreadAwareStringProperty

@TypeChecked
@ArtifactProviderFor(GriffonView)
class SampleView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull
    SampleController controller
    @MVCMember @Nonnull
    SampleModel model

    @FXML
    private TextField input
    @FXML
    private Label output

    @Override
    void initUI() {
        Stage stage = (Stage) application.createApplicationContainer([:])
        stage.title = application.configuration.getAsString('application.title')
        stage.width = 400
        stage.height = 120
        stage.scene = init()
        application.windowManager.attach('mainWindow', stage)
    }

    // build the UI
    private Scene init() {
        Scene scene = new Scene(new Group())
        scene.fill = Color.WHITE

        Node node = loadFromFXML()
        input.textProperty().bindBidirectional(model.inputProperty())
        output.textProperty().bind(uiThreadAwareStringProperty(model.outputProperty()))
        scene.root.children.addAll(node)
        connectActions(node, controller)

        scene
    }
}
