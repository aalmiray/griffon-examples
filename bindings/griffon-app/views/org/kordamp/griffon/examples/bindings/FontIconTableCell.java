/*
 * Copyright 2016-2017 Andres Almiray
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

import griffon.javafx.util.ToStringOnlyStringConverter;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.annotation.Nonnull;

import static java.util.Objects.requireNonNull;

public class FontIconTableCell<S, T> extends TableCell<S, T> {
    private static final String ERROR_CONVERTER_NULL = "Argument 'converter' must not be null";

    @Nonnull
    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn() {
        return param -> new FontIconTableCell<S, T>();
    }

    @Nonnull
    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(@Nonnull StringConverter<T> converter) {
        return param -> new FontIconTableCell<S, T>(converter);
    }

    private Subscription subscription;
    private final FontIcon icon;
    private final ObjectProperty<StringConverter<T>> converter = new SimpleObjectProperty<StringConverter<T>>(this, "converter");

    @SuppressWarnings("unchecked")
    public FontIconTableCell() {
        this(new ToStringOnlyStringConverter<>());
    }

    public FontIconTableCell(@Nonnull StringConverter<T> converter) {
        this.getStyleClass().add("font-icon-table-cell");
        this.icon = new FontIcon();
        setConverter(requireNonNull(converter, ERROR_CONVERTER_NULL));
    }

    @Nonnull
    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return converter;
    }

    public final void setConverter(@Nonnull StringConverter<T> converter) {
        converterProperty().set(requireNonNull(converter, ERROR_CONVERTER_NULL));
    }

    @Nonnull
    public final StringConverter<T> getConverter() {
        return converterProperty().get();
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            if (subscription != null) {
                subscription.unsubscribe();
                subscription = null;
            }

            final TableColumn<S, T> column = getTableColumn();
            ObservableValue<T> observable = column == null ? null : column.getCellObservableValue(getIndex());

            if (observable != null) {
                ChangeListener<T> listener = (v, o, n) -> setIconCode(n);
                observable.addListener(listener);
                subscription = () -> observable.removeListener(listener);
                setIconCode(observable.getValue());
            } else if (item != null) {
                setIconCode(item);
            }

            setGraphic(icon);
            setAlignment(Pos.CENTER);
        }
    }

    private void setIconCode(T value) {
        if (value instanceof Ikon) {
            icon.setIconCode((Ikon) value);
        } else {
            icon.setIconLiteral(getConverter().toString(value));
        }
    }

    private interface Subscription {
        void unsubscribe();
    }
}
