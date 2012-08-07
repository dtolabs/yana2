package yana2

import com.dtolabs.Project

import org.codehaus.groovy.grails.commons.GrailsClass

import com.dtolabs.yana2.springacl.ProjectAccess
import com.dtolabs.yana2.springacl.DefaultProjectAccess

class ProjectFilters {
    def projectService
    def messageSource

    def filters = {
        all(controller:'*', controllerExclude: 'project|login|logout|errors', action:'*') {
            before = {
                if(!session.project && !params.project){
                    if (actionName =~ /^.*api$/) {
                        response.status=406
                        render(text: "Project parameter is required")
                        return false
                    }else{
                        redirect(controller: 'project', action: 'list', params: ['mustChoose': 1])
                        return false
                    }
                }else if(!params.project){
                    params.project=session.project
                }else if(!session.project && !(actionName =~ /.*api$/)) {
                    //only set session if not api call
                }
            }
        }
        projectAccessPermission(controller: '*', controllerExclude: 'project|login|logout|errors', action: '*') {
            before={
                if(params.project){
                    //default project access level, if undeclared in the controller is 'read'
                    def GrailsClass ctype = grailsApplication.getArtefactByLogicalPropertyName('Controller', controllerName)
                    def ProjectAccess.Level alevel = ctype.getClazz().getAnnotation(DefaultProjectAccess)?.value()

                    if (!alevel) {
                        return true
                    }

                    def Project project = projectService.findProject(params.project)
                    if (!project) {
                        response.status = 404
                        request.message=messageSource.getMessage('default.not.found.message',
                                                              ['Project', params.project] as Object[],
                                                              "Project {0} was not found", request.locale)
                        render(view:'/errors/error404')
                        return false
                    }


                    //look for method annotation
                    def cmethod=ctype.getClazz().getDeclaredMethod(actionName)
                    if(cmethod && cmethod.getAnnotation(ProjectAccess)){
                        alevel= cmethod.getAnnotation(ProjectAccess).value()
                    }

                    if(alevel){
                        switch(alevel){
                            case ProjectAccess.Level.architect:
                                if (!projectService.authorizedArchitectPermission(project)) {
                                    return false
                                }
                                break;
                            case ProjectAccess.Level.operator:
                                if (!projectService.authorizedOperatorPermission(project)) {
                                    return false
                                }
                                break;
                        }
                    }
                }
            }
        }
    }
}
