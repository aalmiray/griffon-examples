/*
 * Copyright 20162017 Andres Almiray
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
package org.kordamp.griffon.addressbook.ohmdb;

import com.ohmdb.api.Db;
import com.ohmdb.api.Table;
import com.ohmdb.exception.InvalidIdException;
import griffon.plugins.ohmdb.DbCallback;
import griffon.plugins.ohmdb.DbHandler;
import org.kordamp.griffon.addressbook.Contact;
import org.kordamp.griffon.addressbook.ContactRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;

import static java.util.Arrays.asList;

public class OhmdbContactRepository implements ContactRepository {
    @Inject
    private DbHandler dbHandler;

    @Nullable
    @Override
    public Contact findById(@Nonnull final Long id) {
        return dbHandler.withOhmdb(new DbCallback<Contact>() {
            @Nullable
            @Override
            public Contact handle(@Nonnull String dataSourceName, @Nonnull Db db) {
                Table<Contact> table = db.table(Contact.class);
                try {
                    return table.get(id);
                } catch (InvalidIdException e) {
                    return null;
                }
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
        dbHandler.withOhmdb(new DbCallback<Contact>() {
            @Nullable
            @Override
            public Contact handle(@Nonnull String dataSourceName, @Nonnull Db db) {
                Table<Contact> table = db.table(Contact.class);
                contact.setId(table.insert(contact));
                return contact;
            }
        });
    }

    private void update(@Nonnull final Contact contact) {
        dbHandler.withOhmdb(new DbCallback<Contact>() {
            @Nullable
            @Override
            public Contact handle(@Nonnull String dataSourceName, @Nonnull Db db) {
                db.table(Contact.class).update(contact);
                return contact;
            }
        });
    }

    @Override
    public void delete(@Nonnull final Contact contact) {
        dbHandler.withOhmdb(new DbCallback<Contact>() {
            @Nullable
            @Override
            public Contact handle(@Nonnull String dataSourceName, @Nonnull Db db) {
                db.table(Contact.class).delete(contact.getId());
                return contact;
            }
        });
    }

    @Override
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public Collection<Contact> findAll() {
        return dbHandler.withOhmdb(new DbCallback<Collection<Contact>>() {
            @Nullable
            @Override
            public Collection<Contact> handle(@Nonnull String dataSourceName, @Nonnull Db db) {
                Table<Contact> table = db.table(Contact.class);
                return asList(table.getAll(table.ids()));
            }
        });
    }

    @Override
    public void clear() {
        dbHandler.withOhmdb(new DbCallback<Void>() {
            @Nullable
            @Override
            public Void handle(@Nonnull String dataSourceName, @Nonnull Db db) {
                Table<Contact> table = db.table(Contact.class);
                for (long id : table.ids()) {
                    table.delete(id);
                }
                return null;
            }
        });
    }
}
