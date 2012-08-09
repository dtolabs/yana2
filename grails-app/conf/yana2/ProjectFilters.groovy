package yana2

import com.dtolabs.Project

import org.codehaus.groovy.grails.commons.GrailsClass

import com.dtolabs.yana2.springacl.ProjectAccess
import com.dtolabs.yana2.springacl.DefaultProjectAccess
import com.dtolabs.ProjectService
import org.springframework.context.MessageSource

class ProjectFilters {

    def filters = {
        all(controller:'*', controllerExclude: 'project|login|logout|errors|user|role', action:'*') {
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
    }
}
