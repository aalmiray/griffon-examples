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
package org.kordamp.griffon.addressbook.gsql;

import griffon.plugins.gsql.GsqlBootstrap;
import groovy.sql.DataSet;
import groovy.sql.Sql;
import org.kordamp.griffon.addressbook.ContactsDataProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Named("contacts")
public class ContactsGsqlBootstrap implements GsqlBootstrap {
    @Inject
    private ContactsDataProvider contactsDataProvider;

    @Override
    public void init(@Nonnull String datasourceName, @Nonnull Sql sql) {
        DataSet dataSet = sql.dataSet("contacts");
        contactsDataProvider.getContacts().forEach(contact -> {
            Map<String, Object> params = ContactMapper.toMap(contact);
            try {
                List<List<Object>> result = dataSet.executeInsert(params, "INSERT INTO contacts (name, lastname, address, company, email) VALUES (:name, :lastname, :address, :company, :email)");
                contact.setId(((Number) result.get(0).get(0)).longValue());
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    @Override
    public void destroy(@Nonnull String datasourceName, @Nonnull Sql sql) {

    }
}