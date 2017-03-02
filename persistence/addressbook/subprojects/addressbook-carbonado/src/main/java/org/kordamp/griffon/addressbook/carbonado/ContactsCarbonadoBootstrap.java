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
package org.kordamp.griffon.addressbook.carbonado;

import com.amazon.carbonado.PersistException;
import com.amazon.carbonado.Repository;
import com.amazon.carbonado.RepositoryException;
import com.amazon.carbonado.Storage;
import griffon.plugins.carbonado.CarbonadoBootstrap;
import org.kordamp.griffon.addressbook.ContactsDataProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

@Named("contacts")
public class ContactsCarbonadoBootstrap implements CarbonadoBootstrap {
    @Inject
    private ContactsDataProvider contactsDataProvider;

    @Override
    public void init(@Nonnull String datasourceName, @Nonnull Repository repository) {
        try {
            Storage<Contacts> storage = repository.storageFor(Contacts.class);
            contactsDataProvider.getContacts().forEach(contact -> {
                Contacts entity = storage.prepare();
                entity = Contacts.fromContact(entity, contact);
                try {
                    entity.insert();
                } catch (PersistException e) {
                    throw new IllegalStateException(e);
                }
            });
        } catch (RepositoryException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void destroy(@Nonnull String datasourceName, @Nonnull Repository repository) {

    }
}