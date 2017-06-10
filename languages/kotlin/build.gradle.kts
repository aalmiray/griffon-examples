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

val griffonVersion by project
val kotlinVersion by project
val slf4jVersion by project

buildscript {
    repositories {
        gradleScriptKotlin()
        maven { setUrl("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        classpath(kotlinModule("gradle-plugin"))
    }
}

apply {
    plugin("kotlin")
}

plugins {
    id("org.jetbrains.kotlin.kapt") version "1.1.2-2"
    application
}

application {
    mainClassName = "sample.javafx.kotlin.LauncherKt"
}

repositories {
    jcenter()
    gradleScriptKotlin()
}

dependencies {
    kapt("org.codehaus.griffon:griffon-core-compile:$griffonVersion")
    compileOnly("org.codehaus.griffon:griffon-core-compile:$griffonVersion")

    compile("org.codehaus.griffon:griffon-javafx:$griffonVersion")
    compile("org.codehaus.griffon:griffon-guice:$griffonVersion")
    compile(kotlinModule("stdlib"))

    runtime("org.slf4j:slf4j-simple:$slf4jVersion")

    testCompile("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testCompile("org.codehaus.griffon:griffon-core-test:$griffonVersion")
    testCompile("org.codehaus.griffon:griffon-javafx-test:$griffonVersion")
}

tasks.withType<ProcessResources> {
    filesMatching("**/*.properties") {
        expand(hashMapOf(
                "application_name"    to project.name,
                "application_version" to project.version,
                "griffon_version"     to griffonVersion
        ))
    }
}