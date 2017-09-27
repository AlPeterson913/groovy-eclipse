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
package org.eclipse.jdt.core.groovy.tests.search;

import static org.eclipse.jdt.groovy.core.tests.GroovyBundle.isAtLeastGroovy;

import org.junit.Ignore;
import org.junit.Test;

public final class DGMInferencingTests extends InferencingTestSuite {

    @Test
    public void testDGM1() throws Exception {
        String contents = "[1].collectNested { it }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM2() throws Exception {
        String contents = "[1].collectNested { it }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM3() throws Exception {
        String contents = "1.with { it.intValue() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM4() throws Exception {
        String contents = "1.addShutdownHook { it.intValue() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM5() throws Exception {
        String contents = "[key:1].every { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "key";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.String");
    }

    @Test
    public void testDGM6() throws Exception {
        String contents = "[key:1].any { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "value";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM7() throws Exception {
        String contents = "[key:1].every { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "key";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.String");
    }

    @Test
    public void testDGM8() throws Exception {
        String contents = "[key:1].any { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "value";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM9() throws Exception {
        String contents = "[1].collectMany { [it.intValue()] }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test @Ignore
    public void testDGM10() throws Exception {
        // this one is not working since Inferencing Engine gets tripped up with the different variants of 'metaClass'
        String contents = "Integer.metaClass { this }";
        String str = "this";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "groovy.lang.MetaClass");
    }

    @Test
    public void testDGM11() throws Exception {
        String contents = "([1] ).collectEntries { index -> index.intValue() }";
        String str = "index";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM12() throws Exception {
        String contents = "[key:1].findResult(1) { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "key";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.String");
    }

    @Test
    public void testDGM13() throws Exception {
        String contents = "[key:1].findResult(1) { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "value";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM14() throws Exception {
        String contents = "[1].findResults { it.intValue() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM15() throws Exception {
        String contents = "[key:1].findResults { it.getKey().toUpperCase() + it.getValue().intValue() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.util.Map$Entry<java.lang.String,java.lang.Integer>");
    }

    @Test
    public void testDGM16() throws Exception {
        String contents = "[key:1].findResults { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "key";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.String");
    }

    @Test
    public void testDGM17() throws Exception {
        String contents = "[key:1].findResults { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "value";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM18() throws Exception {
        String contents = "[key:1].findAll { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "key";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.String");
    }

    @Test
    public void testDGM19() throws Exception {
        String contents = "[key:1].findAll { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "value";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM20() throws Exception {
        String contents = "[key:1].groupBy { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "key";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.String");
    }

    @Test
    public void testDGM21() throws Exception {
        String contents = "[key:1].groupBy { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "value";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM22() throws Exception {
        String contents = "([1]).countBy { it.intValue() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM23() throws Exception {
        String contents = "[key:1].groupEntriesBy { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "key";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.String");
    }

    @Test
    public void testDGM24() throws Exception {
        String contents = "[key:1].groupEntriesBy { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "value";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM25() throws Exception {
        String contents = "[key:1].inject(1) { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "key";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.String");
    }

    @Test
    public void testDGM26() throws Exception {
        String contents = "[key:1].inject(1) { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "value";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM27() throws Exception {
        String contents = "[key:1].withDefault { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "key";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.String");
    }

    @Test
    public void testDGM28() throws Exception {
        String contents = "[key:1].withDefault { key, value -> key.toUpperCase() + value.intValue() }";
        String str = "value";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.Integer");
    }

    @Test
    public void testDGM29() throws Exception {
        String contents = "new FileOutputStream().withStream { it }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.io.OutputStream");
    }

    @Test
    public void testDGM30() throws Exception {
        String contents = "new File(\"test\").eachFileMatch(FileType.FILES, 1) { it.getName() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.io.File");
    }

    @Test
    public void testDGM31() throws Exception {
        String contents = "new File(\"test\").eachDirMatch(FileType.FILES, 1) { it.getName() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.io.File");
    }

    @Test
    public void testDGM32() throws Exception {
        String contents = "new File(\"test\").withReader { it.reset() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.io.BufferedReader");
    }

    @Test
    public void testDGM33() throws Exception {
        String contents = "new FileReader(new File(\"test\")).filterLine(new FileWriter(new File(\"test\"))) { it.toUpperCase() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.String");
    }

    @Test
    public void testDGM34() throws Exception {
        String contents = "new File(\"test\").withOutputStream { it.flush() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.io.OutputStream");
    }

    @Test
    public void testDGM35() throws Exception {
        String contents = "new File(\"test\").withInputStream { it.flush() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.io.InputStream");
    }

    @Test
    public void testDGM36() throws Exception {
        String contents = "new File(\"test\").withDataOutputStream { it.flush() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.io.DataOutputStream");
    }

    @Test
    public void testDGM37() throws Exception {
        String contents = "new File(\"test\").withDataInputStream { it.flush() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.io.DataInputStream");
    }

    @Test
    public void testDGM38() throws Exception {
        String contents = "new File(\"test\").withWriter { it.flush() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.io.BufferedWriter");
    }

    @Test
    public void testDGM39() throws Exception {
        String contents = "new File(\"test\").withWriterAppend { it.flush() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.io.BufferedWriter");
    }

    @Test
    public void testDGM40() throws Exception {
        String contents = "new File(\"test\").withPrintWriter { it.flush() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.io.PrintWriter");
    }

    @Test
    public void testDGM41() throws Exception {
        String contents = "new FileReader(new File(\"test\")).transformChar(new FileWriter(new File(\"test\"))) { it.toUpperCase() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.String");
    }

    @Test
    public void testDGM42() throws Exception {
        String contents = "new FileReader(new File(\"test\")).transformLine(new FileWriter(new File(\"test\"))) { it.toUpperCase() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.String");
    }

    @Test
    public void testDGM43() throws Exception {
        String contents = "\"\".eachMatch(\"\") { it.toLowerCase() }";
        String str = "it";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        assertType(contents, start, end, "java.lang.String");
    }

    @Test // GRECLIPSE-1695
    public void testDGM44() throws Exception {
        String contents = "List<String> myList = new ArrayList<String>()\n" +
            "myList.toSorted { a, b ->\n" +
            "  a.trim() <=> b.trim()\n" +
            "}.each {\n" +
            "  it\n" +
            "}\n";
        int start = contents.lastIndexOf("it");
        int end = start + "it".length();
        assertType(contents, start, end, "java.lang.String");
    }

    @Test // GRECLIPSE-1695 redux
    public void testDGM45() throws Throwable {
        // Java 8 adds default method sort(Comparator) to List interface...
        // TypeInferencingVisitorWithRequestor.lookupExpressionType replaces DGM (from CategoryTypeLookup) with JDK (from SimpleTypeLookup)
        String contents = "List<String> myList = new ArrayList<String>()\n" +
            "myList.sort { a, b ->\n" +
            "  a.trim() <=> b.trim()\n" +
            "}.each {\n" +
            "  it\n" +
            "}\n";
        int start = contents.lastIndexOf("it");
        int end = start + "it".length();
        assertTypeOneOf(contents, start, end, "java.lang.Void", "java.lang.String");
    }

    @Test
    public void testDGM46() throws Exception {
        String contents = "java.util.regex.Pattern[] pats = [~/one/, ~/two/]\n" +
            "pats.eachWithIndex { pat, idx ->\n" + // T <T> eachWithIndex(T self, Closure task)
            "  \n" +
            "}\n";
        int start = contents.indexOf("eachWithIndex");
        int end = start + "eachWithIndex".length();
        assertType(contents, start, end, "java.util.regex.Pattern[]");
    }

    @Test
    public void testDGM47() throws Exception {
        String contents = "java.util.regex.Pattern[] pats = [~/one/, ~/two/]\n" +
            "pats.eachWithIndex { pat, idx ->\n" +
            "  \n" +
            "}.collect {\n" + // T <T> collect(Object self, Closure<T> task)
            "  it\n" +
            "}\n";
        int start = contents.indexOf("collect");
        int end = start + "collect".length();
        assertType(contents, start, end, "java.util.List<T>"); // better than 'unknown'
    }

    @Test
    public void testDGM48() throws Exception {
        String contents = "int[] ints = [1, 2, 3]\n" +
            "String dgm(Object[] arr) { null }\n" +
            "Object dgm(Object obj) { null }\n" +
            "def result = dgm(ints)\n";
        int start = contents.indexOf("result");
        int end = start + "result".length();
        assertType(contents, start, end, "java.lang.Object");
    }

    @Test
    public void testDGM48a() throws Exception {
        // TODO: runtime preference seems to be the Object method
        String contents = "int[] ints = [1, 2, 3]\n" +
            "Object dgm(Object obj) { null }\n" +
            "String dgm(Object[] arr) { null }\n" +
            "def result = dgm(ints)\n";
        int start = contents.indexOf("result");
        int end = start + "result".length();
        assertType(contents, start, end, "java.lang.String");
    }

    @Test
    public void testDGM49() throws Exception {
        // primitive array is not compatible with boxed-type array
        String contents = "int[] ints = [1, 2, 3]\n" +
            "Integer dgm(Integer[] arr) { null }\n" +
            "Object dgm(Object obj) { null }\n" +
            "def result = dgm(ints)\n";
        int start = contents.indexOf("result");
        int end = start + "result".length();
        assertType(contents, start, end, "java.lang.Object");
    }

    @Test
    public void testDGM50() throws Exception {
        // SimpleTypeLookup returns first method in case of no type-compatible matches
        // TODO: primitive array is not compatible with derived-from-boxed-type array
        String contents = "int[] ints = [1, 2, 3]\n" +
            "Number dgm(Number[] arr) { null }\n" +
            "def result = dgm(ints)\n";
        int start = contents.indexOf("result");
        int end = start + "result".length();
        assertType(contents, start, end, "java.lang.Number");
        //assertUnknownConfidence(contents, start, end, "java.lang.Object", false);
    }

    @Test
    public void testDGM50a() throws Exception {
        String contents = "Integer[] ints = [1, 2, 3]\n" +
            "Number dgm(Number[] arr) { null }\n" +
            "def result = dgm(ints)\n";
        int start = contents.indexOf("result");
        int end = start + "result".length();
        assertType(contents, start, end, "java.lang.Number");
    }

    @Test
    public void testDGMDeclaring1() throws Exception {
        // With groovy 2.0, there are some new DGM classes.  Need to ensure that we are using those classes as the declaring type, but only for 2.0 or later.
        String contents = "\"\".eachLine";
        String str = "eachLine";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        if (isAtLeastGroovy(20)) {
            assertDeclaringType(contents, start, end, "org.codehaus.groovy.runtime.StringGroovyMethods");
        } else {
            assertDeclaringType(contents, start, end, "org.codehaus.groovy.runtime.DefaultGroovyMethods");
        }
    }

    @Test
    public void testDGMDeclaring2() throws Exception {
        String contents = "new File().eachLine";
        String str = "eachLine";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        if (isAtLeastGroovy(20)) {
            assertDeclaringType(contents, start, end, "org.codehaus.groovy.runtime.ResourceGroovyMethods");
        } else {
            assertDeclaringType(contents, start, end, "org.codehaus.groovy.runtime.DefaultGroovyMethods");
        }
    }

    @Test
    public void testDGMDeclaring3() throws Exception {
        String contents = "Writer w\nw.leftShift";
        String str = "leftShift";
        int start = contents.lastIndexOf(str);
        int end = start + str.length();
        if (isAtLeastGroovy(20)) {
            assertDeclaringType(contents, start, end, "org.codehaus.groovy.runtime.IOGroovyMethods");
        } else {
            assertDeclaringType(contents, start, end, "org.codehaus.groovy.runtime.DefaultGroovyMethods");
        }
    }
}
