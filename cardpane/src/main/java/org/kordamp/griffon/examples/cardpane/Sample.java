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
package org.kordamp.griffon.examples.cardpane;

import griffon.javafx.scene.layout.IndexedCardPane;
import griffon.javafx.scene.layout.NamedCardPane;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static griffon.javafx.beans.binding.MappingBindings.mapAsString;

public class Sample extends Application {
    @FXML private IndexedCardPane indexedCardPane;
    @FXML private NamedCardPane namedCardPane;
    @FXML private Label indexLabel;
    @FXML private ChoiceBox<String> frameChoice;

    public static void main(String[] args) throws Exception {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("CardPanes");
        stage.setScene(initUI());
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();

        Platform.runLater(() -> {
            indexedCardPane.show(0);
            frameChoice.setValue("frame-0");
        });
    }

    private Scene initUI() {
        Node node = loadFxml();

        for (int i = 0; i < 17; i++) {
            Image image = new Image(getClass()
                .getResourceAsStream("frames/T" + (i + 1) + ".gif"));
            ImageView iv = new ImageView(image);
            iv.setId("indexed-" + i);
            indexedCardPane.add(iv);
            iv = new ImageView(image);
            iv.setId("frame-" + i);
            namedCardPane.add(iv.getId(), iv);
            frameChoice.getItems().add(iv.getId());
        }

        indexLabel.textProperty().bind(mapAsString(indexedCardPane.selectedIndexProperty(), n -> String.valueOf(n.intValue())));

        frameChoice.valueProperty().addListener((v, o, n) -> namedCardPane.show(n));

        Scene scene = new Scene(new Group(node));
        scene.setFill(Color.WHITE);
        scene.getStylesheets().add("bootstrapfx.css");

        return scene;
    }

    private Node loadFxml() {
        FXMLLoader fxml = new FXMLLoader(getClass().getResource("sample.fxml"));
        fxml.setControllerFactory(klass -> Sample.this);
        try {
            Parent node = (Parent) fxml.load();
            URL css = getClass().getResource("sample.css");
            node.getStylesheets().add(css.toExternalForm());
            return node;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void previousIndex(ActionEvent ignored) {
        indexedCardPane.previous();
    }

    public void nextIndex(ActionEvent ignored) {
        indexedCardPane.next();
    }

    public void firstIndex(ActionEvent ignored) {
        indexedCardPane.first();
    }

    public void lastIndex(ActionEvent ignored) {
        indexedCardPane.last();
    }
}