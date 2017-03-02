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
package org.kordamp.griffon.addressbook.jpa;

import griffon.plugins.jpa.JpaBootstrap;
import org.kordamp.griffon.addressbook.ContactsDataProvider;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Named("contacts")
public class ContactsJpaBootstrap implements JpaBootstrap {
    @Inject
    private ContactsDataProvider contactsDataProvider;

    @Override
    public void init(@Nonnull String datasourceName, @Nonnull EntityManager entityManager) {
        entityManager.getTransaction().begin();
        contactsDataProvider.getContacts().forEach(contact -> entityManager.persist(ContactEntity.fromContact(contact)));
        entityManager.flush();
        entityManager.getTransaction().commit();
    }

    @Override
    public void destroy(@Nonnull String datasourceName, @Nonnull EntityManager em) {

    }
}