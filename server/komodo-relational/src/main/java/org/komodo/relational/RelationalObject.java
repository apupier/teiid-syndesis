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
package org.komodo.relational;

import org.komodo.spi.lexicon.LexiconConstants.JcrLexicon;
import org.komodo.spi.lexicon.LexiconConstants.NTLexicon;
import org.komodo.spi.lexicon.ddl.StandardDdlLexicon;
import org.komodo.spi.repository.Descriptor;
import org.komodo.spi.repository.KomodoObject;
import org.komodo.spi.repository.PropertyDescriptor;
import org.komodo.utils.ArgCheck;

/**
 * A {@link KomodoObject} related to a relational model.
 */
public interface RelationalObject extends KomodoObject {

    /**
     * A filter to use when deciding which properties and descriptors apply to an object.
     */
    public interface Filter {

        /**
         * @param descriptorName
         *        the name of the descriptor being checked (cannot be empty)
         * @return <code>true</code> if the descriptor should be reject
         */
        boolean rejectDescriptor( final String descriptorName );

        /**
         * @param propName
         *        the name of the property being checked (cannot be empty)
         * @return <code>true</code> if this property should be rejected
         */
        boolean rejectProperty( final String propName );

    }

    /**
     * A filter to exclude specific DDL-namespaced properties and type descriptors.
     */
    Filter DDL_QNAMES_FILTER = new ExcludeQNamesFilter( StandardDdlLexicon.DDL_EXPRESSION,
                                                        StandardDdlLexicon.DDL_LENGTH,
                                                        StandardDdlLexicon.DEFAULT_PRECISION,
                                                        StandardDdlLexicon.DEFAULT_OPTION,
                                                        StandardDdlLexicon.DDL_ORIGINAL_EXPRESSION,
                                                        StandardDdlLexicon.DDL_START_CHAR_INDEX,
                                                        StandardDdlLexicon.DDL_START_COLUMN_NUMBER,
                                                        StandardDdlLexicon.DDL_START_LINE_NUMBER );

    /**
     * A filter to exclude JCR-namespaced properties and type descriptors.
     */
    Filter JCR_FILTER = new ExcludeNamespaceFilter( JcrLexicon.Namespace.PREFIX, JcrLexicon.Namespace.URI );

    /**
     * An empty collection of filters.
     */
    Filter[] NO_FILTERS = new Filter[ 0 ];

    /**
     * A filter to exclude NT-namespaced properties and type descriptors.
     */
    Filter NT_FILTER = new ExcludeNamespaceFilter( NTLexicon.Namespace.PREFIX, NTLexicon.Namespace.URI );

    /**
     * A filter to exclude residual properties and type descriptors.
     */
    Filter RESIDUAL_FILTER = new Filter() {

        private String NAME = "*"; //$NON-NLS-1$

        /**
         * {@inheritDoc}
         *
         * @see org.komodo.relational.RelationalObject.Filter#rejectProperty(java.lang.String)
         */
        @Override
        public boolean rejectProperty( final String propName ) {
            ArgCheck.isNotEmpty( propName, "propName" ); //$NON-NLS-1$
            return NAME.equals( propName );
        }

        /**
         * {@inheritDoc}
         *
         * @see org.komodo.relational.RelationalObject.Filter#rejectDescriptor(java.lang.String)
         */
        @Override
        public boolean rejectDescriptor( final String descriptorName ) {
            ArgCheck.isNotEmpty( descriptorName, "descriptorName" ); //$NON-NLS-1$
            return NAME.equals( descriptorName );
        }

    };

    /**
     * The default set of filters for restricting which properties and descriptors apply to relational objects.
     */
    Filter[] DEFAULT_FILTERS = new Filter[] { DDL_QNAMES_FILTER, JCR_FILTER, NT_FILTER, RESIDUAL_FILTER };

    /**
     * @return the filters to use when deciding which {@link PropertyDescriptor properties} and {@link Descriptor descriptors} are
     *         valid for this object (never <code>null</code> but can be empty)
     */
    Filter[] getFilters();

    /**
     * @param newFilters
     *        the new set of filters to use when deciding which {@link PropertyDescriptor properties} and {@link Descriptor
     *        descriptors} are valid for this object (can be <code>null</code>)
     */
    void setFilters( final Filter[] newFilters );

}
