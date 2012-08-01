package com.dtolabs

import grails.plugins.springsecurity.Secured


@Secured(['ROLE_YANA_ADMIN', 'ROLE_YANA_SUPERUSER'])
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
        render(text: xml, contentType: "text/xml")
    }
}
