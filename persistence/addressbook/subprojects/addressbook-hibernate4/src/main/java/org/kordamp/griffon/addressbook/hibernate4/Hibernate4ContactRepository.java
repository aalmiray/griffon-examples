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
package org.kordamp.griffon.addressbook.hibernate4;

import griffon.plugins.hibernate4.Hibernate4Handler;
import org.kordamp.griffon.addressbook.Contact;
import org.kordamp.griffon.addressbook.ContactRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class Hibernate4ContactRepository implements ContactRepository {
    @Inject
    private Hibernate4Handler hibernate4Handler;

    @Nullable
    @Override
    public Contact findById(@Nonnull final Long id) {
        return hibernate4Handler.withHbm4Session((sessionFactoryName, session) -> {
            ContactEntity entity = (ContactEntity) session.get(ContactEntity.class, id);
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
        hibernate4Handler.withHbm4Session((sessionFactoryName, session) -> {
            ContactEntity entity = ContactEntity.fromContact(contact);
            session.save(entity);
            contact.setId(entity.getId());
            return contact;
        });
    }

    private void update(@Nonnull final Contact contact) {
        hibernate4Handler.withHbm4Session((sessionFactoryName, session) -> {
            ContactEntity entity = (ContactEntity) session.get(ContactEntity.class, contact.getId());
            session.update(entity.refreshWith(contact));
            return contact;
        });
    }

    @Override
    public void delete(@Nonnull final Contact contact) {
        hibernate4Handler.withHbm4Session((sessionFactoryName, session) -> {
            ContactEntity entity = (ContactEntity) session.get(ContactEntity.class, contact.getId());
            session.delete(entity);
            return contact;
        });
    }

    @Override
    @Nonnull
    @SuppressWarnings({"ConstantConditions", "unchecked", "JpaQlInspection"})
    public Collection<Contact> findAll() {
        return hibernate4Handler.withHbm4Session((sessionFactoryName, session) -> (Collection<Contact>) session.createQuery("from ContactEntity").list().stream()
            .map(record -> ContactEntity.toContact((ContactEntity) record))
            .collect(toList()));
    }

    @Override
    public void clear() {
        hibernate4Handler.withHbm4Session((sessionFactoryName, session) -> {
            session.createQuery("from ContactEntity").list().forEach(session::delete);
            return null;
        });
    }
}
