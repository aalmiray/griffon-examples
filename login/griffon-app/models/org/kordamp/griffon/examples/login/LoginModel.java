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
package org.kordamp.griffon.examples.login;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonModel.class)
public class LoginModel extends AbstractGriffonModel {
    private StringProperty username;
    private StringProperty password;

    @Nonnull
    public final StringProperty usernameProperty() {
        if (username == null) {
            username = new SimpleStringProperty(this, "username", "");
        }
        return username;
    }

    public void setUsername(String username) {
        usernameProperty().set(username);
    }

    public String getUsername() {
        return usernameProperty().get();
    }

    @Nonnull
    public final StringProperty passwordProperty() {
        if (password == null) {
            password = new SimpleStringProperty(this, "password", "");
        }
        return password;
    }

    public void setPassword(String password) {
        passwordProperty().set(password);
    }

    public String getPassword() {
        return passwordProperty().get();
    }
}