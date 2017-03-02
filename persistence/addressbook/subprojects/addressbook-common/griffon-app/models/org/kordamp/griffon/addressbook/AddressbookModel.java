/*
 * Copyright 2016-2017-2017 Andres Almiray
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

import griffon.core.artifact.GriffonModel;
import griffon.javafx.collections.ElementObservableList;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import lombok.Getter;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import javax.annotation.Nonnull;

import static javafx.collections.FXCollections.observableArrayList;

@ArtifactProviderFor(GriffonModel.class)
public class AddressbookModel extends AbstractGriffonModel {
    @Getter
    private final ObservableContact observableContact = new ObservableContact();

    @Getter
    private final ObservableList<ObservableContact> contacts = new ElementObservableList<>(observableArrayList());

    private IntegerProperty selectedIndex = new SimpleIntegerProperty(this, "selectedIndex", -1);

    @Nonnull
    public IntegerProperty selectedIndexProperty() {
        return selectedIndex;
    }

    public int getSelectedIndex() {
        return selectedIndex.get();
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex.set(selectedIndex);
    }
}