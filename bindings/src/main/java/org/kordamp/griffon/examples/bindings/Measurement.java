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
