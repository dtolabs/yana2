/*
 * Copyright 2012 DTO Labs, Inc. (http://dtolabs.com)
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
 *
 */
 
/*
 * SearchControllerTests.java
 * 
 * User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 * Created: 5/22/12 4:33 PM
 * 
 */

 package com.dtolabs


import com.dtosolutions.SearchController;


//@TestFor (SearchController)
class SearchControllerTests extends GroovyTestCase{
    def searchableService
    Node node1
    Node node2
    Node node3
    def SearchController scTest
    @Override
    protected void setUp() {
        super.setUp()
        final type1 = new NodeType(name: 'testtype', description: 'testtype desc')
        assertTrue type1.validate()
        assertNotNull type1.save()
        final type2 = new NodeType(name: 'test2type', description: 'test2type desc2')
        assertTrue type2.validate()
        assertNotNull type2.save()
        node1 = new Node(nodetype: type1, name: "node1", description: "node1 desc", tags: "taga, tagb, tagc", status: Status.TEST)
        assertNotNull node1.save(flush: true)

        node2 = new Node(nodetype: type2, name: "node2", description: "node2 desc", tags: "tagb, tagc", status: Status.TEST)
        assertNotNull node2.save(flush: true)

        node3 = new Node(nodetype: type2, name: "node3", description: "node3 desc", tags: "tagc", status: Status.TEST)
        assertNotNull node3.save(flush: true)

        scTest = new SearchController()
        scTest.searchableService = searchableService
    }

    /**
     * Test basic query
     */
    void testBasic() {

        scTest.params.q="node1"
        def results= scTest.index()

        assertNotNull(results)
        assertNotNull(results.searchResult)
        assertNotNull(results.searchResult.results)
        assertEquals(1,results.searchResult.results.size())
        assertEquals(Node.class,results.searchResult.results[0].class)
        final Node noderes = results.searchResult.results[0]
        assertEquals("node1",noderes.name)
        assertEquals(node1.id,noderes.id)
    }

    /**
     * Test nodetype: field query for node type name
     */
    void testNodetypeField() {
        scTest.params.q="nodetype:testtype"
        def results= scTest.index()

        assertNotNull(results)
        assertNotNull(results.searchResult)
        assertNotNull(results.searchResult.results)
        assertTrue(results.searchResult.results.size()>0)

        def ids = new HashSet(results.searchResult.results.collect {it.id})
        assertTrue results.searchResult.results.every {it.class.equals(Node.class)}
        assertTrue ids.contains(node1.id)
        assertFalse ids.contains(node2.id)
        assertFalse ids.contains(node3.id)

        scTest.params.q="nodetype:test2type"
        results= scTest.index()


        assertNotNull(results)
        assertNotNull(results.searchResult)
        assertNotNull(results.searchResult.results)
        assertTrue(results.searchResult.results.size() > 0)
        ids = new HashSet(results.searchResult.results.collect {it.id})
        assertTrue results.searchResult.results.every {it.class.equals(Node.class)}
        assertTrue ids.contains(node2.id)
        assertTrue ids.contains(node3.id)
        assertFalse ids.contains(node1.id)
    }
    /**
     * Test implicit query for node type name (on "all" index field)
     */
    void testNodetypeAll() {
        scTest.params.q="testtype"
        def results= scTest.index()

        assertNotNull(results)
        assertNotNull(results.searchResult)
        assertNotNull(results.searchResult.results)
        assertTrue(results.searchResult.results.size()>0)

        def ids = new HashSet(results.searchResult.results.collect {it.id})
        assertTrue results.searchResult.results.every {it.class.equals(Node.class)}
        assertTrue ids.contains(node1.id)
        assertFalse ids.contains(node2.id)
        assertFalse ids.contains(node3.id)

        scTest.params.q="test2type"
        results= scTest.index()


        assertNotNull(results)
        assertNotNull(results.searchResult)
        assertNotNull(results.searchResult.results)
        assertTrue(results.searchResult.results.size() > 0)
        ids = new HashSet(results.searchResult.results.collect {it.id})
        assertTrue results.searchResult.results.every {it.class.equals(Node.class)}
        assertTrue ids.contains(node2.id)
        assertTrue ids.contains(node3.id)
        assertFalse ids.contains(node1.id)
    }

    /**
     * Test explicit tags query
     */
    void testTags() {
        scTest.params.q="tags:taga"
        def results= scTest.index()

        assertNotNull(results)
        assertNotNull(results.searchResult)
        assertNotNull(results.searchResult.results)
        assertTrue(results.searchResult.results.size()>0)
        def ids = new HashSet(results.searchResult.results.collect {it.id})
        assertTrue results.searchResult.results.every {it.class.equals(Node.class)}
        assertTrue ids.contains(node1.id)
        assertFalse ids.contains(node2.id)
        assertFalse ids.contains(node3.id)

        scTest.params.q="tags:tagb"
        results= scTest.index()

        assertNotNull(results)
        assertNotNull(results.searchResult)
        assertNotNull(results.searchResult.results)
        assertTrue(results.searchResult.results.size() > 0)

        ids = new HashSet(results.searchResult.results.collect {it.id})
        assertTrue results.searchResult.results.every {it.class.equals(Node.class)}
        assertTrue ids.contains(node1.id)
        assertTrue ids.contains(node2.id)
        assertFalse ids.contains(node3.id)
    }
}
