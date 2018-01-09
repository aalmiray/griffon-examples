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
package org.kordamp.griffon.examples.shutdown;

import griffon.core.GriffonApplication;
import griffon.core.ShutdownHandler;
import griffon.core.i18n.MessageSource;
import griffon.core.threading.UIThreadManager;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

public class MyShutdownHandler implements ShutdownHandler {
    private final MessageSource messageSource;
    private final UIThreadManager uiThreadManager;

    @Inject
    public MyShutdownHandler(@Nonnull @Named("applicationMessageSource") MessageSource messageSource, @Nonnull UIThreadManager uiThreadManager) {
        this.messageSource = messageSource;
        this.uiThreadManager = uiThreadManager;
    }

    @Override
    public boolean canShutdown(@Nonnull GriffonApplication application) {
        return uiThreadManager.runInsideUISync(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(msg(".alert.title"));
            alert.setHeaderText(msg(".alert.header"));
            alert.setContentText(msg(".alert.content"));
            return alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.CANCEL;
        });
    }

    private String msg(String key) {
        return messageSource.getMessage(getClass().getName() + key);
    }

    @Override
    public void onShutdown(@Nonnull GriffonApplication application) {
        System.out.println("Saving workspace ...");
    }
}
