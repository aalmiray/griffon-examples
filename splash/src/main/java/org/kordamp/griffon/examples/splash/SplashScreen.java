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

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.tbee.javafx.scene.layout.MigPane;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javafx.application.Platform.runLater;

public class SplashScreen extends Stage {
    private final Label status;
    private final ProgressBar progressBar;

    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    public SplashScreen(String splashUri) {
        super(StageStyle.UNDECORATED);
        setResizable(false);
        setWidth(320);
        setHeight(260);

        StackPane root = new StackPane();
        root.getChildren().add(new ImageView(new Image(splashUri)));

        MigPane pane = new MigPane("fill");
        status = new Label("Loading ...");
        progressBar = new ProgressBar();
        progressBar.setPrefWidth(280);
        progressBar.setMaxWidth(280);

        pane.add(progressBar, "x 20, y 210");
        pane.add(status, "x 20, y 230");
        root.getChildren().add(pane);

        Scene scene = new Scene(root);
        setScene(scene);
    }

    public void setText(@Nonnull String text) {
        runLater(() -> status.setText(text));
    }

    public void setProgress(double value) {
        progressBar.setProgress(value);
    }

    @Override
    public void hide() {
        executorService.shutdown();
        super.hide();
    }
}
