package sample.javafx.kotlin;

import griffon.core.artifact.GriffonModel
import griffon.metadata.ArtifactProviderFor
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel

@ArtifactProviderFor(GriffonModel::class)
class SampleModel : AbstractGriffonModel() {
    private var _input: StringProperty = SimpleStringProperty(this, "input", "")

    var input: String
        get() = _input.get()
        set(s) = _input.set(s)

    fun inputProperty() = _input

    private var _output: StringProperty = SimpleStringProperty(this, "output", "")

    var output: String
        get() = _output.get()
        set(s) = _output.set(s)

    fun outputProperty() = _output
}