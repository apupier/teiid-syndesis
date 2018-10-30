/*************************************************************************************
 * JBoss, Home of Professional Open Source.
* See the COPYRIGHT.txt file distributed with this work for information
* regarding copyright ownership. Some portions may be licensed
* to Red Hat, Inc. under one or more contributor license agreements.
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.
*
* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
* 02110-1301 USA.
 ************************************************************************************/
package org.komodo.spi.runtime;

import org.komodo.spi.Messages;
import org.komodo.spi.Messages.SPI;


/**
 * The <code>ExecutionConfigurationEvent</code> class is the event that is broadcast from the {@link TeiidInstanceManager instance manager}
 * when a teiid instance or connector is added, removed, or changed, or when a teiid instance is refreshed.
 *
 *
 */
public final class ExecutionConfigurationEvent {

    public static ExecutionConfigurationEvent createAddDataSourceEvent( TeiidDataSource dataSource ) {
        return new ExecutionConfigurationEvent(EventType.ADD, TargetType.DATA_SOURCE, dataSource);
    }

    public static ExecutionConfigurationEvent createDeployVDBEvent( String vdbName ) {
        return new ExecutionConfigurationEvent(EventType.ADD, TargetType.VDB, vdbName);
    }

    public static ExecutionConfigurationEvent createRemoveDataSourceEvent( TeiidDataSource dataSource ) {
        return new ExecutionConfigurationEvent(EventType.REMOVE, TargetType.DATA_SOURCE, dataSource);
    }

    public static ExecutionConfigurationEvent createUnDeployVDBEvent( String vdbName ) {
        return new ExecutionConfigurationEvent(EventType.REMOVE, TargetType.VDB, vdbName);
    }

    public static ExecutionConfigurationEvent createUpdateDataSourceEvent( TeiidDataSource dataSource ) {
        return new ExecutionConfigurationEvent(EventType.UPDATE, TargetType.DATA_SOURCE, dataSource);
    }

    private final EventType eventType;

    private final TargetType targetType;

    private final Object target;

    private ExecutionConfigurationEvent( EventType eventType,
                                         TargetType targetType,
                                         Object target) {
        if (target == null) {
            throw new IllegalArgumentException(Messages.getString(SPI.valueCannotBeNull, "target")); //$NON-NLS-1$
        }
        assert (eventType != null);
        assert (targetType != null);

        this.eventType = eventType;
        this.targetType = targetType;
        this.target = target;
    }

    /**
     * Create a refresh event.
     * 
     * @param targetType the target type that was refreshed
     */
    private ExecutionConfigurationEvent( TargetType targetType ) {
        this(EventType.REFRESH, targetType, null);
    }

    /**
     * @return the connector involved in the event
     * @throws IllegalStateException if method is called for a teiid instance event
     */
    public TeiidDataSource getDataSource() {
        if (this.targetType != TargetType.DATA_SOURCE) {
            throw new IllegalStateException(Messages.getString(Messages.SPI.invalidTargetTypeForGetDataSourceMethod, 
                                                           this.targetType,
                                                           TargetType.DATA_SOURCE));
        }

        return (TeiidDataSource)this.target;
    }

    /**
     * @return the event type (never <code>null</code>)
     */
    public EventType getEventType() {
        return this.eventType;
    }

    /**
     * @return the target type (never <code>null</code>)
     */
    public TargetType getTargetType() {
        return this.targetType;
    }

    /**
     * @return the connector involved in the event
     * @throws IllegalStateException if method is called for a teiid instance event
     */
    public TeiidTranslator getTranslator() {
        if (this.targetType != TargetType.TRANSLATOR) {
            throw new IllegalStateException(Messages.getString(Messages.SPI.invalidTargetTypeForGetTranslatorMethod,
                                                           this.targetType,
                                                           TargetType.TRANSLATOR));
        }

        return (TeiidTranslator)this.target;
    }

    public enum EventType {
        ADD,
        CONNECTED,
        REFRESH,
        REMOVE,
        UPDATE,
        DEFAULT;
    }

    public enum TargetType {
        TRANSLATOR,
        DATA_SOURCE,
        TINSTANCE,
        VDB,
        SOURCE_BINDING;
    }

}
