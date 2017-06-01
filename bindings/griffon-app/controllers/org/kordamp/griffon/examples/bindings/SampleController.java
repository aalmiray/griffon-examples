package org.kordamp.griffon.examples.bindings;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

@ArtifactProviderFor(GriffonController.class)
public class SampleController extends AbstractGriffonController {
    @MVCMember private SampleModel model;

    public void add() {
        model.getMeasurements().add(model.nextMeasurement());
    }

    public void clear() {
        model.getMeasurements().clear();
    }
}