package com.dtolabs


import grails.plugins.springsecurity.Secured


@Secured(['ROLE_YANA_ADMIN', 'ROLE_YANA_SUPERUSER'])
class ImportController {

    def springSecurityService
    def webhookService
    def projectService
    def importService

    def api() {
        switch (request.method) {
            case "POST":
                this.savexml()
                break
        }
    }

    def index() {
        redirect action: 'importxml'
    }

    def importxml() {
        [projectList: projectService.listProjects()]
    }


    def savexml() {

        def nodes = []

        def project = Project.findByName(params.project)
        if (!project) {
            request.message = message(code: 'default.not.found.message',
                    args: [params.project],
                    default: "Project {0} was not found")

            return redirect(action: 'importxml')
        }

        /**
         * Get the user's file upload.
         */
        def importFile = request.getFile("yanaimport")

        log.debug("importFile name: " + importFile.name
                + ", content-type: " + importFile.contentType
                + ", fileName: " + importFile.originalFilename)

        /**
         * Validate and then populate the project's model
         */
        if (!importFile.empty) {

            try {
                // validate the XML input data
                importService.validate(importFile.inputStream)

                // populate the project
                nodes = importService.populate(
                        importFile.inputStream, project)
                log.info("imported node count: " + nodes.size())
                webhookService.postToURL('node', nodes, 'create')

            } catch (ImportServiceException e) {
                log.error("Error importing document: " + e.message)
                flash.message = "Error importing document: " + e.message
                redirect(action: "importxml")
            }
        } else {
            flash.message = "Import file cannot be empty. Please try again."
            redirect(action: "importxml")
        }
        // return the node list as the model to the view
        [nodes: nodes]
    }
}
