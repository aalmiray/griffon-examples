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
package org.kordamp.griffon.addressbook;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;

import javax.annotation.Nonnull;
import java.util.Collections;

import static griffon.javafx.beans.binding.MappingBindings.mapAsBoolean;
import static griffon.javafx.beans.binding.UIThreadAwareBindings.uiThreadAwareIntegerProperty;
import static griffon.javafx.beans.binding.UIThreadAwareBindings.uiThreadAwareStringProperty;
import static griffon.javafx.collections.GriffonFXCollections.uiThreadAwareObservableList;
import static javafx.beans.binding.Bindings.createBooleanBinding;

@ArtifactProviderFor(GriffonView.class)
public class AddressbookView extends AbstractJavaFXGriffonView {
    private AddressbookController controller;
    private AddressbookModel model;

    @FXML private TextField name;
    @FXML private TextField lastname;
    @FXML private TextField address;
    @FXML private TextField company;
    @FXML private TextField email;
    @FXML private ListView<ObservableContact> contacts;

    private IntegerProperty uiSelectedIndex;
    private StringProperty uiNameProperty;
    private StringProperty uiLastnameProperty;
    private StringProperty uiAddressProperty;
    private StringProperty uiCompanyProperty;
    private StringProperty uiEmailProperty;

    @MVCMember
    public void setController(@Nonnull AddressbookController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull AddressbookModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication()
            .createApplicationContainer(Collections.<String, Object>emptyMap());
        stage.setTitle(getApplication().getConfiguration().getAsString("application.title"));
        stage.setScene(init());
        stage.sizeToScene();
        stage.setResizable(false);
        getApplication().getWindowManager().attach("mainWindow", stage);
    }

    // build the UI
    private Scene init() {
        Scene scene = new Scene((Parent) loadFromFXML());
        scene.setFill(Color.WHITE);
        scene.getStylesheets().add("bootstrapfx.css");

        bindContacts();
        bindForm();
        configureActions(scene.getRoot());

        return scene;
    }

    private void bindContacts() {
        contacts.setItems(uiThreadAwareObservableList(model.getContacts()));
        contacts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        contacts.getSelectionModel().selectedIndexProperty().addListener((v, o, n) -> {
            if (n != null) { model.setSelectedIndex(n.intValue()); }
        });

        uiSelectedIndex = uiThreadAwareIntegerProperty(model.selectedIndexProperty());

        uiSelectedIndex.addListener((v, o, n) -> {
            if (n.intValue() == -1) {
                contacts.getSelectionModel().clearSelection();
                model.getObservableContact().setContact(new Contact());
            } else {
                contacts.getSelectionModel().select(n.intValue());
                contacts.requestFocus();
            }
        });

        contacts.setOnMouseClicked(evt -> {
            if (evt.getClickCount() == 2) {
                model.setSelectedIndex(contacts.getSelectionModel().getSelectedIndex());
                model.getObservableContact().setContact(contacts.getSelectionModel().getSelectedItem().getContact());
            }
        });
    }

    private void bindForm() {
        uiNameProperty = uiThreadAwareStringProperty(model.getObservableContact().nameProperty());
        uiLastnameProperty = uiThreadAwareStringProperty(model.getObservableContact().lastnameProperty());
        uiAddressProperty = uiThreadAwareStringProperty(model.getObservableContact().addressProperty());
        uiCompanyProperty = uiThreadAwareStringProperty(model.getObservableContact().companyProperty());
        uiEmailProperty = uiThreadAwareStringProperty(model.getObservableContact().emailProperty());

        name.textProperty().bindBidirectional(uiNameProperty);
        lastname.textProperty().bindBidirectional(uiLastnameProperty);
        address.textProperty().bindBidirectional(uiAddressProperty);
        company.textProperty().bindBidirectional(uiCompanyProperty);
        email.textProperty().bindBidirectional(uiEmailProperty);
    }

    private void configureActions(Node node) {
        connectActions(node, controller);

        toolkitActionFor(controller, "save").enabledProperty().bind(model.getObservableContact().readyProperty());
        BooleanBinding hasSelection = mapAsBoolean(contacts.getSelectionModel().selectedIndexProperty(), v -> v.intValue() != -1);
        toolkitActionFor(controller, "delete").enabledProperty().bind(hasSelection);
        BooleanBinding hasItems = createBooleanBinding(() -> contacts.getItems().size() != 0, contacts.getItems());
        toolkitActionFor(controller, "print").enabledProperty().bind(hasItems);
    }
}
