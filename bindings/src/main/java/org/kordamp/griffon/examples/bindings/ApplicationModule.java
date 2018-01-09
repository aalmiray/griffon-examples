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
package org.kordamp.griffon.examples.bindings;

import griffon.core.event.EventHandler;
import griffon.core.injection.Module;
import org.codehaus.griffon.runtime.core.injection.AbstractModule;
import org.kordamp.jipsy.ServiceProviderFor;

@ServiceProviderFor(Module.class)
public class ApplicationModule extends AbstractModule {
    @Override
    protected void doConfigure() {
        bind(EventHandler.class)
            .to(ApplicationEventHandler.class)
            .asSingleton();
    }
}