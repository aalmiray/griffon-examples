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
package org.kordamp.griffon.examples.login;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.javafx.beans.binding.MappingBindings;
import griffon.javafx.beans.binding.UIThreadAwareBindings;
import griffon.metadata.ArtifactProviderFor;
import griffon.util.GriffonNameUtils;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Map;

@ArtifactProviderFor(GriffonView.class)
public class LoginView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull
    private LoginController controller;
    @MVCMember @Nonnull
    private LoginModel model;

    @FXML private TextField username;
    @FXML private PasswordField password;

    private StringProperty uiUsername;
    private StringProperty uiPassword;

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication()
            .createApplicationContainer(Collections.<String, Object>emptyMap());
        stage.setTitle(getApplication().getConfiguration().getAsString("application.title"));
        stage.setScene(init());
        stage.sizeToScene();
        getApplication().getWindowManager().attach("loginWindow", stage);
    }

    private Scene init() {
        Parent node = (Parent) loadFromFXML();

        uiUsername = UIThreadAwareBindings.uiThreadAwareStringProperty(username.textProperty());
        uiPassword = UIThreadAwareBindings.uiThreadAwareStringProperty(password.textProperty());

        model.usernameProperty().bindBidirectional(uiUsername);
        model.passwordProperty().bindBidirectional(uiPassword);

        BooleanBinding usernameIsNotBlank = MappingBindings.mapAsBoolean(uiUsername, GriffonNameUtils::isNotBlank);
        BooleanBinding passwordIsNotBlank = MappingBindings.mapAsBoolean(uiPassword, GriffonNameUtils::isNotBlank);
        BooleanBinding ready = MappingBindings.mapBooleans(usernameIsNotBlank, passwordIsNotBlank, false, (u, p) -> u && p);
        toolkitActionFor(controller, "login").enabledProperty().bind(ready);
        connectActions(node, controller);
        connectMessageSource(node);

        Scene scene = new Scene(node);
        scene.setFill(Color.WHITE);
        scene.getStylesheets().add("bootstrapfx.css");

        return scene;
    }

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(this);
    }

    @Override
    public void mvcGroupDestroy() {
        getApplication().getEventRouter().removeEventListener(this);
        getApplication().getWindowManager().hide("loginWindow");
    }

    public void onLoginFailureEvent(LoginFailureEvent event) {
        runInsideUISync(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(msg("login.error.title"));
            alert.setHeaderText(msg("login.header.text"));
            alert.setContentText(msg("login.content.text"));
            alert.showAndWait();
        });
    }
}
