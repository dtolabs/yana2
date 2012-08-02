package com.dtolabs

import org.springframework.transaction.TransactionStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.access.prepost.PostFilter

import org.springframework.security.acls.model.Permission
import com.dtolabs.yana2.springacl.YanaPermission
import org.springframework.security.acls.model.ObjectIdentity
import org.springframework.security.acls.model.Sid
import org.springframework.security.acls.model.NotFoundException
import org.springframework.security.acls.domain.GrantedAuthoritySid
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.core.Authentication

class ProjectService {
    static transactional = true

    def servletContext

    def objectIdentityRetrievalStrategy
    def aclPermissionFactory
    def aclService
    def aclUtilService
    def springSecurityService


    @PreAuthorize("hasPermission(#project, admin) or hasRole('ROLE_YANA_ADMIN') or hasRole('ROLE_YANA_SUPERUSER')")
    void addPermission(Project project, String username, Permission permission) {
        aclUtilService.addPermission project, username, permission
    }

    void addPermission(Project project, String username, int permission) {
        addPermission project, username, aclPermissionFactory.buildFromMask(permission)
    }

    /**
     * Deny a permission. Used when you have the instance available.
     *
     * @param domainObject the domain class instance
     * @param recipient the grantee; can be a username, role name, Sid, or Authentication
     * @param permission the permission to grant
     */
    @PreAuthorize("hasPermission(#project, admin)")
    void denyPermission(Project project, String recipient, Permission permission) {
        ObjectIdentity oid = objectIdentityRetrievalStrategy.getObjectIdentity(project)
        int_denyPermission oid, recipient, permission
    }

    @PreAuthorize("hasPermission(#project, admin)")
    void denyPermission(Project project, String username, int permission) {
        denyPermission project, username, aclPermissionFactory.buildFromMask(permission)
    }
    /**
     * Deny a permission.
     *
     * @param oid represents the domain object
     * @param recipient the grantee; can be a username, role name, Sid, or Authentication
     * @param permission the permission to grant
     */
    private void int_denyPermission(ObjectIdentity oid, recipient, Permission permission) {

        Sid sid = createSid(recipient)

        def acl
        try {
            acl = aclService.readAclById(oid)
        }
        catch (NotFoundException e) {
            acl = aclService.createAcl(oid)
        }

        acl.insertAce 0, permission, sid, false
        aclService.updateAcl acl

        log.debug "Denied permission $permission for Sid $sid for $oid.type with id $oid.identifier"
    }

    private Sid createSid(recipient) {
        if (recipient instanceof String) {
            return recipient.startsWith('ROLE_') ?
                   new GrantedAuthoritySid(recipient) :
                   new PrincipalSid(recipient)
        }

        if (recipient instanceof Sid) {
            return recipient
        }

        if (recipient instanceof Authentication) {
            return new PrincipalSid(recipient)
        }

        throw new IllegalArgumentException('recipient must be a String, Sid, or Authentication')
    }


    @PreAuthorize("hasPermission(#id, 'com.dtolabs.Project', read) or hasPermission(#id, 'com.dtolabs.Project', admin)")
    Project getProject(long id){
        Project.get(id)
    }
    Project findProject(String name) {
        def project = Project.findByName(name)
        return project?getProject(project.id):null
    }

    /**
     * Return true if the user has Architect permission for the project
     * @param p
     * @return
     */
    boolean hasArchitectPermission(Project p) {
        return aclUtilService.hasPermission(springSecurityService.authentication, p, YanaPermission.ARCHITECT) || aclUtilService.hasPermission(springSecurityService.authentication, p, YanaPermission.ADMINISTRATION)
    }
    /**
     * Return true if the user has Architect permission for the project,
     * but fail if not
     * @param p
     * @return
     */
    @PreAuthorize("hasPermission(#p, 'architect') or hasPermission(#p, admin)")
    boolean authorizedArchitectPermission(Project p) {
        return aclUtilService.hasPermission(springSecurityService.authentication, p, YanaPermission.ARCHITECT) || aclUtilService.hasPermission(springSecurityService.authentication, p, YanaPermission.ADMINISTRATION)
    }

    /**
     * Return true if the user has User permission for the project
     * @param p
     * @return
     */
    boolean hasOperatorPermission(Project p) {
        return aclUtilService.hasPermission(springSecurityService.authentication, p, YanaPermission.OPERATOR) || aclUtilService.hasPermission(springSecurityService.authentication, p, YanaPermission.ADMINISTRATION)
    }
    /**
     * Return true if the user has User permission for the project,
     * but fail if not
     * @param p
     * @return
     */
    @PreAuthorize("hasPermission(#p, 'operator') or hasPermission(#p, admin)")
    boolean authorizedOperatorPermission(Project p) {
        return aclUtilService.hasPermission(springSecurityService.authentication, p, YanaPermission.OPERATOR) || aclUtilService.hasPermission(springSecurityService.authentication, p, YanaPermission.ADMINISTRATION)
    }

