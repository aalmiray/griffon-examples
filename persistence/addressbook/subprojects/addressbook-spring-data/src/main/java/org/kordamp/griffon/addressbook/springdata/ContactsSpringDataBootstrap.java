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
package org.kordamp.griffon.addressbook.springdata;

import griffon.core.GriffonApplication;
import griffon.core.event.EventHandler;
import org.kordamp.griffon.addressbook.ContactsDataProvider;
import org.kordamp.griffon.addressbook.springdata.beans.ContactEntity;
import org.kordamp.griffon.addressbook.springdata.beans.ContactEntityJpaRepository;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

@Named("contacts")
public class ContactsSpringDataBootstrap implements EventHandler {
    @Inject
    private ContactEntityJpaRepository repository;

    @Inject
    private ContactsDataProvider contactsDataProvider;

    public void onStartupStart(@Nonnull GriffonApplication application) {
        contactsDataProvider.getContacts().forEach(contact -> repository.save(ContactEntity.fromContact(contact)));
        repository.flush();
    }
}