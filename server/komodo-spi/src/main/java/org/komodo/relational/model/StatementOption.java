/*
 * Copyright Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags and
 * the COPYRIGHT.txt file distributed with this work.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.komodo.relational.model;

import org.komodo.relational.RelationalObject;
import org.komodo.spi.KException;
import org.komodo.spi.repository.KomodoType;
import org.komodo.spi.repository.Property;
import org.komodo.spi.repository.UnitOfWork;

/**
 * Represents a DDL statement option from a relational model.
 */
public interface StatementOption extends Property, RelationalObject {

    /**
     * Identifier of this object
     */
    KomodoType IDENTIFIER = KomodoType.STATEMENT_OPTION;

    /**
     * An empty collection of index constraints.
     */
    StatementOption[] NO_OPTIONS = new StatementOption[0];

    /**
     * The type identifier.
     */
    int TYPE_ID = StatementOption.class.hashCode();

    /**
     * @param transaction
     *        the transaction (cannot be <code>null</code> or have a state that is not {@link State#NOT_STARTED})
     * @return the statement option (can be empty)
     * @throws KException
     *         if an error occurs
     */
    String getOption( final UnitOfWork transaction ) throws KException;

    /**
     * @param transaction
     *        the transaction (cannot be <code>null</code> or have a state that is not {@link State#NOT_STARTED})
     * @param newOption
     *        the new value for the <code>statement option</code> property (cannot be empty)
     * @throws KException
     *         if an error occurs
     */
    void setOption( final UnitOfWork transaction,
                    final String newOption ) throws KException;

}
