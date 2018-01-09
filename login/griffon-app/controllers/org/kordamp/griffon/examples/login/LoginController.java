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
package org.kordamp.griffon.examples.login;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

@ArtifactProviderFor(GriffonController.class)
public class LoginController extends AbstractGriffonController {
    @MVCMember @Nonnull
    private LoginModel model;

    @Inject
    private LoginService loginService;

    @ControllerAction
    public void login() {
        try {
            Subject subject = loginService.login(model.getUsername(), model.getPassword());
            getApplication().getContext().put(Subject.class.getName(), subject);
            getApplication().getMvcGroupManager().createMVCGroup(SampleMVCGroup.class);
        } catch (LoginException e) {
            getApplication().getEventRouter().publishEvent(new LoginFailureEvent(this));
            model.setPassword("");
        }
    }

    @ControllerAction
    public void cancel() {
        // getApplication().shutdown();
        getApplication().getWindowManager().hide("loginWindow");
    }
}