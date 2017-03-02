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
package org.kordamp.griffon.addressbook.jdbi;

import griffon.plugins.jdbi.JdbiBootstrap;
import org.kordamp.griffon.addressbook.ContactsDataProvider;
import org.skife.jdbi.v2.DBI;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

@Named("contacts")
public class ContactsJdbiBootstrap implements JdbiBootstrap {
    @Inject
    private ContactsDataProvider contactsDataProvider;

    @Override
    public void init(@Nonnull String datasourceName, @Nonnull DBI dbi) {
        ContactDAO dao = dbi.open(ContactDAO.class);
        contactsDataProvider.getContacts().forEach(contact -> {
            contact.setId(dao.insert(
                contact.getName(),
                contact.getLastname(),
                contact.getAddress(),
                contact.getCompany(),
                contact.getEmail()
            ));
        });
    }

    @Override
    public void destroy(@Nonnull String datasourceName, @Nonnull DBI dbi) {

    }
}