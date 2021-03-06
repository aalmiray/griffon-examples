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

import griffon.plugins.sql2o.Sql2oBootstrap;
import org.kordamp.griffon.addressbook.ContactsDataProvider;
import org.sql2o.Sql2o;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

@Named("contacts")
public class ContactsSql2oBootstrap implements Sql2oBootstrap {
    @Inject
    private ContactsDataProvider contactsDataProvider;

    @Override
    public void init(@Nonnull String datasourceName, @Nonnull Sql2o sql2o) {
        sql2o.withConnection((connection, argument) -> {
            final String sql = "INSERT INTO contacts(name, lastname, company, address, email) VALUES (:name, :lastname, :company, :address, :email)";
            contactsDataProvider.getContacts().forEach(contact -> connection.createQuery(sql)
                .bind(contact)
                .executeUpdate());
        });
    }

    @Override
    public void destroy(@Nonnull String datasourceName, @Nonnull Sql2o sql2o) {

    }
}