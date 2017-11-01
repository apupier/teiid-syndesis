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
package org.komodo.core.internal.sequencer;

import javax.jcr.Node;
import org.junit.Test;
import org.komodo.spi.lexicon.sql.teiid.TeiidSqlLexicon.Block;
import org.komodo.spi.lexicon.sql.teiid.TeiidSqlLexicon.CommandStatement;
import org.komodo.spi.lexicon.sql.teiid.TeiidSqlLexicon.CreateProcedureCommand;
import org.komodo.spi.lexicon.sql.teiid.TeiidSqlLexicon.From;
import org.komodo.spi.lexicon.sql.teiid.TeiidSqlLexicon.GroupBy;
import org.komodo.spi.lexicon.sql.teiid.TeiidSqlLexicon.Query;
import org.komodo.spi.lexicon.sql.teiid.TeiidSqlLexicon.SPParameter;
import org.komodo.spi.lexicon.sql.teiid.TeiidSqlLexicon.Select;
import org.komodo.spi.lexicon.sql.teiid.TeiidSqlLexicon.StoredProcedure;
import org.komodo.spi.lexicon.sql.teiid.TeiidSqlLexicon.SubqueryContainer;

/**
 *
 */
@SuppressWarnings( {"javadoc", "nls"} )
public class TestTeiidSqlSequencer extends AbstractTestTeiidSqlSequencer {

    @Test
    public void testGroupByRollup() throws Exception {
        String sql = "SELECT a FROM m.g GROUP BY rollup(b, c)";
        Node fileNode = sequenceSql(sql, TSQL_QUERY);

        Node queryNode = verify(fileNode, Query.ID, Query.ID);

        Node selectNode = verify(queryNode, Query.SELECT_REF_NAME, Select.ID);
        verifyElementSymbol(selectNode, Select.SYMBOLS_REF_NAME, "a");

        Node fromNode = verify(queryNode, Query.FROM_REF_NAME, From.ID);
        verifyUnaryFromClauseGroup(fromNode, From.CLAUSES_REF_NAME, 1, "m.g");

        Node groupByNode = verify(queryNode, Query.GROUP_BY_REF_NAME, GroupBy.ID);
        verifyElementSymbol(groupByNode, GroupBy.SYMBOLS_REF_NAME,  1, "b");
        verifyElementSymbol(groupByNode, GroupBy.SYMBOLS_REF_NAME,  2, "c");
        verifyProperty(groupByNode, GroupBy.ROLLUP_PROP_NAME, true);
    }

    @Test
    public void testStoredQuery2SanityCheck() throws Exception {
        String sql = "BEGIN exec proc1('param1'); END";
        Node fileNode = sequenceSql(sql, TSQL_PROC_CMD);

        Node createProcNode = verify(fileNode, CreateProcedureCommand.ID, CreateProcedureCommand.ID);
        Node outerBlkNode = verify(createProcNode, CreateProcedureCommand.BLOCK_REF_NAME, Block.ID);
        Node cmdStmtNode = verify(outerBlkNode, Block.STATEMENTS_REF_NAME, CommandStatement.ID);

        Node storedProcNode = verify(cmdStmtNode, SubqueryContainer.COMMAND_REF_NAME, StoredProcedure.ID);
        verifyProperty(storedProcNode, StoredProcedure.PROCEDURE_NAME_PROP_NAME, "proc1");

        Node param1Node = verify(storedProcNode, StoredProcedure.PARAMETERS_REF_NAME, SPParameter.ID);
        verifyProperty(param1Node, SPParameter.PARAMETER_TYPE_PROP_NAME, 1);
        verifyProperty(param1Node, SPParameter.INDEX_PROP_NAME, 1);
        verifyConstant(param1Node, SPParameter.EXPRESSION_REF_NAME, "param1");
    }
}
