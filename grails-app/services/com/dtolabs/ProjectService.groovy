package com.dtolabs

class ProjectService {
    static transactional=true

    def listProjects() {
        return Project.list()
    }

    def userSelectProject(session, Project p){
        session.project = p.name
    }

    def createProject(String name, String description){
        def project = new Project(name: name, description: description)
        if(!project.save()){
            log.error("Unable to create project: "+project)
        }
        return project
    }
}
