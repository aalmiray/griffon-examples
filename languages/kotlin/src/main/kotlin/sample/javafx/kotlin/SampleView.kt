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
package sample.javafx.kotlin

import griffon.core.artifact.GriffonController
import griffon.core.artifact.GriffonView
import griffon.inject.MVCMember
import griffon.javafx.beans.binding.UIThreadAwareBindings
import griffon.metadata.ArtifactProviderFor
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.fxml.FXML
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.Window
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView
import javax.annotation.Nonnull

@ArtifactProviderFor(GriffonView::class)
class SampleView : AbstractJavaFXGriffonView() {
    @set:[MVCMember Nonnull]
    lateinit var controller: SampleController
    @set:[MVCMember Nonnull]
    lateinit var model: SampleModel

    @FXML
    lateinit private var input: TextField
    @FXML
    lateinit private var output: Label

    override fun initUI() {
        val stage: Stage = application.createApplicationContainer(mapOf()) as Stage
        stage.title = application.configuration.getAsString("application.title")
        stage.width = 400.0
        stage.height = 120.0
        stage.scene = _init()
        application.getWindowManager<Window>().attach("mainWindow", stage)
    }

    private fun _init(): Scene {
        val scene: Scene = Scene(Group())
        scene.fill = Color.WHITE

        val node = loadFromFXML()
        input.textProperty().bindBidirectional(model.inputProperty())
        output.textProperty().bind(UIThreadAwareBindings.uiThreadAwareStringProperty(model.outputProperty()))
        (scene.root as Group).children.addAll(node)
        connectActions(node as Any, controller as GriffonController)
        return scene
    }
}