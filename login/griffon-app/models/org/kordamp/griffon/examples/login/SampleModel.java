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
import griffon.inject.Contextual;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.security.auth.Subject;
import java.security.Principal;
import java.util.Iterator;
import java.util.Map;

import static java.util.Arrays.asList;

@ArtifactProviderFor(GriffonModel.class)
public class SampleModel extends AbstractGriffonModel {
    @Contextual
    private Subject subject;

    private StringProperty content;

    @Nonnull
    public final StringProperty contentProperty() {
        if (content == null) {
            content = new SimpleStringProperty(this, "content", "");
        }
        return content;
    }

    public void setContent(String content) {
        contentProperty().set(content);
    }

    public String getContent() {
        return contentProperty().get();
    }

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        setContent(msg(getClass().getName() + ".content", asList(resolvePrimaryPrincipal().getName())));
    }

    @Nullable
    private Principal resolvePrimaryPrincipal() {
        Iterator<Principal> principals = subject.getPrincipals().iterator();
        return principals.hasNext() ? principals.next() : null;
    }
}