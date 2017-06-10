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
package sample.javafx.java;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonModel.class)
public class SampleModel extends AbstractGriffonModel {
    private StringProperty input = new SimpleStringProperty(this, "input");
    private StringProperty output = new SimpleStringProperty(this, "output");

    @Nonnull
    public final StringProperty inputProperty() {
        return input;
    }

    public void setInput(String input) {
        inputProperty().set(input);
    }

    public String getInput() {
        return inputProperty().get();
    }

    @Nonnull
    public final StringProperty outputProperty() {
        return output;
    }

    public void setOutput(String output) {
        outputProperty().set(output);
    }

    public String getOutput() {
        return outputProperty().get();
    }
}