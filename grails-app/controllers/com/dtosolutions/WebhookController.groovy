package com.dtosolutions

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_USER','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class WebhookController {

	def springSecurityService
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
   
    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
		def user = springSecurityService.isLoggedIn() ? User.get(springSecurityService.principal.id) : null
		boolean superuser = 0
		def roleNames = principal.authorities*.authority
		roleNames.each(){
			if(it=='ROLE_YANA_SUPERUSER'){
				superuser==1
			}
		}
		def webhookList = (superuser==1)?Webhook.list(params):Webhook.findAllByUser(user)
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [webhookInstanceList: webhookList, webhookInstanceTotal: webhookList.size()]
    }

    def create() {
        [webhookInstance: new Webhook(params)]
    }

    def save() {
		println(params)
		def webhookInstance = Webhook.findByUrl(params.url)
		if(!webhookInstance){
			if(!params.service){params.service='node'}
			webhookInstance = new Webhook(params)
		}
		
        if (!webhookInstance.save(flush: true)) {
            render(view: "create", model: [webhookInstance: webhookInstance])
            return
        }

		flash.message = message(code: 'default.created.message', args: [message(code: 'webhook.label', default: 'Webhook'), webhookInstance.id])
        redirect(action: "show", id: webhookInstance.id)
    }

    def show() {
        def webhookInstance = Webhook.get(params.id)
        if (!webhookInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'webhook.label', default: 'Webhook'), params.id])
            redirect(action: "list")
            return
        }

        [webhookInstance: webhookInstance]
    }

    def edit() {
        def webhookInstance = Webhook.get(params.id)
        if (!webhookInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'webhook.label', default: 'Webhook'), params.id])
            redirect(action: "list")
            return
        }

        [webhookInstance: webhookInstance]
    }

    def update() {
        def webhookInstance = Webhook.get(params.id)
        if (!webhookInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'webhook.label', default: 'Webhook'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (webhookInstance.version > version) {
                webhookInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'webhook.label', default: 'Webhook')] as Object[],
                          "Another user has updated this Webhook while you were editing")
                render(view: "edit", model: [webhookInstance: webhookInstance])
                return
            }
        }

        webhookInstance.properties = params

        if (!webhookInstance.save(flush: true)) {
            render(view: "edit", model: [webhookInstance: webhookInstance])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'webhook.label', default: 'Webhook'), webhookInstance.id])
        redirect(action: "show", id: webhookInstance.id)
    }

    def delete() {
        def webhookInstance = Webhook.get(params.id)
        if (!webhookInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'webhook.label', default: 'Webhook'), params.id])
            redirect(action: "list")
            return
        }

        try {
            webhookInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'webhook.label', default: 'Webhook'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'webhook.label', default: 'Webhook'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
