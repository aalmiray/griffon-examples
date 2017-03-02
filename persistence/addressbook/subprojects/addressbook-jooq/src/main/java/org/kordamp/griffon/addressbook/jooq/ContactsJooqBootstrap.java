/*
 * Copyright 2016-2017 Andres Almiray
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
package org.kordamp.griffon.addressbook.jooq;

import griffon.core.GriffonApplication;
import griffon.core.event.EventHandler;
import griffon.plugins.datasource.DataSourceHandler;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.kordamp.griffon.addressbook.ContactsDataProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

import static org.kordamp.griffon.addressbook.jooq.schema.Tables.CONTACTS;

@Named("contacts")
public class ContactsJooqBootstrap implements EventHandler {
    @Inject
    private DataSourceHandler dataSourceHandler;

    @Inject
    private ContactsDataProvider contactsDataProvider;

    public void onStartupStart(@Nonnull GriffonApplication application) {
        dataSourceHandler.withConnection((dataSourceName, dataSource, connection) -> {
            final DSLContext create = DSL.using(connection, SQLDialect.H2);
            contactsDataProvider.getContacts().forEach(contact -> create.insertInto(CONTACTS)
                .set(CONTACTS.NAME, contact.getName())
                .set(CONTACTS.LASTNAME, contact.getLastname())
                .set(CONTACTS.COMPANY, contact.getCompany())
                .set(CONTACTS.ADDRESS, contact.getAddress())
                .set(CONTACTS.EMAIL, contact.getEmail())
                .execute());
            return null;
        });
    }
}