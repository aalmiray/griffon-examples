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
package org.kordamp.griffon.addressbook.sql2o;

import griffon.plugins.sql2o.Sql2oCallback;
import griffon.plugins.sql2o.Sql2oHandler;
import org.kordamp.griffon.addressbook.Contact;
import org.kordamp.griffon.addressbook.ContactRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;

public class Sql2oContactRepository implements ContactRepository {
    @Inject
    private Sql2oHandler sql2oHandler;

    @Nullable
    @Override
    public Contact findById(@Nonnull final Long id) {
        return sql2oHandler.withSql2o((datasourceName, sql2o) -> sql2o.withConnection((connection, argument) -> {
            return connection.createQuery("SELECT * FROM contacts WHERE id = :id")
                .addParameter("id", id)
                .executeAndFetchFirst(Contact.class);
        }));
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
        sql2oHandler.withSql2o((datasourceName, sql2o) -> {
            sql2o.withConnection((connection, argument) -> {
                String sql = "INSERT INTO contacts(name, lastname, company, address, email) VALUES (:name, :lastname, :company, :address, :email)";

                Object key = connection.createQuery(sql)
                    .bind(contact)
                    .executeUpdate()
                    .getKey();
                contact.setId(((Number) key).longValue());
            });
            return contact;
        });
    }

    private void update(@Nonnull final Contact contact) {
        sql2oHandler.withSql2o((datasourceName, sql2o) -> {
            sql2o.withConnection((connection, argument) -> {
                String sql = "UPDATE contacts SET name = :name, " +
                    "lastname = :lastname, company = :company," +
                    "address = :address, email = :email " +
                    "WHERE id = :id";

                connection.createQuery(sql)
                    .bind(contact)
                    .executeUpdate();
            });
            return contact;
        });
    }

    @Override
    public void delete(@Nonnull final Contact contact) {
        sql2oHandler.withSql2o((datasourceName, sql2o) -> {
            sql2o.withConnection((connection, argument) -> {
                connection.createQuery("DELETE FROM contacts WHERE id = :id")
                    .addParameter("id", contact.getId())
                    .executeUpdate();
            });
            return contact;
        });
    }

    @Override
    @Nonnull
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public Collection<Contact> findAll() {
        return sql2oHandler.withSql2o((datasourceName, sql2o) -> (Collection<Contact>) sql2o.withConnection((connection, argument) -> {
            return connection.createQuery("SELECT * FROM contacts").executeAndFetch(Contact.class);
        }));
    }

    @Override
    public void clear() {
        sql2oHandler.withSql2o((Sql2oCallback<Void>) (datasourceName, sql2o) -> {
            sql2o.withConnection((connection, argument) -> {
                connection.createQuery("DELETE FROM contacts").executeUpdate();
            });
            return null;
        });
    }
}
