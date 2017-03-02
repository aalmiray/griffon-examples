package org.kordamp.griffon.addressbook;

import griffon.core.artifact.ArtifactManager;
import griffon.core.injection.Module;
import griffon.core.test.GriffonUnitRule;
import griffon.core.test.TestFor;
import org.codehaus.griffon.runtime.core.injection.AbstractTestingModule;
import org.junit.Rule;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@TestFor(AddressbookController.class)
public class AddressbookControllerTest {
    static {
        // force initialization JavaFX Toolkit
        new javafx.embed.swing.JFXPanel();
    }

    private AddressbookController controller;

    @Rule
    public final GriffonUnitRule griffon = new GriffonUnitRule();

    @Inject private ArtifactManager artifactManager;
    @Inject private ContactsDataProvider contactsDataProvider;
    @Inject private ContactRepository contactRepository;

    @Test
    public void delete_a_contact() {
        // given:
        AddressbookModel model = artifactManager.newInstance(AddressbookModel.class);
        contactsDataProvider.getContacts().forEach(c -> model.getContacts().add(new ObservableContact(c)));
        controller.setModel(model);

        // expectations:
        final AtomicBoolean invoked = new AtomicBoolean();
        Contact contact = contactsDataProvider.getContacts().get(0);
        doAnswer(invocation -> invoked.compareAndSet(false, true)).when(contactRepository).delete(contact);

        // when:
        model.setSelectedIndex(0);
        controller.deleteAction();

        // then:
        assertThat(model.getSelectedIndex(), equalTo(-1));
        assertThat(model.getContacts().size(), equalTo(1));
        assertTrue(invoked.get());
        verify(contactRepository, only()).delete(contact);
    }

    @Test
    public void save_new_contact() {
        // given:
        AddressbookModel model = artifactManager.newInstance(AddressbookModel.class);
        controller.setModel(model);
        ObservableContact observableContact = model.getObservableContact();
        observableContact.setName("name");
        observableContact.setLastname("lastname");
        observableContact.setAddress("address");
        observableContact.setCompany("company");
        observableContact.setEmail("email");

        // expectations:
        final AtomicBoolean invoked = new AtomicBoolean();
        Contact contact = observableContact.getContact();
        doAnswer(invocation -> invoked.compareAndSet(false, true)).when(contactRepository).save(contact);

        // when:
        controller.saveAction();

        // then:
        assertThat(model.getContacts().size(), equalTo(1));
        verify(contactRepository, only()).save(contact);
    }

    @Test
    public void update_existing_contact() {
        // given:
        AddressbookModel model = artifactManager.newInstance(AddressbookModel.class);
        contactsDataProvider.getContacts().forEach(c -> model.getContacts().add(new ObservableContact(c)));
        Contact contact = model.getContacts().get(0).getContact();
        contact.setId(1L);
        controller.setModel(model);
        model.getObservableContact().setContact(contact);

        // expectations:
        final AtomicBoolean invoked = new AtomicBoolean();
        doAnswer(invocation -> invoked.compareAndSet(false, true)).when(contactRepository).save(contact);

        // when:
        model.getObservableContact().setName("Updated");
        controller.saveAction();

        // then:
        assertThat(model.getContacts().size(), equalTo(2));
        assertThat(contact.getName(), equalTo("Updated"));
        verify(contactRepository, only()).save(contact);
    }

    @Nonnull
    private List<Module> moduleOverrides() {
        return asList(new AbstractTestingModule() {
            @Override
            protected void doConfigure() {
                bind(ContactRepository.class)
                    .toProvider(() -> mock(ContactRepository.class))
                    .asSingleton();
            }
        });
    }
}
