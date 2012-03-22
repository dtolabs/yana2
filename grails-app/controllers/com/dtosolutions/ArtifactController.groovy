package com.dtosolutions

import org.springframework.dao.DataIntegrityViolationException

class ArtifactController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [artifactInstanceList: Artifact.list(params), artifactInstanceTotal: Artifact.count()]
    }

    def create() {
        [artifactInstance: new Artifact(params)]
    }

    def save() {
        def artifactInstance = new Artifact(params)
        if (!artifactInstance.save(flush: true)) {
            render(view: "create", model: [artifactInstance: artifactInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'artifact.label', default: 'Artifact'), artifactInstance.id])
        redirect(action: "show", id: artifactInstance.id)
    }

    def show() {
        def artifactInstance = Artifact.get(params.id)
        if (!artifactInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'artifact.label', default: 'Artifact'), params.id])
            redirect(action: "list")
            return
        }

        [artifactInstance: artifactInstance]
    }

    def edit() {
        def artifactInstance = Artifact.get(params.id)
        if (!artifactInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'artifact.label', default: 'Artifact'), params.id])
            redirect(action: "list")
            return
        }

        [artifactInstance: artifactInstance]
    }

    def update() {
        def artifactInstance = Artifact.get(params.id)
        if (!artifactInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'artifact.label', default: 'Artifact'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (artifactInstance.version > version) {
                artifactInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'artifact.label', default: 'Artifact')] as Object[],
                          "Another user has updated this Artifact while you were editing")
                render(view: "edit", model: [artifactInstance: artifactInstance])
                return
            }
        }

        artifactInstance.properties = params

        if (!artifactInstance.save(flush: true)) {
            render(view: "edit", model: [artifactInstance: artifactInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'artifact.label', default: 'Artifact'), artifactInstance.id])
        redirect(action: "show", id: artifactInstance.id)
    }

    def delete() {
        def artifactInstance = Artifact.get(params.id)
        if (!artifactInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'artifact.label', default: 'Artifact'), params.id])
            redirect(action: "list")
            return
        }

        try {
            artifactInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'artifact.label', default: 'Artifact'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'artifact.label', default: 'Artifact'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
