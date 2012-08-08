package com.dtolabs

import grails.plugins.springsecurity.Secured
import com.dtolabs.yana2.springacl.YanaPermission

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
            flash.message = message(code: 'parameter.missing', args: ['project'], default: 'Parameter {0} is required')
            return redirect(action: 'list')
        }
        Project p = projectService.findProject(params.project)
        if(!p){
            response.status=404
            request.message = message(code: 'default.not.found.message',args: [message(code: 'project.label',default: 'Project'),params.project],default: "Project {0} was not found")
            return render(view:'/errors/error404')
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
        Project p = projectService.findProject(params.name)
        if (!p) {
            response.status = 404
            request.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.project], default: "Project {0} was not found")
            return render(view: '/errors/error404')
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

    @Secured(['ROLE_YANA_ADMIN', 'ROLE_YANA_SUPERUSER'])
    def editAdmin(){
        if (!params.name) {
            request.message = message(code: 'parameter.missing', args: ['name'], default: 'Parameter {0} is required')
            return render(view: 'create')
        }
        Project p = projectService.findProject(params.name)
        if (!p) {
            response.status = 404
            request.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.project], default: "Project {0} was not found")
            return render(view: '/errors/error404')
        }

        def acls=projectService.getProjectPermissions(p)
        [acls:acls,project: p]
    }

    @Secured(['ROLE_YANA_ADMIN', 'ROLE_YANA_SUPERUSER'])
    def deleteProjectPermission(){
        if (!params.name) {
            request.message = message(code: 'parameter.missing', args: ['name'], default: 'Parameter {0} is required')
            return render(view: 'create')
        }
        Project p = projectService.findProject(params.name)
        if (!p) {
            response.status = 404
            request.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.project], default: "Project {0} was not found")
            return render(view: '/errors/error404')
        }
        def recipient = params.recipient
        def perm = params.permission
        def grant = params.permissionGrant=='grant'
        def done=projectService.deleteProjectPermission(p,recipient,perm,grant)
        return redirect(action: 'editAdmin',params: [name:params.name]+[deleted:1])
    }
    @Secured(['ROLE_YANA_ADMIN', 'ROLE_YANA_SUPERUSER'])
    def saveProjectPermission(){
        if (!params.name) {
            request.message = message(code: 'parameter.missing', args: ['name'], default: 'Parameter {0} is required')
            return render(view: 'create')
        }
        Project p = projectService.findProject(params.name)
        if (!p) {
            response.status = 404
            request.message = message(code: 'default.not.found.message', args: [message(code: 'project.label', default: 'Project'), params.project], default: "Project {0} was not found")
            return render(view: '/errors/error404')
        }
        def missingParams = ['recipient', 'permission', 'permissionGrant'].findAll{!params[it]}
        if (missingParams) {
            flash.message = message(code: 'parameter.missing', args: missingParams)
            return redirect(action: 'editAdmin', params: [name: params.name])
        }
        def validGrant = ['grant', 'deny']
        if(!(params.permissionGrant in validGrant)){
            flash.message = message(code:'parameter.not.inlist.message',args:['permissionGrant',params.permissionGrant,validGrant.toString()])
            return redirect(action: 'editAdmin', params: [name: params.name])
        }

        def recipient = params.recipient
        def perm = params.permission
        def grant

        if(params.permissionGrant=='grant'){
            grant=true
            projectService.addPermission(p, recipient, perm)
        }else if(params.permissionGrant=='deny'){
            grant=false
            projectService.denyPermission(p, recipient, perm)
        }
        return redirect(action: 'editAdmin', params: params.subMap(['recipient','permission','permissionGrant','name']) + [saved: 1])
    }

}
