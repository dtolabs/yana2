package com.dtolabs

import grails.plugins.springsecurity.Secured
import com.dtolabs.yana2.springacl.DefaultProjectAccess
import com.dtolabs.yana2.springacl.ProjectAccess


@Secured(['ROLE_YANA_ADMIN', 'ROLE_YANA_SUPERUSER'])
@DefaultProjectAccess(ProjectAccess.Level.read)
class ExportController {
    def springSecurityService
    def projectService
    def exportService
    def importService

    def api() {
        switch (request.method) {
            case "GET":
                this.xml()
                break
        }
    }

    def index() {
        [projectList: projectService.listProjects()]
    }

    def xml() {

        def project = Project.findByName(params.project)
        if (!project) {
            request.message = message(code: 'default.not.found.message',
                    args: [params.project],
                    default: "Project {0} was not found")

            return redirect(action: 'index')
        }

        def xml = exportService.export(project)
        if ("inline" != params.view) {
            response.setHeader("Content-Disposition", "attachment; filename=\"${project.name.replaceAll('[^a-zA-Z0-9.+_]+','_')}.yana.xml\"")
        }
        render(text: xml, contentType: "text/xml")
    }
}
