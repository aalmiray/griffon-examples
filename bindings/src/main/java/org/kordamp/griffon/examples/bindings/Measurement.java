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
package org.kordamp.griffon.examples.bindings;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class Measurement {
    private final StringProperty name = new SimpleStringProperty(this, "name", "");
    private final IntegerProperty amount = new SimpleIntegerProperty(this, "amount", 0);
    private final ObjectProperty<LocalDateTime> timestamp = new SimpleObjectProperty<>(this, "timestamp", LocalDateTime.now());

    public Measurement(String name, int amount) {
        this.name.set(name);
        this.amount.set(amount);
    }

    public ReadOnlyStringProperty nameProperty() {
        return name;
    }

    public ReadOnlyIntegerProperty amountProperty() {
        return amount;
    }

    public ReadOnlyObjectProperty<LocalDateTime> timestampProperty() {
        return timestamp;
    }

    public String getName() {
        return name.get();
    }

    public int getAmount() {
        return amount.get();
    }

    public LocalDateTime getTimestamp() {
        return timestamp.get();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Measurement{");
        sb.append("name=").append(getName());
        sb.append(", amount=").append(getAmount());
        sb.append(", timestamp=").append(getTimestamp());
        sb.append('}');
        return sb.toString();
    }
}
