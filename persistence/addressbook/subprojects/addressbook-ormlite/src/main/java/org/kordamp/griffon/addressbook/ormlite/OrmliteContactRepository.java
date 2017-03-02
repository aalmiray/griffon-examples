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
package org.kordamp.griffon.addressbook.ormlite;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import griffon.plugins.ormlite.ConnectionSourceCallback;
import griffon.plugins.ormlite.ConnectionSourceHandler;
import org.kordamp.griffon.addressbook.Contact;
import org.kordamp.griffon.addressbook.ContactRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class OrmliteContactRepository implements ContactRepository {
    @Inject
    private ConnectionSourceHandler connectionSourceHandler;

    @Nullable
    @Override
    public Contact findById(@Nonnull final Long id) {
        return connectionSourceHandler.withConnectionSource((databaseName, connectionSource) -> {
            Dao<ContactEntity, Integer> contactDao = DaoManager.createDao(connectionSource, ContactEntity.class);
            ContactEntity entity = contactDao.queryForId(id.intValue());
            return entity == null ? null : ContactEntity.toContact(entity);
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
        connectionSourceHandler.withConnectionSource((databaseName, connectionSource) -> {
            Dao<ContactEntity, Integer> contactDao = DaoManager.createDao(connectionSource, ContactEntity.class);
            ContactEntity contactEntity = ContactEntity.fromContact(contact);
            contactDao.create(contactEntity);
            contact.setId(contactEntity.getId());
            return contact;
        });
    }

    private void update(@Nonnull final Contact contact) {
        connectionSourceHandler.withConnectionSource((databaseName, connectionSource) -> {
            Dao<ContactEntity, Integer> contactDao = DaoManager.createDao(connectionSource, ContactEntity.class);
            ContactEntity contactEntity = contactDao.queryForId(contact.getId().intValue());
            contactEntity.refreshWith(contact);
            contactDao.update(contactEntity);
            return contact;
        });
    }

    @Override
    public void delete(@Nonnull final Contact contact) {
        connectionSourceHandler.withConnectionSource((databaseName, connectionSource) -> {
            Dao<ContactEntity, Integer> contactDao = DaoManager.createDao(connectionSource, ContactEntity.class);
            contactDao.deleteById(contact.getId().intValue());
            return contact;
        });
    }

    @Override
    @Nonnull
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public Collection<Contact> findAll() {
        return connectionSourceHandler.withConnectionSource((ConnectionSourceCallback<Collection<Contact>>) (databaseName, connectionSource) -> {
            Dao<ContactEntity, Integer> contactDao = DaoManager.createDao(connectionSource, ContactEntity.class);
            return contactDao.queryForAll().stream()
                .map(ContactEntity::toContact)
                .collect(toList());
        });
    }

    @Override
    public void clear() {
        connectionSourceHandler.withConnectionSource((ConnectionSourceCallback<Void>) (databaseName, connectionSource) -> {
            Dao<ContactEntity, Integer> contactDao = DaoManager.createDao(connectionSource, ContactEntity.class);
            for (ContactEntity entity : contactDao.queryForAll()) {
                contactDao.deleteById(entity.getId().intValue());
            }
            return null;
        });
    }
}
