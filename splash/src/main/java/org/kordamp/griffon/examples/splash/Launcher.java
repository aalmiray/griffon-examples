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

import griffon.core.GriffonApplication;
import griffon.javafx.JavaFXGriffonApplication;
import javafx.application.Platform;

import javax.annotation.Nonnull;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static griffon.util.AnnotationUtils.named;
import static java.util.Collections.singletonList;

public class Launcher extends JavaFXGriffonApplication {
    public static void main(String[] args) {
        run(Launcher.class, args);
    }

    private SplashScreen splashScreen;

    @Override
    public void init() throws Exception {
        Platform.runLater(() -> {
            splashScreen = new SplashScreen("splash.png");
            splashScreen.show();
        });
        super.init();
    }

    @Override
    protected void afterInit() {
        super.afterInit();
        getEventRouter().addEventListener(this);
    }

    public void onReadyStart(@Nonnull final GriffonApplication application) {
        Platform.runLater(splashScreen::toFront);
    }

    public void onStartupStart(@Nonnull final GriffonApplication application) {
        getInjector().getInstance(ExecutorService.class, named("defaultExecutorService")).submit(() -> {
                for (int i = 1; i < 11; i++) {
                    pause(500);
                    getEventRouter().publishEvent("Load", singletonList(i));
                }
            }
        );
    }

    public void onLoad(int i) {
        splashScreen.setText("Loading " + i + " ...");
        if (i == 10) {
            pause(500);
            Platform.runLater(splashScreen::hide);
        }
    }

    private void pause(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException ignored) {
            // ignore
        }
    }
}