package com.dtolabs

import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_USER', 'ROLE_YANA_ARCHITECT', 'ROLE_YANA_ADMIN', 'ROLE_YANA_SUPERUSER'])
class ProjectController {
    static allowedMethods = [delete: 'POST']

    def ProjectService projectService

    def index() {
        redirect(action:'list',params:params)
    }
    def list() {
        if (params.mustChoose) {
            request.message = message(code: 'project.view.list.mustChoose')
        }
        [projects:projectService.listProjects()]
    }
    def select() {
        if(!params.project) {
            request.message = message(code: 'parameter.missing', args: ['project'], default: 'Parameter {0} is required')
            return redirect(action: 'list')
        }
        Project p = projectService.findProject(params.project)
        if(!p){
            response.status=404
            request.message = message(code: 'default.not.found.message',args: [params.project],default: "Project {0} was not found")
            return render(text:request.message)
        }
        projectService.userSelectProject(session,p)
        return redirect(controller: 'node',action: 'index')
    }
    @Secured(['ROLE_YANA_ADMIN','ROLE_YANA_SUPERUSER'])
    def create() {
    }
    def cancel(){
        flash.message = message(code: 'project.action.cancel.message')
        redirect(action: 'list')
    }

    @Secured(['ROLE_YANA_ADMIN', 'ROLE_YANA_SUPERUSER'])
    def save() {
        if (!params.name) {
            request.message = message(code: 'parameter.missing', args: ['name'], default: 'Parameter {0} is required')
            return render(view:'create')
        }
        if(!params.description){
            params.description=""
        }

        def project=projectService.createProject(params.name,params.description)
        if(project.errors.hasErrors()){
            request.errors = project.errors
            return render(view: 'create',model: [project:project])
        }
        projectService.userSelectProject(session, project)


        flash.message = message(code: 'default.created.message', args: ['Project',project.name], default: 'Project {0} created')
        redirect(action: 'list')
    }

    @Secured(['ROLE_YANA_ADMIN', 'ROLE_YANA_SUPERUSER'])
    def delete() {
        if (!params.name) {
            request.message = message(code: 'parameter.missing', args: ['name'], default: 'Parameter {0} is required')
            return render(view: 'create')
        }
        Project p = projectService.findProject(params.project)
        if (!p) {
            response.status = 404
            request.message = message(code: 'default.not.found.message', args: [params.name], default: "Project {0} was not found")
            return render(text: request.message)
        }

        def result = projectService.deleteProject(p)
        if (result.error) {
            flash.error=result.error
            return redirect(controller: 'project',action:'list')
        }
        session.project=null

        flash.message = message(code: 'default.deleted.message', args: ['Project', params.name], default: 'Project {0} deleted')
        redirect(action: 'list')
    }
}
