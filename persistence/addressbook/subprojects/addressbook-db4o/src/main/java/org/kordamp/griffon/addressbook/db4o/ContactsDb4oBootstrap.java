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
package org.kordamp.griffon.addressbook.db4o;

import com.db4o.ObjectContainer;
import griffon.plugins.db4o.Db4oBootstrap;
import org.kordamp.griffon.addressbook.ContactsDataProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

@Named("contacts")
public class ContactsDb4oBootstrap implements Db4oBootstrap {
    @Inject
    private ContactsDataProvider contactsDataProvider;
    @Inject
    private IdGenerator idGenerator;

    @Override
    public void init(@Nonnull String dataSourceName, @Nonnull final ObjectContainer objectContainer) {
        contactsDataProvider.getContacts().forEach(contact -> {
            Long id = idGenerator.nextId();
            contact.setId(id);
            objectContainer.store(contact);
        });
    }

    @Override
    public void destroy(@Nonnull String dataSourceName, @Nonnull ObjectContainer objectContainer) {

    }
}