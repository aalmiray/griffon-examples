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
import griffon.plugins.glazedlists.javafx.EventObservableList;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicBinding;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.annotation.Nonnull;
import java.util.Collections;

import static griffon.javafx.support.JavaFXUtils.findElements;
import static griffon.plugins.glazedlists.javafx.GlazedListsJavaFX.createJavaFXThreadProxyList;
import static javafx.beans.binding.Bindings.createBooleanBinding;

@ArtifactProviderFor(GriffonView.class)
public class AddresbookView extends AbstractJavaFXGriffonView {
    private AddresbookController controller;
    private AddresbookModel model;

    @FXML private TextField name;
    @FXML private TextField lastname;
    @FXML private TextField address;
    @FXML private TextField company;
    @FXML private TextField email;
    @FXML private ListView<ObservableContact> contacts;

    @MVCMember
    public void setController(@Nonnull AddresbookController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull AddresbookModel model) {
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
        contacts.setItems(new EventObservableList(createJavaFXThreadProxyList(model.getContacts())));
        contacts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        contacts.getSelectionModel().selectedIndexProperty().addListener((v, o, n) -> {
            if (n != null) { model.setSelectedIndex(n.intValue()); }
        });

        model.selectedIndexProperty().addListener((v, o, n) -> {
            runInsideUIAsync(() -> {
                if (n.intValue() == -1) {
                    contacts.getSelectionModel().clearSelection();
                    model.getObservableContact().setContact(new Contact());
                } else {
                    contacts.getSelectionModel().select(n.intValue());
                    contacts.requestFocus();
                }
            });
        });

        contacts.setOnMouseClicked(evt -> {
            if (evt.getClickCount() == 2) {
                model.setSelectedIndex(contacts.getSelectionModel().getSelectedIndex());
                model.getObservableContact().setContact(contacts.getSelectionModel().getSelectedItem().getContact());
            }
        });
    }

    private void bindForm() {
        name.textProperty().bindBidirectional(model.getObservableContact().nameProperty());
        lastname.textProperty().bindBidirectional(model.getObservableContact().lastnameProperty());
        address.textProperty().bindBidirectional(model.getObservableContact().addressProperty());
        company.textProperty().bindBidirectional(model.getObservableContact().companyProperty());
        email.textProperty().bindBidirectional(model.getObservableContact().emailProperty());
    }

    private void configureActions(Node node) {
        connectActions(node, controller);

        findElements(node, n -> n instanceof Button).forEach(n -> {
            Node graphic = ((Button) n).getGraphic();
            ((FontIcon) graphic).setIconColor(Color.WHITE);
        });

        toolkitActionFor(controller, "save").enabledProperty().bind(model.getObservableContact().validProperty());
        MonadicBinding<Boolean> hasSelection = EasyBind.map(contacts.getSelectionModel().selectedIndexProperty(), v -> v.intValue() != -1);
        toolkitActionFor(controller, "delete").enabledProperty().bind(hasSelection);
        BooleanBinding hasItems = createBooleanBinding(() -> contacts.getItems().size() != 0, contacts.getItems());
        toolkitActionFor(controller, "print").enabledProperty().bind(hasItems);
    }
}
