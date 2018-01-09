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
package org.kordamp.griffon.addressbook;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;
import javax.inject.Inject;

@ArtifactProviderFor(GriffonController.class)
public class AddresbookController extends AbstractGriffonController {
    private AddresbookModel model;

    @Inject
    private ContactRepository contactRepository;

    @MVCMember
    public void setModel(@Nonnull AddresbookModel model) {
        this.model = model;
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void newAction() {
        model.setSelectedIndex(-1);
    }

    public void saveAction() {
        // push changes to domain object
        model.getObservableContact().updateContact();
        Contact contact = model.getObservableContact().getContact();
        boolean isNew = contact.isNotPersisted();
        // save to db
        contactRepository.save(contact);
        // if is a new contact, add it to the list
        if (isNew) {
            ObservableContact observableContact = new ObservableContact(contact);
            model.getContacts().add(observableContact);
            model.setSelectedIndex(model.getContacts().indexOf(observableContact));
        }
    }

    @Threading(Threading.Policy.SKIP)
    public void deleteAction() {
        ObservableContact observableContact = model.getContacts().remove(model.getSelectedIndex());
        model.setSelectedIndex(-1);
        runOutsideUI(() -> contactRepository.delete(observableContact.getContact().getId()));
    }

    public void printAction() {
        contactRepository.findAll().forEach(System.out::println);
    }
}