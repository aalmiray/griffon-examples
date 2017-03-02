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
package org.kordamp.griffon.addressbook.ormlite;

import com.j256.ormlite.table.TableUtils;
import griffon.inject.DependsOn;
import griffon.inject.Evicts;
import griffon.plugins.ormlite.ConnectionSourceCallback;
import griffon.plugins.ormlite.ConnectionSourceHandler;
import org.junit.Before;
import org.kordamp.griffon.addressbook.AbstractContactRepositoryTest;

import javax.inject.Inject;
import javax.inject.Named;

public class OrmliteContactRepositoryTest extends AbstractContactRepositoryTest {
    @Inject private ConnectionSourceHandler connectionSourceHandler;

    @Before
    public void setupDatabase() {
        connectionSourceHandler.withConnectionSource((ConnectionSourceCallback<Void>) (databaseName, connectionSource) -> {
            TableUtils.createTableIfNotExists(connectionSource, ContactEntity.class);
            return null;
        });
    }

    @DependsOn("application")
    @Evicts("application-ormlite")
    @Named("application-ormlite")
    public static class OrmliteTestModule extends ApplicationOrmliteModule {
        @Override
        protected void bindOrmliteBootstrap() {
            // empty
        }
    }
}
