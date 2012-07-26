package com.dtolabs



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor (Project)
class ProjectTests {

    void testUniqueName() {
        Project p1 = new Project(name:'blah',description: 'x').save()
        assertNotNull p1

        Project p2 = new Project(name: 'blah', description: 'x')
        assertFalse p2.validate()
        assertTrue p2.hasErrors()
        assertTrue p2.errors.hasFieldErrors('name')

        assertNull p2.save()
    }

    void testName() {
        Project p1 = new Project(name:'',description: 'x')
        assertNotNull p1
        assertFalse p1.validate()
        assertTrue p1.errors.hasFieldErrors('name')
        assertFalse p1.errors.hasFieldErrors('description')


        Project p2 = new Project(name: 'z', description: '')
        assertNotNull p2
        assertFalse p2.validate()
        assertFalse p2.errors.hasFieldErrors('name')
        assertTrue p2.errors.hasFieldErrors('description')

    }
}
