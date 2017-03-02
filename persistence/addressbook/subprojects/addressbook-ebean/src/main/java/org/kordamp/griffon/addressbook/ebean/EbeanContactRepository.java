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
package org.kordamp.griffon.addressbook.ebean;

import griffon.plugins.ebean.EbeanServerCallback;
import griffon.plugins.ebean.EbeanServerHandler;
import org.kordamp.griffon.addressbook.Contact;
import org.kordamp.griffon.addressbook.ContactRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class EbeanContactRepository implements ContactRepository {
    @Inject
    private EbeanServerHandler ebeanServerHandler;

    @Nullable
    @Override
    public Contact findById(@Nonnull final Long id) {
        return ebeanServerHandler.withEbean((ebeanServerName, ebeanServer) -> {
            ContactEntity entity = ebeanServer.find(ContactEntity.class, id);
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
        ebeanServerHandler.withEbean((ebeanServerName, ebeanServer) -> {
            ContactEntity contactEntity = ContactEntity.fromContact(contact);
            ebeanServer.save(contactEntity);
            contact.setId(contactEntity.getId());
            return contact;
        });
    }

    private void update(@Nonnull final Contact contact) {
        ebeanServerHandler.withEbean((ebeanServerName, ebeanServer) -> {
            ContactEntity contactEntity = ebeanServer.find(ContactEntity.class, contact.getId());
            contactEntity.refreshWith(contact);
            ebeanServer.update(contactEntity);
            return contact;
        });
    }

    @Override
    public void delete(@Nonnull final Contact contact) {
        ebeanServerHandler.withEbean((ebeanServerName, ebeanServer) -> {
            ContactEntity contactEntity = ebeanServer.find(ContactEntity.class, contact.getId());
            ebeanServer.delete(contactEntity);
            return contact;
        });
    }

    @Override
    @Nonnull
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public Collection<Contact> findAll() {
        return ebeanServerHandler.withEbean((ebeanServerName, ebeanServer) -> ebeanServer.find(ContactEntity.class)
            .findList().stream()
            .map(ContactEntity::toContact)
            .collect(toList()));
    }

    @Override
    public void clear() {
        ebeanServerHandler.withEbean((EbeanServerCallback<Void>) (ebeanServerName, ebeanServer) -> {
            ebeanServer.find(ContactEntity.class)
                .findList().forEach(ebeanServer::delete);
            return null;
        });
    }
}
