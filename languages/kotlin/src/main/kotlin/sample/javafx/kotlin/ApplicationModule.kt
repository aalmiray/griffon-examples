package sample.javafx.kotlin

import griffon.core.injection.Module
import griffon.util.AnnotationUtils.named
import org.codehaus.griffon.runtime.core.injection.AbstractModule
import org.codehaus.griffon.runtime.util.ResourceBundleProvider
import org.kordamp.jipsy.ServiceProviderFor
import java.util.ResourceBundle

@ServiceProviderFor(Module::class)
class ApplicationModule : AbstractModule() {
    override fun doConfigure() {
        bind(ResourceBundle::class.java)
                .withClassifier(named("applicationResourceBundle"))
                .toProvider(ResourceBundleProvider("sample.javafx.kotlin.Config"))
                .asSingleton()
    }
}