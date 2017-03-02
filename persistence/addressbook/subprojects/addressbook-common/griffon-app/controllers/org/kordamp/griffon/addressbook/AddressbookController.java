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
package org.kordamp.griffon.addressbook;

import griffon.core.GriffonApplication;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;
import javax.inject.Inject;

@ArtifactProviderFor(GriffonController.class)
public class AddressbookController extends AbstractGriffonController {
    private AddressbookModel model;

    @Inject
    private ContactRepository contactRepository;

    @MVCMember
    public void setModel(@Nonnull AddressbookModel model) {
        this.model = model;
    }

    public void onStartupEnd(GriffonApplication application) {
        // load contacts into model
        contactRepository.findAll().stream()
            .map(ObservableContact::new)
            .forEach(model.getContacts()::add);
    }

    public void newAction() {
        model.setSelectedIndex(-1);
    }

    public void saveAction() {
        // push changes to domain object
        model.getObservableContact().updateContact();
        final Contact contact = model.getObservableContact().getContact();
        boolean isNew = contact.isNotPersisted();
        // persist domain object
        contactRepository.save(contact);
        // if it's a new contact, add it to the list
        if (isNew) {
            ObservableContact observableContact = new ObservableContact(contact);
            model.getContacts().add(observableContact);
            model.setSelectedIndex(model.getContacts().indexOf(observableContact));
        } else {
            // update existing contact in list
            model.getContacts().stream()
                .filter(c -> c.getContact().getId().equals(contact.getId()))
                .findFirst()
                .ifPresent(c -> c.updateWith(model.getObservableContact()));
        }
    }

    public void deleteAction() {
        ObservableContact observableContact = model.getContacts().remove(model.getSelectedIndex());
        model.setSelectedIndex(-1);
        contactRepository.delete(observableContact.getContact());
    }

    public void printAction() {
        contactRepository.findAll().forEach(System.out::println);
    }
}