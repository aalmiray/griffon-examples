package sample.javafx.kotlin

import griffon.util.AbstractMapResourceBundle

class Config : AbstractMapResourceBundle() {
    override fun initialize(entries: MutableMap<String, Any>) {
        entries.put("application", hashMapOf(
                "title" to "JavaFX + Kotlin",
                "startupGroups" to listOf("sample"),
                "autoshutdown" to true
        ))
        entries.put("mvcGroups", hashMapOf(
                "sample" to hashMapOf(
                        "model" to "sample.javafx.kotlin.SampleModel",
                        "view" to "sample.javafx.kotlin.SampleView",
                        "controller" to "sample.javafx.kotlin.SampleController"
                )
        ))
    }
}