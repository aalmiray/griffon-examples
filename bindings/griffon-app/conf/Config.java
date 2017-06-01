import griffon.util.AbstractMapResourceBundle;

import javax.annotation.Nonnull;
import java.util.Map;

import static java.util.Arrays.asList;
import static griffon.util.CollectionUtils.map;

public class Config extends AbstractMapResourceBundle {
    @Override
    protected void initialize(@Nonnull Map<String, Object> entries) {
        map(entries)
            .e("application", map()
                .e("title", "Bindings")
                .e("startupGroups", asList("sample"))
                .e("autoShutdown", true)
            )
            .e("mvcGroups", map()
                .e("sample", map()
                    .e("model", "org.kordamp.griffon.examples.bindings.SampleModel")
                    .e("view", "org.kordamp.griffon.examples.bindings.SampleView")
                    .e("controller", "org.kordamp.griffon.examples.bindings.SampleController")
                )
            );
    }
}