/*
 * Copyright 2016-2018 Andres Almiray
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
package sample.javafx.groovy

application {
    title = 'JavaFX + Groovy'
    startupGroups = ['sample']
    autoShutdown = true
}
mvcGroups {
    sample {
        model      = 'sample.javafx.groovy.SampleModel'
        view       = 'sample.javafx.groovy.SampleView'
        controller = 'sample.javafx.groovy.SampleController'
    }
}