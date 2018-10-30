/*
 * JBoss, Home of Professional Open Source.
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 */
package org.komodo.spi;

import org.komodo.spi.metadata.MetadataObserver;
import org.komodo.spi.repository.RepositoryObserver;

/**
 *
 */
public interface KClient extends RepositoryObserver, MetadataObserver {

    /**
     * The client state.
     */
    public static enum State {

        /**
         * Client has been successfully started.
         */
        STARTED,

        /**
         * Client is shutdown.
         */
        SHUTDOWN,

        /**
         * There was an error starting or shutting down the client.
         */
        ERROR

    }

    /**
     * @return the engine state (never <code>null</code>)
     */
    public State getState();
}