    /**
     * Return true if the user has User permission for the project
     * @param p
     * @return
     */
    boolean hasReadPermission(Project p) {
        return aclUtilService.hasPermission(springSecurityService.authentication, p, YanaPermission.READ) || aclUtilService.hasPermission(springSecurityService.authentication, p, YanaPermission.ADMINISTRATION)
    }
    /**
     * Return true if the user has User permission for the project,
     * but fail if not
     * @param p
     * @return
     */
    @PreAuthorize("hasPermission(#p, read) or hasPermission(#p, admin)")
    boolean authorizedReadPermission(Project p) {
        return aclUtilService.hasPermission(springSecurityService.authentication, p, YanaPermission.READ) || aclUtilService.hasPermission(springSecurityService.authentication, p, YanaPermission.ADMINISTRATION)
    }

    @PreAuthorize("hasPermission(#p, delete) or hasPermission(#p, admin)")
    def deleteProject(Project p){
        def projectName=p.name
        def result=[success:false]
        Project.withTransaction{ TransactionStatus status->
            //delete all Nodes, NodeTypes, Attribute, Filter
            try {
                ChildNode.executeUpdate("delete ChildNode N where exists ( from Node P where N.parent = P and P.project = :project )", [project:p])
                NodeValue.executeUpdate("delete NodeValue N where exists ( from Node P where N.node = P and P.project = :project )", [project:p])
                Node.executeUpdate("delete from Node as N where N.project = :project ", [project: p])
                NodeAttribute.executeUpdate("delete NodeAttribute N where exists ( from NodeType P where N.nodetype = P and P.project = :project )", [project: p])
                NodeTypeRelationship.executeUpdate("delete NodeTypeRelationship N where exists ( from NodeType P where N.parent = P and P.project = :project )", [project: p])
                NodeType.executeUpdate("delete from NodeType as N where N.project = :project ", [project: p])
                Attribute.executeUpdate("delete from Attribute as N where N.project = :project ", [project: p])
                Filter.executeUpdate("delete from Filter as N where N.project = :project ", [project: p])

                // Delete the ACL information as well
                aclUtilService.deleteAcl p

                p.delete(flush: true)
                result = [success: true]
            }catch(Exception e){
                status.setRollbackOnly()
                log.error("Failed to delete project ${projectName}",e)
                result = [error: "Failed to delete project ${projectName}: ${e}",success: false]

            }
        }
        return result
    }

    @PostFilter("hasPermission(filterObject, read) or hasPermission(filterObject, admin)")
    def listProjects() {
        return Project.list()
    }

    @PreAuthorize("hasPermission(#project, read) or hasPermission(#project, admin)")
    def userSelectProject(session, Project project) {
        session.project = project.name
    }

    @PreAuthorize("hasRole('ROLE_YANA_ADMIN') or hasRole('ROLE_YANA_SUPERUSER')")
    def createProject(String name, String description) {
        def project = new Project(name: name, description: description)
        if (!project.save()) {
            log.error("Unable to create project: " + project)
        } else {
            Properties defaultFilters = new Properties()
            if (null != servletContext) {
                def instream = servletContext.getResourceAsStream("/properties/default-filters.properties")
                if (instream) {
                    defaultFilters.load(instream)
                }
                defaultFilters.each {String k, String v ->
                    if (!Filter.findByProjectAndDataType(project, k)) {
                        Filter test = new Filter()
                        test.project = project
                        test.dataType = k
                        test.regex = v
                        test.save(failOnError: true, flush: true)
                    }
                }
            }
        }
        //grant admin permissions to this project
        addPermission(project,'ROLE_YANA_SUPERUSER', YanaPermission.ADMINISTRATION)
        addPermission(project,'ROLE_YANA_ADMIN', YanaPermission.ADMINISTRATION)

        //YANA_USER gets OPERATOR and READ access
        addPermission(project,'ROLE_YANA_USER',YanaPermission.OPERATOR)
        addPermission(project,'ROLE_YANA_USER', YanaPermission.READ)

        //YANA_ARCHITECT gets ARCHITECT and READ access
        addPermission(project,'ROLE_YANA_ARCHITECT',YanaPermission.ARCHITECT)
        addPermission(project,'ROLE_YANA_ARCHITECT', YanaPermission.READ)

        return project
    }
}
