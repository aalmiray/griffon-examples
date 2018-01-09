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
package org.kordamp.griffon.addressbook.sql2o;

import griffon.core.injection.Module;
import griffon.inject.DependsOn;
import griffon.plugins.sql2o.Sql2oBootstrap;
import org.codehaus.griffon.runtime.core.injection.AbstractModule;
import org.kordamp.griffon.addressbook.ContactRepository;
import org.kordamp.jipsy.ServiceProviderFor;

import javax.inject.Named;

@DependsOn("application")
@Named("application-sql2o")
@ServiceProviderFor(Module.class)
public class ApplicationSql2oModule extends AbstractModule {
    @Override
    protected void doConfigure() {
        bindContactRepository();
        bindSql2oBootstrap();
    }

    protected void bindContactRepository() {
        bind(ContactRepository.class)
            .to(Sql2oContactRepository.class)
            .asSingleton();
    }

    protected void bindSql2oBootstrap() {
        bind(Sql2oBootstrap.class)
            .to(ContactsSql2oBootstrap.class)
            .asSingleton();
    }
}