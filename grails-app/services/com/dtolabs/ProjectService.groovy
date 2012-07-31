package com.dtolabs

import org.springframework.transaction.TransactionStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.access.prepost.PostFilter

import org.springframework.security.acls.model.Permission
import com.dtolabs.yana2.springacl.YanaPermission

class ProjectService {
    static transactional = true

    def servletContext

    def objectIdentityRetrievalStrategy
    def aclPermissionFactory
    def aclService
    def aclUtilService
    def springSecurityService

    void addPermission(Project project, String username, int permission) {
        addPermission project, username, aclPermissionFactory.buildFromMask(permission)
    }

    @PreAuthorize("hasPermission(#project, admin)")
    void addPermission(Project project, String username, Permission permission) {
        aclUtilService.addPermission project, username, permission
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
     * Return true if the user has User permission for the project
     * @param p
     * @return
     */
    boolean hasOperatorPermission(Project p) {
        return aclUtilService.hasPermission(springSecurityService.authentication, p, YanaPermission.OPERATOR) || aclUtilService.hasPermission(springSecurityService.authentication, p, YanaPermission.ADMINISTRATION)
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
                p.delete(flush: true)
                // Delete the ACL information as well
                aclUtilService.deleteAcl p
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

    @PreAuthorize("hasPermission(#p, read) or hasPermission(#p, admin)")
    def userSelectProject(session, Project p) {
        session.project = p.name
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
        addPermission(project,'ROLE_YANA_USER',YanaPermission.OPERATOR)
        addPermission(project,'ROLE_YANA_USER', YanaPermission.READ)
        addPermission(project,'ROLE_YANA_ARCHITECT', YanaPermission.READ)
        addPermission(project,'ROLE_YANA_ARCHITECT',YanaPermission.ARCHITECT)

        return project
    }
}
