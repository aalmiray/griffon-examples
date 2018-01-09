/*
 * Copyright 2016-2018 Andres Almiray
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
package org.kordamp.griffon.addressbook.jooq;

import griffon.plugins.datasource.ConnectionCallback;
import griffon.plugins.datasource.DataSourceHandler;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.kordamp.griffon.addressbook.Contact;
import org.kordamp.griffon.addressbook.ContactRepository;
import org.kordamp.griffon.addressbook.jooq.schema.tables.records.ContactsRecord;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;

import static java.util.stream.Collectors.toList;
import static org.kordamp.griffon.addressbook.jooq.schema.Tables.CONTACTS;

public class JooqContactRepository implements ContactRepository {
    @Inject
    private DataSourceHandler dataSourceHandler;

    @Nullable
    @Override
    public Contact findById(@Nonnull final Long id) {
        return dataSourceHandler.withConnection((dataSourceName, dataSource, connection) -> {
            DSLContext context = DSL.using(connection, SQLDialect.H2);
            ContactsRecord record = (ContactsRecord) context.select().from(CONTACTS)
                .where(CONTACTS.ID.equal(id.intValue()))
                .fetchOne();
            return record == null ? null : asContact(record);
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
        dataSourceHandler.withConnection((dataSourceName, dataSource, connection) -> {
            DSLContext context = DSL.using(connection, SQLDialect.H2);
            contact.setId(context.insertInto(CONTACTS)
                .set(CONTACTS.NAME, contact.getName())
                .set(CONTACTS.LASTNAME, contact.getLastname())
                .set(CONTACTS.ADDRESS, contact.getAddress())
                .set(CONTACTS.COMPANY, contact.getCompany())
                .set(CONTACTS.EMAIL, contact.getEmail())
                .returning(CONTACTS.ID)
                .fetchOne()
                .getValue(CONTACTS.ID).longValue());
            return contact;
        });
    }

    private void update(@Nonnull final Contact contact) {
        dataSourceHandler.withConnection((dataSourceName, dataSource, connection) -> {
            DSLContext context = DSL.using(connection, SQLDialect.H2);
            context.update(CONTACTS)
                .set(CONTACTS.NAME, contact.getName())
                .set(CONTACTS.LASTNAME, contact.getLastname())
                .set(CONTACTS.ADDRESS, contact.getAddress())
                .set(CONTACTS.COMPANY, contact.getCompany())
                .set(CONTACTS.EMAIL, contact.getEmail())
                .where(CONTACTS.ID.equal(contact.getId().intValue()))
                .execute();
            return contact;
        });
    }

    @Override
    public void delete(@Nonnull final Contact contact) {
        dataSourceHandler.withConnection((dataSourceName, dataSource, connection) -> {
            DSLContext context = DSL.using(connection, SQLDialect.H2);
            context.delete(CONTACTS)
                .where(CONTACTS.ID.equal(contact.getId().intValue()))
                .execute();
            return contact;
        });
    }

    @Override
    @Nonnull
    @SuppressWarnings("ConstantConditions")
    public Collection<Contact> findAll() {
        return dataSourceHandler.withConnection((dataSourceName, dataSource, connection) -> {
            DSLContext context = DSL.using(connection, SQLDialect.H2);
            return context.select().from(CONTACTS).fetch().stream()
                .map(record -> (ContactsRecord) record)
                .map(JooqContactRepository::asContact)
                .collect(toList());
        });
    }

    @Override
    public void clear() {
        dataSourceHandler.withConnection((ConnectionCallback<Void>) (dataSourceName, dataSource, connection) -> {
            DSLContext context = DSL.using(connection, SQLDialect.H2);
            context.deleteFrom(CONTACTS).execute();
            return null;
        });
    }

    @Nonnull
    private static Contact asContact(@Nonnull final ContactsRecord record) {
        Contact contact = Contact.builder()
            .name(record.getName())
            .lastname(record.getLastname())
            .address(record.getAddress())
            .company(record.getCompany())
            .email(record.getEmail())
            .build();
        contact.setId(record.getId().longValue());
        return contact;
    }
}
