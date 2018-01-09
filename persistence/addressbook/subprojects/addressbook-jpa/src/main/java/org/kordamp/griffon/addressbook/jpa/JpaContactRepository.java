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
package org.kordamp.griffon.addressbook.jpa;

import griffon.plugins.jpa.EntityManagerCallback;
import griffon.plugins.jpa.EntityManagerHandler;
import org.kordamp.griffon.addressbook.Contact;
import org.kordamp.griffon.addressbook.ContactRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Collection;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

public class JpaContactRepository implements ContactRepository {
    @Inject
    private EntityManagerHandler entityManagerHandler;

    @Nullable
    @Override
    public Contact findById(@Nonnull final Long id) {
        return entityManagerHandler.withEntityManager((persistenceUnitName, entityManager) -> withTransaction(entityManager, em -> {
            ContactEntity entity = entityManager.find(ContactEntity.class, id);
            return entity == null ? null : ContactEntity.toContact(entity);
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
        entityManagerHandler.withEntityManager((persistenceUnitName, entityManager) -> withTransaction(entityManager, em -> {
            ContactEntity entity = ContactEntity.fromContact(contact);
            entityManager.persist(entity);
            contact.setId(entity.getId());
            return contact;
        }));
    }

    private void update(@Nonnull final Contact contact) {
        entityManagerHandler.withEntityManager((persistenceUnitName, entityManager) -> withTransaction(entityManager, em -> {
            ContactEntity entity = entityManager.find(ContactEntity.class, contact.getId());
            entity.refreshWith(contact);
            entityManager.persist(entity);
            return contact;
        }));
    }

    @Override
    public void delete(@Nonnull final Contact contact) {
        entityManagerHandler.withEntityManager((persistenceUnitName, entityManager) -> withTransaction(entityManager, em -> {
            ContactEntity entity = entityManager.find(ContactEntity.class, contact.getId());
            entityManager.remove(entity);
            return contact;
        }));
    }

    @Override
    @Nonnull
    @SuppressWarnings({"ConstantConditions", "unchecked"})
    public Collection<Contact> findAll() {
        return entityManagerHandler.withEntityManager((persistenceUnitName, entityManager) -> (Collection<Contact>) entityManager.createQuery("SELECT c FROM ContactEntity c").getResultList().stream()
            .map(record -> ContactEntity.toContact((ContactEntity) record))
            .collect(toList()));
    }

    @Override
    public void clear() {
        entityManagerHandler.withEntityManager((EntityManagerCallback<Void>) (persistenceUnitName, entityManager) -> withTransaction(entityManager, em -> {
            entityManager.createQuery("SELECT c FROM ContactEntity c").getResultList()
                .stream().peek(System.out::println)
                .forEach(entityManager::remove);
            return null;
        }));
    }

    @Nullable
    private static <T> T withTransaction(@Nonnull EntityManager entityManager, @Nonnull Function<EntityManager, T> function) {
        T result = null;
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            result = function.apply(entityManager);
            entityManager.flush();
            transaction.commit();
        } catch (RuntimeException e) {
            transaction.rollback();
            throw e;
        }

        return result;
    }
}
