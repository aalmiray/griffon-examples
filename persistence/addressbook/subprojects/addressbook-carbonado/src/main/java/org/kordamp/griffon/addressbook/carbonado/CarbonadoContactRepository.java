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
package org.kordamp.griffon.addressbook.carbonado;

import com.amazon.carbonado.Cursor;
import com.amazon.carbonado.FetchNoneException;
import com.amazon.carbonado.Repository;
import com.amazon.carbonado.RepositoryException;
import com.amazon.carbonado.Storage;
import griffon.plugins.carbonado.CarbonadoHandler;
import griffon.plugins.carbonado.RepositoryCallback;
import org.kordamp.griffon.addressbook.Contact;
import org.kordamp.griffon.addressbook.ContactRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CarbonadoContactRepository implements ContactRepository {
    @Inject
    private CarbonadoHandler carbonadoHandler;

    @Nullable
    @Override
    public Contact findById(@Nonnull final Long id) {
        return carbonadoHandler.withCarbonado((datasourceName, repository) -> {
            try {
                Storage<Contacts> storage = repository.storageFor(Contacts.class);
                Contacts entity = storage.prepare();
                entity.setId(id);
                entity.load();
                return Contacts.toContact(entity);
            } catch (FetchNoneException e) {
                return null;
            } catch (RepositoryException e) {
                throw new IllegalStateException(e);
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
        carbonadoHandler.withCarbonado((datasourceName, repository) -> {
            try {
                Storage<Contacts> storage = repository.storageFor(Contacts.class);
                Contacts entity = Contacts.fromContact(storage.prepare(), contact);
                entity.insert();
                contact.setId(entity.getId());
            } catch (RepositoryException e) {
                throw new IllegalStateException(e);
            }
            return contact;
        });
    }

    private void update(@Nonnull final Contact contact) {
        carbonadoHandler.withCarbonado((datasourceName, repository) -> {
            try {
                Storage<Contacts> storage = repository.storageFor(Contacts.class);
                Contacts entity = storage.prepare();
                entity.setId(contact.getId());
                entity.load();
                Contacts.fromContact(entity, contact).update();
            } catch (RepositoryException e) {
                throw new IllegalStateException(e);
            }
            return contact;
        });
    }

    @Override
    public void delete(@Nonnull final Contact contact) {
        carbonadoHandler.withCarbonado((datasourceName, repository) -> {
            try {
                Storage<Contacts> storage = repository.storageFor(Contacts.class);
                Contacts.fromContact(storage.prepare(), contact).delete();
            } catch (RepositoryException e) {
                throw new IllegalStateException(e);
            }
            return contact;
        });
    }

    @Override
    @Nonnull
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public Collection<Contact> findAll() {
        return carbonadoHandler.withCarbonado(new RepositoryCallback<Collection<Contact>>() {
            @Nullable
            @Override
            public Collection<Contact> handle(@Nonnull String repositoryName, @Nonnull Repository repository) {
                List<Contact> list = new ArrayList<>();
                try {
                    Storage<Contacts> storage = repository.storageFor(Contacts.class);
                    Cursor<Contacts> cursor = storage.query().fetch();
                    while (cursor.hasNext()) {
                        list.add(Contacts.toContact(cursor.next()));
                    }
                } catch (RepositoryException e) {
                    throw new IllegalStateException(e);
                }
                return list;
            }
        });
    }

    @Override
    public void clear() {
        final Collection<Contact> contacts = findAll();
        carbonadoHandler.withCarbonado(new RepositoryCallback<Void>() {
            @Nullable
            @Override
            public Void handle(@Nonnull String repositoryName, @Nonnull Repository repository) {
                try {
                    Storage<Contacts> storage = repository.storageFor(Contacts.class);
                    for (Contact contact : contacts) {
                        Contacts.fromContact(storage.prepare(), contact).delete();
                    }
                } catch (RepositoryException e) {
                    throw new IllegalStateException(e);
                }
                return null;
            }
        });
    }
}
