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

import griffon.core.GriffonApplication;
import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import javax.annotation.Nonnull;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static griffon.javafx.beans.binding.CollectionBindings.averageInList;
import static griffon.javafx.beans.binding.MappingBindings.mapObject;
import static griffon.javafx.beans.binding.ReducingBindings.mapToIntegerThenReduce;
import static griffon.javafx.beans.binding.ReducingBindings.reduce;
import static java.util.Arrays.asList;
import static javafx.beans.binding.Bindings.createIntegerBinding;
import static javafx.collections.FXCollections.observableArrayList;

@ArtifactProviderFor(GriffonModel.class)
public class SampleModel extends AbstractGriffonModel {
    private final ObservableList<Measurement> measurements = observableArrayList();
    private final ObservableList<Language> languages = observableArrayList();
    private final ObjectProperty<Language> language = new SimpleObjectProperty<>(this, "language", Language.fromLocale(Locale.getDefault()));
    private final ObjectProperty<Locale> locale = new SimpleObjectProperty<>(this, "locale");

    private int identifier = 0;
    private final SecureRandom random = new SecureRandom();

    private final NumberBinding minAmount;
    private final NumberBinding maxAmount;
    private final NumberBinding avgAmount;
    private final ObjectBinding<Measurement> minMeasurement;
    private final ObjectBinding<Measurement> maxMeasurement;
    private final ObjectBinding<String> minName;
    private final ObjectBinding<String> maxName;
    private final ObjectBinding<String> minTimestamp;
    private final ObjectBinding<String> maxTimestamp;
    private final IntegerBinding total;

    public SampleModel() {
        languages.addAll(asList(Language.values()));
        languages.remove(Language.UNKNOWN);

        minAmount = mapToIntegerThenReduce(measurements, 0, Measurement::getAmount, Math::min);
        maxAmount = mapToIntegerThenReduce(measurements, 0, Measurement::getAmount, Math::max);
        avgAmount = averageInList(measurements, 0, Measurement::getAmount);

        Measurement defaultMeasurement = new Measurement("", 0);
        minMeasurement = reduce(measurements, defaultMeasurement, measureReductor(Math::min));
        maxMeasurement = reduce(measurements, defaultMeasurement, measureReductor(Math::max));

        minName = mapObject(minMeasurement, Measurement::getName);
        maxName = mapObject(maxMeasurement, Measurement::getName);

        Function<Measurement, String> timestampMapper = m -> m != defaultMeasurement ? m.getTimestamp().toString() : "";
        minTimestamp = mapObject(minMeasurement, timestampMapper);
        maxTimestamp = mapObject(maxMeasurement, timestampMapper);

        total = createIntegerBinding(measurements::size, measurements);

        language.addListener((v, o, n) -> {
            if (n != null) { getApplication().setLocaleAsString(n.getCode()); }
        });
    }

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().addPropertyChangeListener(GriffonApplication.PROPERTY_LOCALE, evt -> locale.set((Locale) evt.getNewValue()));
    }

    @Nonnull
    public ObservableList<Measurement> getMeasurements() {
        return measurements;
    }

    @Nonnull
    public ObservableList<Language> getLanguages() {
        return languages;
    }

    @Nonnull
    public ObjectProperty<Language> languageProperty() {
        return language;
    }

    @Nonnull
    public Language getLanguage() {
        return language.get();
    }

    @Nonnull
    public ReadOnlyObjectProperty<Locale> localeProperty() {
        return locale;
    }

    @Nonnull
    public NumberBinding totalBinding() {
        return total;
    }

    @Nonnull
    public NumberBinding minAmountBinding() {
        return minAmount;
    }

    @Nonnull
    public NumberBinding maxAmountBinding() {
        return maxAmount;
    }

    @Nonnull
    public NumberBinding avgAmountBinding() {
        return avgAmount;
    }

    @Nonnull
    public ObjectBinding<String> minNameBinding() {
        return minName;
    }

    @Nonnull
    public ObjectBinding<String> maxNameBinding() {
        return maxName;
    }

    @Nonnull
    public ObjectBinding<String> minTimestampBinding() {
        return minTimestamp;
    }

    @Nonnull
    public ObjectBinding<String> maxTimestampBinding() {
        return maxTimestamp;
    }

    public Measurement nextMeasurement() {
        return new Measurement("Sample-" + nextIdentifier(), nextAmount());
    }

    private int nextIdentifier() {
        return identifier++;
    }

    private int nextAmount() {
        return 10 + random.nextInt(80);
    }

    private static BinaryOperator<Measurement> measureReductor(BinaryOperator<Integer> reductor) {
        return (m1, m2) -> m1.getAmount() == reductor.apply(m1.getAmount(), m2.getAmount()) ? m1 : m2;
    }
}