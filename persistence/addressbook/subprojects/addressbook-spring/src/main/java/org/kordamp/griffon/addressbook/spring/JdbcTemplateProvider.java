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
package org.kordamp.griffon.addressbook.spring;

import griffon.core.storage.ObjectFactory;
import griffon.plugins.datasource.DataSourceFactory;
import griffon.plugins.datasource.DataSourceStorage;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;

public class JdbcTemplateProvider implements Provider<JdbcTemplate> {
    @Inject private DataSourceFactory dataSourceFactory;
    @Inject private DataSourceStorage dataSourceStorage;

    @Override
    public JdbcTemplate get() {
        return new JdbcTemplate(getDataSource(ObjectFactory.KEY_DEFAULT));
    }

    @Nonnull
    private DataSource getDataSource(@Nonnull String dataSourceName) {
        DataSource dataSource = dataSourceStorage.get(dataSourceName);
        if (dataSource == null) {
            dataSource = dataSourceFactory.create(dataSourceName);
            dataSourceStorage.set(dataSourceName, dataSource);
        }
        return dataSource;
    }
}
