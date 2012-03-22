package com.dtosolutions

import org.springframework.dao.DataIntegrityViolationException

class SolutionController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [solutionInstanceList: Solution.list(params), solutionInstanceTotal: Solution.count()]
    }

    def create() {
        [solutionInstance: new Solution(params)]
    }

    def save() {
        def solutionInstance = new Solution(params)
        if (!solutionInstance.save(flush: true)) {
            render(view: "create", model: [solutionInstance: solutionInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'solution.label', default: 'Solution'), solutionInstance.id])
        redirect(action: "show", id: solutionInstance.id)
    }

    def show() {
        def solutionInstance = Solution.get(params.id)
        if (!solutionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'solution.label', default: 'Solution'), params.id])
            redirect(action: "list")
            return
        }

        [solutionInstance: solutionInstance]
    }

    def edit() {
        def solutionInstance = Solution.get(params.id)
        if (!solutionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'solution.label', default: 'Solution'), params.id])
            redirect(action: "list")
            return
        }

        [solutionInstance: solutionInstance]
    }

    def update() {
        def solutionInstance = Solution.get(params.id)
        if (!solutionInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'solution.label', default: 'Solution'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (solutionInstance.version > version) {
                solutionInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'solution.label', default: 'Solution')] as Object[],
                          "Another user has updated this Solution while you were editing")
                render(view: "edit", model: [solutionInstance: solutionInstance])
                return
            }
        }

        solutionInstance.properties = params

        if (!solutionInstance.save(flush: true)) {
            render(view: "edit", model: [solutionInstance: solutionInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'solution.label', default: 'Solution'), solutionInstance.id])
        redirect(action: "show", id: solutionInstance.id)
    }

    def delete() {
        def solutionInstance = Solution.get(params.id)
        if (!solutionInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'solution.label', default: 'Solution'), params.id])
            redirect(action: "list")
            return
        }

        try {
            solutionInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'solution.label', default: 'Solution'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'solution.label', default: 'Solution'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
