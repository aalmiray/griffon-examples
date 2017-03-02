/*
 * Copyright 2016-2017-2017 Andres Almiray
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
package org.kordamp.griffon.addressbook.jpa;

import griffon.core.injection.Module;
import griffon.inject.DependsOn;
import griffon.plugins.jpa.JpaBootstrap;
import org.codehaus.griffon.runtime.core.injection.AbstractModule;
import org.kordamp.griffon.addressbook.ContactRepository;
import org.kordamp.jipsy.ServiceProviderFor;

import javax.inject.Named;

@DependsOn("application")
@Named("application-jpa")
@ServiceProviderFor(Module.class)
public class ApplicationJpaModule extends AbstractModule {
    @Override
    protected void doConfigure() {
        bindContactRepository();
        bindJpaBootstrap();
    }

    protected void bindContactRepository() {
        bind(ContactRepository.class)
            .to(JpaContactRepository.class)
            .asSingleton();
    }

    protected void bindJpaBootstrap() {
        bind(JpaBootstrap.class)
            .to(ContactsJpaBootstrap.class)
            .asSingleton();
    }
}