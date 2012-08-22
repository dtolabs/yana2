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
import org.springframework.security.acls.model.AccessControlEntry
import org.springframework.security.acls.domain.PermissionFactory
import com.dtolabs.yana2.YanaConstants

class ProjectService {
    static transactional = true

    def servletContext

    def objectIdentityRetrievalStrategy
    def PermissionFactory aclPermissionFactory
    def aclService
    def aclUtilService
    def springSecurityService


    @PreAuthorize("hasPermission(#project, admin) or hasRole('ROLE_YANA_ADMIN') or hasRole('ROLE_YANA_SUPERUSER')")
    void addPermission(Project project, String username, Permission permission) {
        aclUtilService.addPermission project, username, permission
    }

    @PreAuthorize("hasPermission(#project, admin) or hasRole('ROLE_YANA_ADMIN') or hasRole('ROLE_YANA_SUPERUSER')")
    void addPermission(Project project, String username, int permission) {
        addPermission project, username, aclPermissionFactory.buildFromMask(permission)
    }

    @PreAuthorize("hasPermission(#project, admin) or hasRole('ROLE_YANA_ADMIN') or hasRole('ROLE_YANA_SUPERUSER')")
    void addPermission(Project project, String username, String permission) {
        addPermission project, username, YanaPermission.forName(permission)
    }

    /**
     * Deny a permission. Used when you have the instance available.
     *
     * @param domainObject the domain class instance
     * @param recipient the grantee; can be a username, role name, Sid, or Authentication
     * @param permission the permission to grant
     */
    @PreAuthorize("hasPermission(#project, admin) or hasRole('ROLE_YANA_ADMIN') or hasRole('ROLE_YANA_SUPERUSER')")
    void denyPermission(Project project, String recipient, Permission permission) {
        ObjectIdentity oid = objectIdentityRetrievalStrategy.getObjectIdentity(project)
        int_denyPermission oid, recipient, permission
    }

    @PreAuthorize("hasPermission(#project, admin) or hasRole('ROLE_YANA_ADMIN') or hasRole('ROLE_YANA_SUPERUSER')")
    void denyPermission(Project project, String username, int permission) {
        denyPermission project, username, aclPermissionFactory.buildFromMask(permission)
    }

    @PreAuthorize("hasPermission(#project, admin) or hasRole('ROLE_YANA_ADMIN') or hasRole('ROLE_YANA_SUPERUSER')")
    void denyPermission(Project project, String username, String permission) {
        denyPermission project, username, YanaPermission.forName(permission)
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
        addPermission(project, YanaConstants.ROLE_SUPERUSER, YanaPermission.ADMINISTRATION)
        addPermission(project, YanaConstants.ROLE_ADMIN, YanaPermission.ADMINISTRATION)

        //YANA_USER gets OPERATOR and READ access
        addPermission(project, YanaConstants.ROLE_OPERATOR,YanaPermission.OPERATOR)
        addPermission(project, YanaConstants.ROLE_OPERATOR, YanaPermission.READ)

        //YANA_ARCHITECT gets ARCHITECT and READ access
        addPermission(project, YanaConstants.ROLE_ARCHITECT,YanaPermission.ARCHITECT)
        addPermission(project, YanaConstants.ROLE_ARCHITECT, YanaPermission.READ)

        //READONLY gets READ access
        addPermission(project, YanaConstants.ROLE_USER, YanaPermission.READ)

        return project
    }

    /**
     * Return a set of permission maps describing permissions for the project.
     * Each map will define these keys: 'granted' (true/false), 'permission' (name of permission),
     * 'entry' (acl entry object).
     * Additionally it will either define 'role' with a rolename or 'username' with a username.
     * @param project
     * @return list of maps
     */
    @PreAuthorize("hasPermission(#project, admin) or hasRole('ROLE_YANA_ADMIN') or hasRole('ROLE_YANA_SUPERUSER')")
    def List getProjectPermissions(Project project) {
        def acl = aclUtilService.readAcl(project)
        //map acl defs in to a symbolic map
        def aclset=[]
        acl.entries.each { AccessControlEntry entry->
            def map =[:]

            if (entry.sid instanceof GrantedAuthoritySid || entry.sid.respondsTo('instanceOf',[Class.class] as Object[]) && entry.sid.instanceOf(GrantedAuthoritySid)) {
                //role
                map.role = entry.sid.grantedAuthority
            } else if (entry.sid instanceof PrincipalSid || entry.sid.respondsTo('instanceOf', [Class.class] as Object[]) && entry.sid.instanceOf(PrincipalSid)) {
                //principal
                map.username = entry.sid.principal
            }
            map.granted=entry.granting
            map.permission=YanaPermission.nameFor(entry.permission)
            map.entry=entry
            aclset<<map
        }
        return aclset
    }

    /**
     * Removes a granted permission. Used when you don't have the instance available.
     *
     * @param domainClass the domain class
     * @param id the instance id
     * @param recipient the grantee; can be a username, role name, Sid, or Authentication
     * @param permission the permission to remove
     */
    protected void deletePermission(Class<?> domainClass, long id, recipient, Permission permission, boolean granted) {
        Sid sid = createSid(recipient)
        def acl = aclUtilService.readAcl(domainClass, id)

        def todelete=[]
        acl.entries.eachWithIndex { entry, i ->
            if (entry.sid.equals(sid) && entry.permission.equals(permission) && entry.granting==granted) {
                todelete<<i
            }
        }
        if(todelete){
            todelete.sort().reverse().each{i->
                acl.deleteAce i
            }
        }

        aclService.updateAcl acl

        log.debug "Deleted ${domainClass.name}($id) ACL permissions for recipient $recipient"
    }

    @PreAuthorize("hasPermission(#project, admin) or hasRole('ROLE_YANA_ADMIN') or hasRole('ROLE_YANA_SUPERUSER')")
    def boolean deleteProjectPermission(Project project,String recipient, String perm) {
        aclUtilService.deletePermission(project,recipient, YanaPermission.forName(perm))
        return true
    }
    /**
     * Delete a project permission, specifying recipient, permission name string, and whether it is grant or deny.
     * @param project
     * @param recipient
     * @param perm
     * @param granted
     * @return true if successful
     */
    @PreAuthorize("hasPermission(#project, admin) or hasRole('ROLE_YANA_ADMIN') or hasRole('ROLE_YANA_SUPERUSER')")
    def boolean deleteProjectPermission(Project project,String recipient, String perm, boolean granted) {
        deletePermission(Project,project.id,recipient, YanaPermission.forName(perm),granted)
        return true
    }
}
