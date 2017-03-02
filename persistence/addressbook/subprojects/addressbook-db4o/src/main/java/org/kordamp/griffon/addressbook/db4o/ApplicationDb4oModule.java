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
package org.kordamp.griffon.addressbook.db4o;

import griffon.core.injection.Module;
import griffon.inject.DependsOn;
import griffon.plugins.db4o.Db4oBootstrap;
import org.codehaus.griffon.runtime.core.injection.AbstractModule;
import org.kordamp.griffon.addressbook.ContactRepository;
import org.kordamp.jipsy.ServiceProviderFor;

import javax.inject.Named;

@DependsOn("application")
@Named("application-db4o")
@ServiceProviderFor(Module.class)
public class ApplicationDb4oModule extends AbstractModule {
    @Override
    protected void doConfigure() {
        bindContactRepository();
        bindDb4oBootstrap();
        bindIdGenerator();
    }

    protected void bindContactRepository() {
        bind(ContactRepository.class)
            .to(Db4oContactRepository.class)
            .asSingleton();
    }

    protected void bindDb4oBootstrap() {
        bind(Db4oBootstrap.class)
            .to(ContactsDb4oBootstrap.class)
            .asSingleton();
    }

    protected void bindIdGenerator() {
        bind(IdGenerator.class)
            .to(DefaultIdGenerator.class)
            .asSingleton();
    }
}