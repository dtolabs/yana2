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


 import com.dtolabs.SearchController


//@TestFor (SearchController)
class SearchControllerTests extends GroovyTestCase{
    def searchableService
    Node node1
    Node node2
    Node node3
    Node nodeProj2
    Project proj1
    Project proj2
    def SearchController scTest
    @Override
    protected void setUp() {
        super.setUp()
        proj1 = new Project(name: 'proj1', description: 'desc1')
        assertTrue proj1.validate()
        assertNotNull proj1.save()

        proj2 = new Project(name: 'proj2', description: 'desc1')
        assertTrue proj2.validate()
        assertNotNull proj2.save()

        final type1 = new NodeType(name: 'testtype', description: 'testtype desc',project:proj1)
        assertTrue type1.validate()
        assertNotNull type1.save()
        final type2 = new NodeType(name: 'test2type', description: 'test2type desc2', project: proj1)
        assertTrue type2.validate()
        assertNotNull type2.save()

        assert 0==Node.list().size()

        node1 = new Node(nodetype: type1, name: "node1", description: "node1 desc", tags: "taga, tagb, tagc", project: proj1)
        assertNotNull node1.save(flush: true)


        assert 1 == Node.list().size()

        node2 = new Node(nodetype: type2, name: "node2", description: "node2 desc", tags: "tagb, tagc", project: proj1)
        assertNotNull node2.save(flush: true)


        assert 2 == Node.list().size()

        node3 = new Node(nodetype: type2, name: "node3", description: "node3 desc", tags: "tagc", project: proj1)
        assertNotNull node3.save(flush: true)


        assert 3 == Node.list().size()

        final type3 = new NodeType(name: 'proj2Type', description: 'proj2 type desc', project: proj2)
        assertTrue type3.validate()
        assertNotNull type3.save()

        nodeProj2 = new Node(nodetype: type3, name: "nodeProj2", description: "nodeProj2 desc", tags: "tagc", project: proj2)
        assertNotNull nodeProj2.save(flush: true)


        assert 4== Node.list().size()

        scTest = new SearchController()
        scTest.searchableService = searchableService
    }

    @Override
    protected void tearDown() {
        super.tearDown()
        node1.delete(flush: true)
        node2.delete(flush: true)
        node3.delete(flush: true)
        nodeProj2.delete(flush: true)
    }
/**
     * Test basic query
     */
    void testBasic() {

        scTest.params.q="node1"
        scTest.params.project="proj1"
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
     * Test basic query
     */
    void testBasicAll() {

        scTest.params.q="*"
        scTest.params.project="proj1"
        def results= scTest.index()

        assertNotNull(results)
        assertNotNull(results.searchResult)
        assertNotNull(results.searchResult.results)
        assert results.searchResult.results.size()>=3

        def ids = new HashSet(results.searchResult.results.findAll{it.class.equals(Node.class)}.collect {it.id})
        assertTrue results.searchResult.results.every {it.class.equals(Node.class)}
        assert ids.contains(node1.id)
        assert ids.contains(node2.id)
        assert ids.contains(node3.id)
        assert !ids.contains(nodeProj2.id)
    }

    /**
     * Test basic query project specific
     */
    void testBasicAllProject2() {

        scTest.params.q="*"
        scTest.params.project="proj2"
        def results= scTest.index()

        assertNotNull(results)
        assertNotNull(results.searchResult)
        assertNotNull(results.searchResult.results)
        assert results.searchResult.results.size()>=1
        def ids = new HashSet(results.searchResult.results.findAll {it.class.equals(Node.class)}.collect {it.id})
        assertTrue results.searchResult.results.every {it.class.equals(Node.class)}
        assert ids.contains(nodeProj2.id)
        assert !ids.contains(node1.id)
        assert !ids.contains(node2.id)
        assert !ids.contains(node3.id)
    }

    /**
     * Test nodetype: field query for node type name
     */
    void testNodetypeField() {
        scTest.params.q="nodetype:testtype"
        scTest.params.project = "proj1"
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
        scTest.params.project = "proj1"
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
        scTest.params.project = "proj1"
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
        scTest.params.project = "proj1"
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
        scTest.params.project = "proj1"
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
        scTest.params.project = "proj1"
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
