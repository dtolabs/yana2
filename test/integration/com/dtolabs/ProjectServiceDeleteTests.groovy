package com.dtolabs

import com.dtolabs.yana2.springacl.YanaPermission
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils

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
 * ProjectServiceDeleteTests.java
 * 
 * User: Greg Schueler <a href="mailto:greg@dtosolutions.com">greg@dtosolutions.com</a>
 * Created: 7/27/12 1:54 PM
 * 
 */
class ProjectServiceDeleteTests extends GroovyTestCase {
    def ProjectService projectService

    private void loginAsAdmin() {
        // have to be authenticated as an admin to create ACLs
        SecurityContextHolder.context.authentication = new UsernamePasswordAuthenticationToken(
                'admin', 'admin',
                AuthorityUtils.createAuthorityList('ROLE_YANA_ADMIN'))
    }
    void testDeleteProject() {
        def p1 = new Project(name: 'test1', description: 'desc')
        assert null !=p1.save()
        def p2 = new Project(name: 'test2', description: 'desc')
        assert null !=p2.save()
        assertEquals 2, Project.list().size()

        //login
        loginAsAdmin()

        //add some acls for the project
        projectService.addPermission(p1, 'ROLE_YANA_USER', YanaPermission.READ)
        projectService.addPermission(p1, 'ROLE_YANA_ADMIN', YanaPermission.ADMINISTRATION)

        def result = projectService.deleteProject(p1)
        assertNotNull(result)
        assert result.success

        assertEquals 1, Project.list().size()

        SecurityContextHolder.clearContext()
    }

    void testDeleteProjectNodeTypes(){
        def p1 = new Project(name: 'test1', description: 'desc')
        assert null !=p1.save()
        def p2 = new Project(name: 'test2', description: 'desc')
        assert null !=p2.save()
        assertEquals 2, Project.list().size()

        def t1 = new NodeType(project:p1, name: 't1',description: 'd1',)
        assert null !=t1.save()

        def t2 = new NodeType(project:p2, name: 't2',description: 'd2',)
        assert null !=t2.save()

        //login
        loginAsAdmin()

        //add some acls for the project
        projectService.addPermission(p1, 'ROLE_YANA_USER', YanaPermission.READ)
        projectService.addPermission(p1, 'ROLE_YANA_ADMIN', YanaPermission.ADMINISTRATION)

        def result = projectService.deleteProject(p1)
        assertNotNull(result)
        assert result.success

        assertEquals 1, Project.list().size()
        assertEquals 1, NodeType.list().size()

        SecurityContextHolder.clearContext()
    }

    void testDeleteProjectNodeTypesAndNodes(){

        def p1 = new Project(name: 'test1', description: 'desc')
        assert null !=p1.save()
        def p2 = new Project(name: 'test2', description: 'desc')
        assert null !=p2.save()
        assertEquals 2, Project.list().size()

        def t1 = new NodeType(project:p1, name: 't1',description: 'd1',)
        assert null!=t1.save()

        def t2 = new NodeType(project:p2, name: 't2',description: 'd2',)
        assert null !=t2.save()

        def n1 = new Node(project:p1, name: 't1',description: 'd1', nodetype: t1)
        assert null !=n1.save()

        def n2 = new Node(project:p2, name: 't2',description: 'd2', nodetype: t2)
        assert null !=n2.save()

        //login
        loginAsAdmin()

        //add some acls for the project
        projectService.addPermission(p1, 'ROLE_YANA_USER', YanaPermission.READ)
        projectService.addPermission(p1, 'ROLE_YANA_ADMIN', YanaPermission.ADMINISTRATION)

        def result = projectService.deleteProject(p1)
        assertNotNull(result)
        assert result.success

        assertEquals 1, Project.list().size()
        assertEquals 1, NodeType.list().size()
        assertEquals 1, Node.list().size()

        SecurityContextHolder.clearContext()
    }

    /**
     * Test deleting a project with domain instances of all types
     */
    void testDeleteProjectAllDomains(){

        def p1 = new Project(name: 'test1', description: 'desc')
        assert null !=p1.save()
        def p2 = new Project(name: 'test2', description: 'desc')
        assert null !=p2.save()
        assertEquals 2, Project.list().size()

        def t1 = new NodeType(project:p1, name: 't1',description: 'd1',)
        assert null!=t1.save()

        def t1b = new NodeType(project:p1, name: 't1b',description: 'd1b',)
        assert null!=t1b.save()

        def t2 = new NodeType(project:p2, name: 't2',description: 'd2',)
        assert null !=t2.save()

        //nnodetype rleationship
        def ntr1 = new NodeTypeRelationship(name: 'blah', parent: t1, child: t1b)
        assert null!=ntr1.save()

        //node
        def n1 = new Node(project:p1, name: 't1',description: 'd1', nodetype: t1)
        assert null !=n1.save()
        def n1b = new Node(project:p1, name: 't1b',description: 'd1', nodetype: t1b)
        assert null !=n1b.save()

        def n2 = new Node(project:p2, name: 't2',description: 'd2', nodetype: t2)
        assert null !=n2.save()

        //filter
        def f1 = new Filter(project: p1, dataType: 'X', regex: 'y')
        assert null!=f1.save()

        def f2 = new Filter(project: p2, dataType: 'X2', regex: 'y2')
        assert null != f2.save()

        //attr
        def a1 = new Attribute(project: p1, name: 'blah', filter: f1)
        assert null!=a1.save()

        def a2 = new Attribute(project: p2, name: 'blah', filter: f2)
        assert null != a2.save()

        //node attr
        def na1 = new NodeAttribute(attribute: a1, nodetype: t1, required: false)
        assert null!=na1.save()

        def na2 = new NodeAttribute(attribute: a2, nodetype: t2, required: false)
        assert null != na2.save()

        //node value
        def nv1 = new NodeValue(node: n1, nodeattribute: na1, value: "y")
        assert null!=nv1.save()
        n1.addToNodeValues(nv1)
        assert null!=n1.save()

        def nv2 = new NodeValue(node: n2, nodeattribute: na2, value: "y")
        assert null != nv2.save()
        n2.addToNodeValues(nv2)
        assert null != n2.save()

        //child node
        def cn1 = new ChildNode(parent: n1, child: n1b)
        assert null!=cn1.save()


        assertEquals 2, Project.list().size()
        assertEquals 3, NodeType.list().size()
        assertEquals 3, Node.list().size()
        assertEquals 1, NodeTypeRelationship.list().size()
        assertEquals 2, Filter.list().size()
        assertEquals 2, Attribute.list().size()
        assertEquals 2, NodeAttribute.list().size()
        assertEquals 2, NodeValue.list().size()
        assertEquals 1, ChildNode.list().size()

        //login
        loginAsAdmin()

        //add some acls for the project
        projectService.addPermission(p1, 'ROLE_YANA_USER', YanaPermission.READ)
        projectService.addPermission(p1, 'ROLE_YANA_ADMIN', YanaPermission.ADMINISTRATION)

        def result = projectService.deleteProject(p1)
        assertNotNull(result)
        assert result.success

        assertEquals 1, Project.list().size()
        assertEquals 1, NodeType.list().size()
        assertEquals 1, Node.list().size()
        assertEquals 0, NodeTypeRelationship.list().size()
        assertEquals 1, Filter.list().size()
        assertEquals 1, Attribute.list().size()
        assertEquals 1, NodeAttribute.list().size()
        assertEquals 1, NodeValue.list().size()
        assertEquals 0, ChildNode.list().size()

        SecurityContextHolder.clearContext()
    }
}
