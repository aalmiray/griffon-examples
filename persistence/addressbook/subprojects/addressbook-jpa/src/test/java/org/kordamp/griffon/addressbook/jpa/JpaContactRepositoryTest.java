/*
 * Copyright 20162017 Andres Almiray
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

import griffon.inject.DependsOn;
import griffon.inject.Evicts;
import org.kordamp.griffon.addressbook.AbstractContactRepositoryTest;

import javax.inject.Named;

public class JpaContactRepositoryTest extends AbstractContactRepositoryTest {
    @DependsOn("application")
    @Evicts("application-jpa")
    @Named("application-jpa")
    public static class JpaTestModule extends ApplicationJpaModule {
        @Override
        protected void bindJpaBootstrap() {
            // empty
        }
    }
}
