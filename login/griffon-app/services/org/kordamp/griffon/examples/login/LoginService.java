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
package org.kordamp.griffon.examples.login;

import griffon.core.artifact.GriffonService;
import griffon.core.resources.ResourceHandler;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Properties;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class LoginService extends AbstractGriffonService {
    private static final Logger LOG = LoggerFactory.getLogger(LoginService.class);

    private final Properties userDatabase = new Properties();

    @Inject
    public LoginService(@Nonnull ResourceHandler resourceHandler) {
        try (InputStream in = resourceHandler.getResourceAsStream("users.properties")) {
            userDatabase.load(in);
        } catch (IOException e) {
            LOG.warn("Could not load user database", e);
        }
    }

    @Nonnull
    public Subject login(@Nonnull String username, @Nonnull String password) throws LoginException {
        String storedPassword = userDatabase.getProperty(username);
        if (password.equals(storedPassword)) {
            Subject subject = new Subject();
            subject.getPrincipals().add(new UserPrincipal(username));
            return subject;
        }
        throw new LoginException("Invalid Credentials");
    }

    private static class UserPrincipal implements Principal {
        private final String username;

        private UserPrincipal(@Nonnull String username) {
            this.username = username;
        }

        @Override
        public String getName() {
            return username;
        }
    }
}