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
package org.kordamp.griffon.addressbook.jdbi;

import griffon.plugins.jdbi.JdbiCallback;
import griffon.plugins.jdbi.JdbiHandler;
import org.kordamp.griffon.addressbook.Contact;
import org.kordamp.griffon.addressbook.ContactRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;

public class JdbiContactRepository implements ContactRepository {
    @Inject
    private JdbiHandler jdbiHandler;

    @Nullable
    @Override
    public Contact findById(@Nonnull final Long id) {
        return jdbiHandler.withJdbi((datasourceName, dbi) -> {
            ContactDAO dao = dbi.open(ContactDAO.class);
            return dao.findById(id);
        });
    }

    @Override
    public void save(@Nonnull final Contact contact) {
        if (contact.isNotPersisted()) {
            insert(contact);
        } else {
            update(contact);
        }
    }

    private void insert(@Nonnull final Contact contact) {
        jdbiHandler.withJdbi((datasourceName, dbi) -> {
            contact.setId(dbi.open(ContactDAO.class).insert(
                contact.getName(),
                contact.getLastname(),
                contact.getAddress(),
                contact.getCompany(),
                contact.getEmail()
            ));
            return contact;
        });
    }

    private void update(@Nonnull final Contact contact) {
        jdbiHandler.withJdbi((datasourceName, dbi) -> {
            dbi.open(ContactDAO.class).update(
                contact.getId(),
                contact.getName(),
                contact.getLastname(),
                contact.getAddress(),
                contact.getCompany(),
                contact.getEmail()
            );
            return contact;
        });
    }

    @Override
    public void delete(@Nonnull final Contact contact) {
        jdbiHandler.withJdbi((datasourceName, dbi) -> {
            dbi.open(ContactDAO.class).delete(contact.getId());
            return contact;
        });
    }

    @Override
    @Nonnull
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public Collection<Contact> findAll() {
        return jdbiHandler.withJdbi((JdbiCallback<Collection<Contact>>) (datasourceName, dbi) -> dbi.open(ContactDAO.class).list());
    }

    @Override
    public void clear() {
        jdbiHandler.withJdbi((JdbiCallback<Void>) (datasourceName, dbi) -> {
            dbi.open(ContactDAO.class).clear();
            return null;
        });
    }
}
