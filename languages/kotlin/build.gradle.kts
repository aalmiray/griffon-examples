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

import org.gradle.api.internal.HasConvention
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

val griffonVersion by project
val kotlinVersion by project
val slf4jVersion by project

plugins {
    application
    kotlin("jvm", "1.1.4-2")
    id("org.jetbrains.kotlin.kapt") version "1.1.4-2"
}

application {
    mainClassName = "sample.javafx.kotlin.LauncherKt"
}

repositories {
    jcenter()
    gradleScriptKotlin()
}

val sourceSets = java.sourceSets!!
val SourceSet.kotlin: SourceDirectorySet
    get() = (this as HasConvention).convention.getPlugin<KotlinSourceSet>().kotlin
val SourceSetContainer.main: SourceSet get() = getByName("main")
val SourceSetContainer.test: SourceSet get() = getByName("test")
fun SourceDirectorySet.sourceDirs(srcDirs: () -> Iterable<File>) {
    setSrcDirs(srcDirs())
}

// Warning:<i><b>root project 'sample-kotlin': Unable to resolve all content root directories</b>
// Details: java.lang.NullPointerException: null</i>

val integrationTest by sourceSets.creating {
    java.sourceDirs { files("src/integration-test/java") }
    kotlin.sourceDirs { files("src/integration-test/kotlin") }
    resources.sourceDirs { files("src/integration-test/resources") }
    compileClasspath += sourceSets.main.output
    runtimeClasspath += compileClasspath
}

val functionalTest by sourceSets.creating {
    java.sourceDirs { files("src/functional-test/java") }
    kotlin.sourceDirs { files("src/functional-test/kotlin") }
    resources.sourceDirs { files("src/functional-test/resources") }
    compileClasspath += sourceSets.main.output
    runtimeClasspath += compileClasspath
}

dependencies {
    kapt("org.codehaus.griffon:griffon-core-compile:$griffonVersion")
    compileOnly("org.codehaus.griffon:griffon-core-compile:$griffonVersion")

    compile("org.codehaus.griffon:griffon-javafx:$griffonVersion")
    compile("org.codehaus.griffon:griffon-guice:$griffonVersion")
    compile(kotlin("stdlib", "$kotlinVersion"))

    runtime("org.slf4j:slf4j-simple:$slf4jVersion")

    testCompile("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testCompile("org.codehaus.griffon:griffon-core-test:$griffonVersion")
    testCompile("org.codehaus.griffon:griffon-javafx-test:$griffonVersion")

    add(integrationTest.compileConfigurationName, "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    add(integrationTest.compileConfigurationName, "org.codehaus.griffon:griffon-core-test:$griffonVersion")
    add(integrationTest.compileConfigurationName, "org.codehaus.griffon:griffon-javafx-test:$griffonVersion")

    add(functionalTest.compileConfigurationName, "org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    add(functionalTest.compileConfigurationName, "org.codehaus.griffon:griffon-core-test:$griffonVersion")
    add(functionalTest.compileConfigurationName, "org.codehaus.griffon:griffon-javafx-test:$griffonVersion")
}

tasks.withType<ProcessResources> {
    filesMatching("**/*.properties") {
        expand(mapOf(
            "application_name" to project.name,
            "application_version" to project.version,
            "griffon_version" to griffonVersion
        ))
    }
}

