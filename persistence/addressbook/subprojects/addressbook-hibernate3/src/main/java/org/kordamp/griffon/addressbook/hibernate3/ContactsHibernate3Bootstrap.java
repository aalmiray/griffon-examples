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
package org.kordamp.griffon.addressbook.hibernate3;

import griffon.plugins.hibernate3.Hibernate3Bootstrap;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.kordamp.griffon.addressbook.ContactsDataProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

@Named("contacts")
public class ContactsHibernate3Bootstrap implements Hibernate3Bootstrap {
    @Inject
    private ContactsDataProvider contactsDataProvider;

    @Override
    public void init(@Nonnull String sessionFactoryName, @Nonnull Session session) {
        Transaction tx = session.beginTransaction();
        contactsDataProvider.getContacts().forEach(contact -> {
            ContactEntity entity = ContactEntity.fromContact(contact);
            session.save(entity);
        });
        tx.commit();
    }

    @Override
    public void destroy(@Nonnull String sessionFactoryName, @Nonnull Session session) {

    }
}