/*
 * Copyright 2009-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.groovy.eclipse.quickfix.test

import static org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants.FORMATTER_INSERT_SPACE_BEFORE_ASSIGNMENT_OPERATOR
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

import org.codehaus.groovy.eclipse.quickassist.GroovyQuickAssistContext
import org.codehaus.groovy.eclipse.quickassist.GroovyQuickAssistProposal
import org.codehaus.groovy.eclipse.quickassist.proposals.AssignStatementToNewLocalProposal
import org.codehaus.groovy.eclipse.quickassist.proposals.ConvertAccessorToPropertyProposal
import org.codehaus.groovy.eclipse.quickassist.proposals.ConvertClosureDefToMethodProposal
import org.codehaus.groovy.eclipse.quickassist.proposals.ConvertMethodDefToClosureProposal
import org.codehaus.groovy.eclipse.quickassist.proposals.ConvertToMultiLineStringProposal
import org.codehaus.groovy.eclipse.quickassist.proposals.ConvertToSingleLineStringProposal
import org.codehaus.groovy.eclipse.quickassist.proposals.ConvertVariableToFieldProposal
import org.codehaus.groovy.eclipse.quickassist.proposals.ExtractToConstantProposal
import org.codehaus.groovy.eclipse.quickassist.proposals.ExtractToLocalProposal
import org.codehaus.groovy.eclipse.quickassist.proposals.RemoveSpuriousSemicolonsProposal
import org.codehaus.groovy.eclipse.quickassist.proposals.ReplaceDefWithStaticTypeProposal
import org.codehaus.groovy.eclipse.quickassist.proposals.SplitVariableDeclAndInitProposal
import org.codehaus.groovy.eclipse.quickassist.proposals.SwapLeftAndRightOperandsProposal
import org.codehaus.groovy.eclipse.refactoring.test.extract.ConvertLocalToFieldTestsData
import org.codehaus.groovy.eclipse.refactoring.test.extract.ExtractConstantTestsData
import org.codehaus.groovy.eclipse.refactoring.test.extract.ExtractLocalTestsData
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.internal.ui.text.correction.AssistContext
import org.eclipse.jface.text.IDocument
import org.junit.Test

final class QuickAssistTests extends QuickFixTestSuite {

    @Test
    void testConvertToClosure1() {
        assertConversion(
            'def x()  { }',
            'def x = { }',
            'x', new ConvertMethodDefToClosureProposal())
    }

    @Test
    void testConvertToClosure2() {
        assertConversion(
            'class X { \ndef x()  { } }',
            'class X { \ndef x = { } }',
            'x', new ConvertMethodDefToClosureProposal())
    }

    @Test
    void testConvertToClosure3() {
        assertConversion(
            'def x(a)  { }',
            'def x = { a -> }',
            'x', new ConvertMethodDefToClosureProposal())
    }

    @Test
    void testConvertToClosure4() {
        assertConversion(
            'def x(int a, int b)  { }',
            'def x = { int a, int b -> }',
            'x', new ConvertMethodDefToClosureProposal())
    }

    @Test
    void testConvertToClosure5() {
        assertConversion(
            'def x(int a, int b)  { fdafsd }',
            'def x = { int a, int b -> fdafsd }',
            'x', new ConvertMethodDefToClosureProposal())
    }

    @Test
    void testConvertToClosure6() {
        assertConversion(
            'def x(int a, int b)\n { fdafsd }',
            'def x = { int a, int b -> fdafsd }',
            'x', new ConvertMethodDefToClosureProposal())
    }

    @Test
    void testConvertToClosure7() {
        assertConversion(
            'def x(int a, int b   )\n { fdafsd }',
            'def x = { int a, int b    -> fdafsd }',
            'x', new ConvertMethodDefToClosureProposal())
    }

    @Test
    void testConvertToClosure8() {
        assertConversion(
            'def x   (int a, int b   )\n { fdafsd }',
            'def x    = { int a, int b    -> fdafsd }',
            'x', new ConvertMethodDefToClosureProposal())
    }

    @Test
    void testConvertToClosure9() {
        assertConversion(
            'def x(int a, int b)  {\n  fdsafds }',
            'def x = { int a, int b ->\n  fdsafds }',
            'x', new ConvertMethodDefToClosureProposal())
    }

    @Test
    void testConvertToClosure10() {
        assertConversion(
            'def xxxx(int a, int b)  {\n  fdsafds }',
            'def xxxx = { int a, int b ->\n  fdsafds }',
            'x', new ConvertMethodDefToClosureProposal())
    }

    @Test
    void testConvertToClosure11() {
        assertConversion(
            'def "xx  xx"(int a, int b)  {\n  fdsafds }',
            'def "xx  xx" = { int a, int b ->\n  fdsafds }',
            'x', new ConvertMethodDefToClosureProposal())
    }

    @Test
    void testConvertToMethod1() {
        assertProposalNotOffered(
            'class X { def x = 1 }',
            15, 0, new ConvertClosureDefToMethodProposal())
    }

    @Test
    void testConvertToMethod2() {
        assertConversion(
            'class X { \ndef x = { } }',
            'class X { \ndef x() { } }',
            'x', new ConvertClosureDefToMethodProposal())
    }

    @Test
    void testConvertToMethod3() {
        assertConversion(
            'class X { \ndef x = { a ->  } }',
            'class X { \ndef x(a) {  } }',
            'x', new ConvertClosureDefToMethodProposal())
    }

    @Test
    void testConvertToMethod4() {
        assertConversion(
            'class X { \ndef x = {int a, int b -> } }',
            'class X { \ndef x(int a, int b) { } }',
            'x', new ConvertClosureDefToMethodProposal())
    }

    @Test
    void testConvertToMethod5() {
        assertConversion(
            'class X { \ndef x = {int a, int b -> fdafsd } }',
            'class X { \ndef x(int a, int b) { fdafsd } }',
            'x', new ConvertClosureDefToMethodProposal())
    }

    @Test
    void testConvertToMethod6() {
        assertConversion(
            'class X { \ndef x = {int a, int b -> fdafsd } }',
            'class X { \ndef x(int a, int b) { fdafsd } }',
            'x', new ConvertClosureDefToMethodProposal())
    }

    @Test
    void testConvertToMethod7() {
        assertConversion(
            'class X { \ndef x = {int a, int b   -> fdafsd } }',
            'class X { \ndef x(int a, int b) { fdafsd } }',
            'x', new ConvertClosureDefToMethodProposal())
    }

    @Test
    void testConvertToMethod8() {
        assertConversion(
            'class X { \ndef x    = {    int a, int b   -> fdafsd } }',
            'class X { \ndef x(int a, int b) { fdafsd } }',
            'x', new ConvertClosureDefToMethodProposal())
    }

    @Test
    void testConvertToMethod9() {
        assertConversion(
            'class X { \ndef x = {int a, int b\n ->\n  fdsafds } }',
            'class X { \ndef x(int a, int b) {\n  fdsafds } }',
            'x', new ConvertClosureDefToMethodProposal())
    }

    @Test
    void testConvertToMethod10() {
        assertConversion(
            'class X { \ndef xxxx = {int a, int b -> \n  fdsafds } }',
            'class X { \ndef xxxx(int a, int b) { \n  fdsafds } }',
            'x', new ConvertClosureDefToMethodProposal())
    }

    @Test
    void testConvertToMethod11() {
        assertConversion(
            'class X { \ndef xxxx = {int a, int b ->\n  fdsafds } }',
            'class X { \ndef xxxx(int a, int b) {\n  fdsafds } }',
            'x', new ConvertClosureDefToMethodProposal())
    }

    @Test
    void testConvertToProperty1() {
        assertConversion(
            '"".isEmpty()',
            '"".empty',
            4, 0, new ConvertAccessorToPropertyProposal())
    }

    @Test
    void testConvertToProperty2() {
        assertConversion(
            '"".getBytes()',
            '"".bytes',
            4, 0, new ConvertAccessorToPropertyProposal())
    }

    @Test
    void testConvertToProperty3() {
        assertProposalNotOffered(
            '"".getBytes("UTF-8")',
            4, 0, new ConvertAccessorToPropertyProposal())
    }

    @Test
    void testConvertToProperty4() {
        assertConversion(
            'new Date().setTime(1L);',
            'new Date().time = 1L;',
            'set', new ConvertAccessorToPropertyProposal())
    }

    @Test
    void testConvertToProperty4a() {
        setJavaPreference(FORMATTER_INSERT_SPACE_BEFORE_ASSIGNMENT_OPERATOR, JavaCore.DO_NOT_INSERT)
        try {
            assertConversion(
                'new Date().setTime(1L);',
                'new Date().time= 1L;',
                'set', new ConvertAccessorToPropertyProposal())
        } finally {
            setJavaPreference(FORMATTER_INSERT_SPACE_BEFORE_ASSIGNMENT_OPERATOR, JavaCore.INSERT)
        }
    }

    @Test
    void testConvertToProperty5() {
        assertProposalNotOffered(
            '[].set(1, null)',
            4, 0, new ConvertAccessorToPropertyProposal())
    }

    @Test
    void testConvertToProperty6() {
        assertProposalNotOffered(
            '"".length()',
            4, 0, new ConvertAccessorToPropertyProposal())
    }

    @Test
    void testConvertToMultiLine1() {
        assertConversion(
            '"fadfsad\\n\\t\' \\"\\nggggg"',
            '"""fadfsad\n\t\' "\nggggg"""',
            'f', new ConvertToMultiLineStringProposal())
    }

    @Test
    void testConvertToMultiLine2() {
        assertConversion(
            '\'fadfsad\\n\\t\\\' "\\nggggg\'',
            '\'\'\'fadfsad\n\t\' "\nggggg\'\'\'',
            'f', new ConvertToMultiLineStringProposal())
    }

    @Test
    void testConvertToMultiLine3() {
        assertConversion(
            'int a,b,c; def eq= "$a is\\n$b + ${c}"',
            'int a,b,c; def eq= """$a is\n$b + ${c}"""',
            'is', new ConvertToMultiLineStringProposal())
    }

    @Test
    void testConvertToSingleLine1() {
        assertConversion(
            '"""fadfsad\n\t\' "\nggggg"""',
            '"fadfsad\\n\\t\' \\"\\nggggg"',
            'f', new ConvertToSingleLineStringProposal())
    }

    @Test
    void testConvertToSingleLine2() {
        assertConversion(
            '\'\'\'fadfsad\n\t\' "\nggggg\'\'\'',
            '\'fadfsad\\n\\t\\\' "\\nggggg\'',
            'f', new ConvertToSingleLineStringProposal())
    }

    @Test
    void testRemoveSemicolons1() {
        assertConversion(
            'def a = 1;',
            'def a = 1',
            null, new RemoveSpuriousSemicolonsProposal())
    }

    @Test
    void testRemoveSemicolons2() {
        assertConversion(
            'def z = 1;def a = 1;',
            'def z = 1;def a = 1',
            null, new RemoveSpuriousSemicolonsProposal())
    }

    @Test
    void testReplaceDef1() {
        assertConversion(
            'int bar = 1; def foo = bar',
            'int bar = 1; int foo = bar',
            13, 0, new ReplaceDefWithStaticTypeProposal())
    }

    @Test
    void testReplaceDef2() {
        assertConversion(
            'int bar = 1; def foo = bar',
            'int bar = 1; int foo = bar',
            16, 0, new ReplaceDefWithStaticTypeProposal())
    }

    @Test
    void testReplaceDef3() {
        assertConversion(
            'def bar = 1g; def foo = bar',
            'def bar = 1g; BigInteger foo = bar',
            14, 3, new ReplaceDefWithStaticTypeProposal())
    }

    @Test
    void testReplaceDef4() {
        assertProposalNotOffered(
            'int bar = 1; def foo = bar',
            17, 0, new ReplaceDefWithStaticTypeProposal())
    }

    @Test
    void testReplaceDef5() {
        assertProposalNotOffered(
            'def method() { return null }',
            0, 3, new ReplaceDefWithStaticTypeProposal())
    }

    @Test
    void testSwapOperands1() {
        assertConversion(
            'if (c && ba) { }',
            'if (ba && c) { }',
            7, 1, new SwapLeftAndRightOperandsProposal())
    }

    @Test
    void testSwapOperands2() {
        assertConversion(
            'if (c && ba && hello) { }',
            'if (hello && c && ba) { }',
            13, 1, new SwapLeftAndRightOperandsProposal())
    }

    @Test
    void testSwapOperands3() {
        assertConversion(
            'if (c && ba && hello) { }',
            'if (ba && c && hello) { }',
            7, 1, new SwapLeftAndRightOperandsProposal())
    }

    @Test
    void testSwapOperands4() {
        assertConversion(
            'if (c && (ba && hello)) { }',
            'if ((ba && hello) && c) { }',
            7, 1, new SwapLeftAndRightOperandsProposal())
    }

    @Test
    void testSwapOperands5() {
        assertConversion(
            'def r = ba == c.q.q.q.q == ddd',
            'def r = ddd == ba == c.q.q.q.q',
            25, 1, new SwapLeftAndRightOperandsProposal())
    }

    @Test
    void testSwapOperands6() {
        assertConversion(
            'def r = ba == c.q.q.q.q == ddd',
            'def r = c.q.q.q.q == ba == ddd',
            12, 1, new SwapLeftAndRightOperandsProposal())
    }

    @Test
    void testSwapOperands7() {
        assertConversion(
            'v  && g && a',
            'g  && v && a',
            '&&', new SwapLeftAndRightOperandsProposal())
    }

    @Test
    void testSwapOperands8() {
        assertConversion(
            'g  || a && v',
            'g  || v && a',
            '&&', new SwapLeftAndRightOperandsProposal())
    }

    @Test
    void testSplitAssignment1() {
        assertConversion(
            'def foo = 1 + 4\n',
            'def foo\nfoo = 1 + 4\n',
            '=', new SplitVariableDeclAndInitProposal())
    }

    @Test
    void testSplitAssignment2() {
        assertConversion(
            'def foo = 1 + 4\n',
            'def foo\nfoo = 1 + 4\n',
            'def foo = 1 + 4', new SplitVariableDeclAndInitProposal())
    }

    @Test
    void testSplitAssignment3() {
        assertConversion(
            'String foo = "1 + 4"\n',
            'String foo\nfoo = "1 + 4"\n',
            '=', new SplitVariableDeclAndInitProposal())
    }

    @Test
    void testSplitAssignment4() {
        assertConversion(
            'def foo  =  1 + 4\n',
            'def foo\nfoo  =  1 + 4\n',
            'def foo  =  1 + 4', new SplitVariableDeclAndInitProposal())
    }

    @Test
    void testSplitAssignment5() {
        assertConversion(
            'def foo  =  1 + 4\n',
            'def foo\nfoo  =  1 + 4\n',
            '=', new SplitVariableDeclAndInitProposal())
    }

    @Test
    void testSplitAssignment6() {
        assertConversion(
            '/*something*/ def foo = 1 + 4\n',
            '/*something*/ def foo\nfoo = 1 + 4\n',
            '=', new SplitVariableDeclAndInitProposal())
    }

    @Test
    void testSplitAssignment7() {
        assertConversion(
            '/*something*/ def foo = 1 + 4\n',
            '/*something*/ def foo\nfoo = 1 + 4\n',
            'def foo = 1 + 4', new SplitVariableDeclAndInitProposal())
    }

    @Test
    void testSplitAssignment8() {
        assertConversion(
            'def z = b = 8\n',
            'def z\nz = b = 8\n',
            'def z = b = 8', new SplitVariableDeclAndInitProposal())
    }

    @Test
    void testSplitAssignment9() {
        String original = '''\
            class Foo {
            \tdef foo() {
            \t\tdef bar = 1 + 4
            \t}
            }
            '''.stripIndent()

        String expected = '''\
            class Foo {
            \tdef foo() {
            \t\tdef bar
            \t\tbar = 1 + 4
            \t}
            }
            '''.stripIndent()

        assertConversion(original, expected, 'def bar = 1 + 4', new SplitVariableDeclAndInitProposal())
    }

    @Test
    void testSplitAssignment10() {
        String original = '''\
            class X {
              def foo() {
                def x = 1
              }
            }
            '''.stripIndent()

        String expected = '''\
            class X {
              def foo() {
                def x
                x = 1
              }
            }
            '''.stripIndent()

        assertConversion(original, expected, 'x', new SplitVariableDeclAndInitProposal())
    }

    @Test
    void testAssignStatementToLocalRefactoring1() {
        assertConversion(
            'import java.awt.Point\n' + 'class Foo {\n' + '\tvoid bar(){\n' + 'new Point(1,2)\n' + '}}',
            'import java.awt.Point\n' + 'class Foo {\n' + '\tvoid bar(){\n' + 'def temp = new Point(1,2)\n' + '}}',
            'new Point', new AssignStatementToNewLocalProposal())
    }

    @Test
    void testAssignStatementToLocalRefactoring2() {
        assertConversion(
            'import java.awt.Point\n' + 'class Foo {\n' + '\tvoid bar(int a){\n' + 'bar(5)\n' + '}}',
            'import java.awt.Point\n' + 'class Foo {\n' + '\tvoid bar(int a){\n' + 'def bar = bar(5)\n' + '}}',
            'bar(5)', new AssignStatementToNewLocalProposal())
    }

    @Test
    void testAssignStatementToLocalRefactoring3() {
        assertConversion(
            'class Foo {\n' + '\tvoid bar(int a){\n' + '2 + 2\n' + '}}',
            'class Foo {\n' + '\tvoid bar(int a){\n' + 'def temp = 2 + 2\n' + '}}',
            '2 + 2', new AssignStatementToNewLocalProposal())
    }

    @Test
    void testAssignStatementToLocalRefactoring4() {
        assertConversion(
            'class Foo {\n' + '\tvoid bar(){\n' + 'false\n' + '}}',
            'class Foo {\n' + '\tvoid bar(){\n' + 'def false1 = false\n' + '}}',
            'false', new AssignStatementToNewLocalProposal())
    }

    @Test
    void testAssignStatementToLocalRefactoring5() {
        assertConversion(
            'class Foo {\n' + '\tvoid bar(){\n' + 'def false1 = true\n' + 'false\n' + '}}',
            'class Foo {\n' + '\tvoid bar(){\n' + 'def false1 = true\n' + 'def false2 = false\n' + '}}',
            'false\n', new AssignStatementToNewLocalProposal())
    }

    @Test
    void testAssignStatementToLocalRefactoring6() {
        assertConversion(
            'class Foo {\n' + '\tvoid bar(int a){\n' + '2\n' + '}}',
            'class Foo {\n' + '\tvoid bar(int a){\n' + 'def name = 2\n' + '}}',
            '2', new AssignStatementToNewLocalProposal())
    }

    @Test
    void testAssignStatementToLocalRefactoring7() {
        assertConversion(
            'class Foo {\n' + '\tvoid bar(int a){\n' + 'a == 2\n' + '}}',
            'class Foo {\n' + '\tvoid bar(int a){\n' + 'def temp = a == 2\n' + '}}',
            'a == 2', new AssignStatementToNewLocalProposal())
    }

    @Test
    void testAssignStatementToLocalRefactoring8() {
        assertConversion(
            'class Foo {\n' + '\tvoid bar(int a){\n' + '[1, 2]\n' + '}}',
            'class Foo {\n' + '\tvoid bar(int a){\n' + 'def list = [1, 2]\n' + '}}',
            '[1, 2]', new AssignStatementToNewLocalProposal())
    }

    @Test
    void testAssignStatementToLocalRefactoring9() {
        assertConversion(
            'class Foo {\n' + 'int bar(int a, int b){\n' + 'def aB\n' + 'a + b\n' + '}}',
            'class Foo {\n' + 'int bar(int a, int b){\n' + 'def aB\n' + 'def temp = a + b\n' + '}}',
            'a + b', new AssignStatementToNewLocalProposal())
    }

    @Test
    void testAssignStatementLocalRefactoring10() {
        assertConversion(
            'class Foo { def myClosure = { "foo".indexOf("qwerty") } }',
            'class Foo { def myClosure = { def indexOf = "foo".indexOf("qwerty") } }',
            '"foo".indexOf("qwerty")', new AssignStatementToNewLocalProposal())
    }

    @Test
    void testExtractToLocalRefactoring_1() {
        assertConversion(
            ExtractLocalTestsData.getTest1In(),
            ExtractLocalTestsData.getTest1Out(),
            ExtractLocalTestsData.findLocation('foo + bar', 'test1'),
            'foo + bar'.length(), new ExtractToLocalProposal(true))
    }

    @Test
    void testExtractToLocalRefactoring_2() {
        assertConversion(
            ExtractLocalTestsData.getTest2In(),
            ExtractLocalTestsData.getTest2Out(),
            ExtractLocalTestsData.findLocation('foo.bar', 'test2'),
            'foo.bar'.length(), new ExtractToLocalProposal(true))
    }

    @Test
    void testExtractToLocalRefactoring_3() {
        assertConversion(
            ExtractLocalTestsData.getTest3In(),
            ExtractLocalTestsData.getTest3Out(),
            ExtractLocalTestsData.findLocation('baz.foo.&bar', 'test3'),
            'baz.foo.&bar'.length(), new ExtractToLocalProposal(false))
    }

    @Test
    void testExtractToLocalRefactoring_4() {
        assertConversion(
            ExtractLocalTestsData.getTest4In(),
            ExtractLocalTestsData.getTest4Out(),
            ExtractLocalTestsData.findLocation('first + 1', 'test4'),
            'first + 1'.length(), new ExtractToLocalProposal(false))
    }

    @Test
    void testExtractToLocalRefactoring_5() {
        assertConversion(
            ExtractLocalTestsData.getTest5In(),
            ExtractLocalTestsData.getTest5Out(),
            ExtractLocalTestsData.findLocation('foo + bar', 'test5'),
            'foo + bar'.length(), new ExtractToLocalProposal(true))
    }

    @Test
    void testExtractToLocalRefactoring_6() {
        assertConversion(
            ExtractLocalTestsData.getTest6In(),
            ExtractLocalTestsData.getTest6Out(),
            ExtractLocalTestsData.findLocation('foo + bar', 'test6'),
            'foo + bar'.length(), new ExtractToLocalProposal(false))
    }

    @Test
    void testExtractToLocalRefactoring_7() {
        assertConversion(
            ExtractLocalTestsData.getTest7In(),
            ExtractLocalTestsData.getTest7Out(),
            ExtractLocalTestsData.findLocation('foo + bar', 'test7'),
            'foo + bar'.length(), new ExtractToLocalProposal(false))
    }

    @Test
    void testExtractToLocalRefactoring_8() {
        assertConversion(
            ExtractLocalTestsData.getTest8In(),
            ExtractLocalTestsData.getTest8Out(),
            ExtractLocalTestsData.findLocation('foo+  bar', 'test8'),
            'foo+  bar'.length(), new ExtractToLocalProposal(true))
    }

    @Test
    void testExtractToLocalRefactoring_9() {
        assertConversion(
            ExtractLocalTestsData.getTest9In(),
            ExtractLocalTestsData.getTest9Out(),
            ExtractLocalTestsData.findLocation('map.one', 'test9'),
            'map.one'.length(), new ExtractToLocalProposal(true))
    }

    @Test
    void testExtractToLocalRefactoring_10() {
        assertConversion(
            ExtractLocalTestsData.getTest10In(),
            ExtractLocalTestsData.getTest10Out(),
            ExtractLocalTestsData.findLocation('model.farInstance()', 'test10'),
            'model.farInstance()'.length(), new ExtractToLocalProposal(false))
    }

    @Test
    void testExtractToLocalRefactoring_10a() {
        assertConversion(
            ExtractLocalTestsData.getTest10In(),
            ExtractLocalTestsData.getTest10Out(),
            ExtractLocalTestsData.findLocation('model.farInstance() ', 'test10'),
            'model.farInstance() '.length(), new ExtractToLocalProposal(false))
    }

    @Test
    void testExtractToLocalRefactoring_10b() {
        assertConversion(
            ExtractLocalTestsData.getTest10In(),
            ExtractLocalTestsData.getTest10Out(),
            ExtractLocalTestsData.findLocation('model.farInstance()  ', 'test10'),
            'model.farInstance()  '.length(), new ExtractToLocalProposal(false))
    }

    @Test
    void testExtractToLocalRefactoring_11() {
        assertConversion(
            ExtractLocalTestsData.getTest11In(),
            ExtractLocalTestsData.getTest11Out(),
            ExtractLocalTestsData.findLocation('println "here"', 'test11'),
            'println "here"'.length(), new ExtractToLocalProposal(false))
    }

    @Test
    void testExtractToLocalRefactoring_12() {
        assertConversion(
            ExtractLocalTestsData.getTest12In(),
            ExtractLocalTestsData.getTest12Out(),
            ExtractLocalTestsData.findLocation('println "here"', 'test12'),
            'println "here"'.length(), new ExtractToLocalProposal(false))
    }

    @Test
    void testExtractToLocalRefactoring_13() {
        assertConversion(
            ExtractLocalTestsData.getTest13In(),
            ExtractLocalTestsData.getTest13Out(),
            ExtractLocalTestsData.findLocation('a + b', 'test13'),
            'a + b'.length(), new ExtractToLocalProposal(false))
    }

    @Test
    void testExtractToConstant_1() {
        assertConversion(
            ExtractConstantTestsData.getTest1In(),
            ExtractConstantTestsData.getTest1Out(),
            ExtractConstantTestsData.findLocation('Foo + Bar', 'test1'),
            'Foo + Bar'.length(), new ExtractToConstantProposal(true))
    }

    @Test
    void testExtractToConstant_2() {
        assertConversion(
            ExtractConstantTestsData.getTest2In(),
            ExtractConstantTestsData.getTest2Out(),
            ExtractConstantTestsData.findLocation('Foo + Bar', 'test2'),
            'Foo + Bar'.length(), new ExtractToConstantProposal(true))
    }

    @Test
    void testExtractToConstant_3() {
        assertConversion(
            ExtractConstantTestsData.getTest3In(),
            ExtractConstantTestsData.getTest3Out(),
            ExtractConstantTestsData.findLocation('Foo+Bar+A.frax()', 'test3'),
            'Foo+Bar+A.frax()'.length(), new ExtractToConstantProposal(true))
    }

    @Test
    void testExtractToConstant_4() {
        assertConversion(
            ExtractConstantTestsData.getTest4In(),
            ExtractConstantTestsData.getTest4Out(),
            ExtractConstantTestsData.findLocation('Foo+Bar+A.frax()', 'test4'),
            'Foo+Bar+A.frax()'.length(), new ExtractToConstantProposal(true))
    }

    @Test
    void testExtractToConstant_5a() {
        assertConversion(
            ExtractConstantTestsData.getTest5aIn(),
            ExtractConstantTestsData.getTest5aOut(),
            ExtractConstantTestsData.findLocation('Foo+Bar+A.frax()', 'test5a'),
            'Foo+Bar+A.frax()'.length(), new ExtractToConstantProposal(true))
    }

    @Test
    void testExtractToConstant_6a() {
        assertConversion(
            ExtractConstantTestsData.getTest6aIn(),
            ExtractConstantTestsData.getTest6aOut(),
            ExtractConstantTestsData.findLocation('Foo+Bar+A.frax()', 'test6a'),
            'Foo+Bar+A.frax()'.length(), new ExtractToConstantProposal(true))
    }

    @Test
    void testExtractToConstant_7() {
        assertProposalNotOffered(
            ExtractConstantTestsData.getTest7In(),
            ExtractConstantTestsData.findLocation('Foo + Bar', 'test7'),
            'Foo + Bar'.length(), new ExtractToConstantProposal(false))
    }

    @Test
    void testExtractToConstant_8() {
        assertConversion(
            ExtractConstantTestsData.getTest8In(),
            ExtractConstantTestsData.getTest8Out(),
            ExtractConstantTestsData.findLocation('Foo + Bar', 'test8'),
            'Foo + Bar'.length(), new ExtractToConstantProposal(false))
    }

    @Test
    void testExtractToConstant_NoReplaceOccurrences1() {
        assertConversion(
            ExtractConstantTestsData.getTestNoReplaceOccurrences1In(),
            ExtractConstantTestsData.getTestNoReplaceOccurrences1Out(),
            ExtractConstantTestsData.findLocation('Foo+Bar+A.frax()', 'testNoReplaceOccurrences1'),
            'Foo+Bar+A.frax()'.length(), new ExtractToConstantProposal(false))
    }

    @Test
    void testExtractToField_MethodToModule() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testMethodToModule')
        assertProposalNotOffered(testCase.getInput(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    @Test
    void testExtractToField_ClosureToModule() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testClosureToModule')
        assertProposalNotOffered(testCase.getInput(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    @Test
    void testExtractToField_DeclarationWithDef() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testDeclarationWithDef')
        assertConversion(testCase.getInput(), testCase.getExpected(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    @Test
    void testExtractToField_DeclarationWithType() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testDeclarationWithType')
        assertConversion(testCase.getInput(), testCase.getExpected(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    @Test
    void testExtractToField_Reference() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testReference')
        assertConversion(testCase.getInput(), testCase.getExpected(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    @Test
    void testExtractToField_TupleDeclaration() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testTupleDeclaration')
        assertProposalNotOffered(testCase.getInput(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    @Test
    void testExtractToField_Initialization() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testInitialization')
        assertConversion(testCase.getInput(), testCase.getExpected(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    @Test
    void testExtractToField_FieldReference() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testFieldReference')
        assertProposalNotOffered(testCase.getInput(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    @Test
    void testExtractToField_Exception() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testException')
        assertProposalNotOffered(testCase.getInput(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    @Test
    void testExtractToField_Prefix() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testPrefix')
        assertConversion(testCase.getInput(), testCase.getExpected(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    @Test
    void testExtractToField_MethodInvocation() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testMethodInvocation')
        assertConversion(testCase.getInput(), testCase.getExpected(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    @Test
    void testExtractToField_ParameterList() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testParameterList')
        assertProposalNotOffered(testCase.getInput(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    @Test
    void testExtractToField_ArgumentList() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testArgumentList')
        assertConversion(testCase.getInput(), testCase.getExpected(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    @Test
    void testExtractToField_InnerClass() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testInnerClass')
        assertConversion(testCase.getInput(), testCase.getExpected(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    @Test
    void testExtractToField_FakeField() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testFakeField')
        assertConversion(testCase.getInput(), testCase.getExpected(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    @Test
    void testExtractToField_ClosureParameterList() {
        ConvertLocalToFieldTestsData.TestCase testCase = ConvertLocalToFieldTestsData.getTestCases().get('testClosureParameterList')
        assertProposalNotOffered(testCase.getInput(), testCase.getSelectionOffset(), testCase.getSelectionLength(), new ConvertVariableToFieldProposal())
    }

    //

    private void assertConversion(String original, String expected, String target, GroovyQuickAssistProposal proposal) {
        int offset = (target == null ? 0 : original.indexOf(target)),
            length = (target == null ? 0 : target.length())
        assertConversion(original, expected, offset, length, proposal)
    }

    private void assertConversion(String original, String expected, int offset, int length, GroovyQuickAssistProposal proposal) {
        GroovyQuickAssistContext context = new GroovyQuickAssistContext(new AssistContext(addGroovySource(original), offset, length))
        assertTrue("Expected proposal \"${ -> proposal.displayString }\" to be relevant", proposal.withContext(context).getRelevance() > 0)
        IDocument document = context.newTempDocument(); proposal.apply(document)
        assertEquals('Invalid application of quick assist', expected, document.get())
    }

    private void assertProposalNotOffered(String original, int offset, int length, GroovyQuickAssistProposal proposal) {
        GroovyQuickAssistContext context = new GroovyQuickAssistContext(new AssistContext(addGroovySource(original), offset, length))
        assertFalse("Expected proposal \"${ -> proposal.displayString }\" to be irrelevant", proposal.withContext(context).getRelevance() > 0)
    }
}
