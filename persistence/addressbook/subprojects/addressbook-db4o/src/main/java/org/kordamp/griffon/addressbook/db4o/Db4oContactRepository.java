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
package org.kordamp.griffon.addressbook.db4o;

import com.db4o.ObjectContainer;
import griffon.plugins.db4o.ObjectContainerCallback;
import griffon.plugins.db4o.ObjectContainerHandler;
import org.kordamp.griffon.addressbook.Contact;
import org.kordamp.griffon.addressbook.ContactRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class Db4oContactRepository implements ContactRepository {
    @Inject
    private ObjectContainerHandler objectContainerHandler;
    @Inject
    private IdGenerator idGenerator;

    @Nullable
    @Override
    public Contact findById(@Nonnull final Long id) {
        return objectContainerHandler.withDb4o(new ObjectContainerCallback<Contact>() {
            @Nullable
            @Override
            public Contact handle(@Nonnull String dataSourceName, @Nonnull ObjectContainer objectContainer) {
                return objectContainer.query(Contact.class).stream()
                    .filter(contact -> contact.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            }
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
        objectContainerHandler.withDb4o(new ObjectContainerCallback<Contact>() {
            @Nullable
            @Override
            public Contact handle(@Nonnull String dataSourceName, @Nonnull ObjectContainer objectContainer) {
                Long id = idGenerator.nextId();
                contact.setId(id);
                objectContainer.store(contact);
                return contact;
            }
        });
    }

    private void update(@Nonnull final Contact contact) {
        objectContainerHandler.withDb4o(new ObjectContainerCallback<Contact>() {
            @Nullable
            @Override
            public Contact handle(@Nonnull String dataSourceName, @Nonnull ObjectContainer objectContainer) {
                objectContainer.store(contact);
                return contact;
            }
        });
    }

    @Override
    public void delete(@Nonnull final Contact contact) {
        objectContainerHandler.withDb4o(new ObjectContainerCallback<Contact>() {
            @Nullable
            @Override
            public Contact handle(@Nonnull String dataSourceName, @Nonnull ObjectContainer objectContainer) {
                objectContainer.delete(contact);
                return contact;
            }
        });
    }

    @Override
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public Collection<Contact> findAll() {
        return objectContainerHandler.withDb4o(new ObjectContainerCallback<Collection<Contact>>() {
            @Nullable
            @Override
            public Collection<Contact> handle(@Nonnull String dataSourceName, @Nonnull ObjectContainer objectContainer) {
                return objectContainer.query(Contact.class).stream().collect(toList());
            }
        });
    }

    @Override
    public void clear() {
        objectContainerHandler.withDb4o(new ObjectContainerCallback<Void>() {
            @Nullable
            @Override
            public Void handle(@Nonnull String dataSourceName, @Nonnull ObjectContainer objectContainer) {
                objectContainer.query(Contact.class).stream().forEach(objectContainer::delete);
                return null;
            }
        });
    }
}
