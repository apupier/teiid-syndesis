/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.komodo.relational.commands.pushdownfunction;

import java.util.Collections;
import org.junit.Test;
import org.komodo.relational.commands.AbstractCommandTest;
import org.komodo.shell.api.CommandResult;
import org.komodo.spi.lexicon.ddl.teiid.TeiidDdlLexicon.CreateProcedure;

/**
 * Test Class to test {@link PushdownFunctionRenameCommand}.
 */
@SuppressWarnings( { "javadoc",
                     "nls" } )
public final class PushdownFunctionRenameCommandTest extends AbstractCommandTest {

    @Test( expected = AssertionError.class )
    public void shouldNotAllowRenameOfParameterNameToResultSet() throws Exception {
        setup( "commandFiles", "addPushdownFuncParams.cmd" );

        final String[] commands = { "rename myParameter1 " + CreateProcedure.RESULT_SET };
        execute( commands );
    }

    @Test( expected = AssertionError.class )
    public void shouldNotAllowRenameSelfToResultSet() throws Exception {
        setup( "commandFiles", "addPushdownFunctions.cmd" );

        final String[] commands = { "cd myPushdownFunction1",
                                    "rename " + CreateProcedure.RESULT_SET };
        execute( commands );
    }

    @Test
    public void shouldNotHaveResultSetCandidateInTabCompletion() throws Exception {
        setup( "commandFiles", "addPushdownFunctions.cmd" );

        final String[] commands = { "cd myPushdownFunction1",
                                    "set-result-set DataTypeResultSet" };
        final CommandResult result = execute( commands );
        assertCommandResultOk( result );
        assertTabCompletion( "rename ", Collections.emptyList() );
    }

}
