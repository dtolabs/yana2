package com.dtolabs

import org.springframework.transaction.TransactionStatus

class ProjectService {
    static transactional = true

    def servletContext

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
                result = [success: true]
            }catch(Exception e){
                status.setRollbackOnly()
                log.error("Failed to delete project ${projectName}",e)
                result = [error: "Failed to delete project ${projectName}: ${e}",success: false]

            }
        }
        return result
    }

    def listProjects() {
        return Project.list()
    }

    def userSelectProject(session, Project p) {
        session.project = p.name
    }

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
        return project
    }
}
