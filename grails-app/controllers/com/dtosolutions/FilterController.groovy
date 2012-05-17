package com.dtosolutions

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class FilterController {

	def springSecurityService
	def iconService
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [filterInstanceList: Filter.list(max:params.max,offset:params.offset,sort:"dataType",order:"asc"), filterInstanceTotal: Filter.count()]
    }

    def create() {
        [filterInstance: new Filter(params)]
    }

    def save() {
        def filterInstance = new Filter(params)
        if (!filterInstance.save(flush: true)) {
            render(view: "create", model: [filterInstance: filterInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'filter.label', default: 'Filter'), filterInstance.id])
        redirect(action: "show", id: filterInstance.id)
    }

    def show() {
		String path = iconService.getLargeIconPath()
        def filterInstance = Filter.get(params.id)
        if (!filterInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'filter.label', default: 'Filter'), params.id])
            redirect(action: "list")
            return
        }

        [filterInstance: filterInstance,path:path]
    }

    def edit() {
        def filterInstance = Filter.get(params.id)
        if (!filterInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'filter.label', default: 'Filter'), params.id])
            redirect(action: "list")
            return
        }

        [filterInstance: filterInstance]
    }

    def update() {
        def filterInstance = Filter.get(params.id)
        if (!filterInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'filter.label', default: 'Filter'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (filterInstance.version > version) {
                filterInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'filter.label', default: 'Filter')] as Object[],
                          "Another user has updated this Filter while you were editing")
                render(view: "edit", model: [filterInstance: filterInstance])
                return
            }
        }

        filterInstance.properties = params

        if (!filterInstance.save(flush: true)) {
            render(view: "edit", model: [filterInstance: filterInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'filter.label', default: 'Filter'), filterInstance.id])
        redirect(action: "show", id: filterInstance.id)
    }

    def delete() {
        def filterInstance = Filter.get(params.id)
        if (!filterInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'filter.label', default: 'Filter'), params.id])
            redirect(action: "list")
            return
        }

        try {
            filterInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'filter.label', default: 'Filter'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'filter.label', default: 'Filter'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
