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
package org.kordamp.griffon.addressbook.hibernate5;

import griffon.inject.DependsOn;
import griffon.inject.Evicts;
import org.kordamp.griffon.addressbook.AbstractContactRepositoryTest;

import javax.inject.Named;

public class Hibernate5ContactRepositoryTest extends AbstractContactRepositoryTest {
    @DependsOn("application")
    @Evicts("application-hibernate5")
    @Named("application-hibernate5")
    public static class Hibernate5TestModule extends ApplicationHibernate5Module {
        @Override
        protected void bindHibernate5Bootstrap() {
            // empty
        }
    }
}
