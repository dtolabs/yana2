package com.dtosolutions

import org.springframework.dao.DataIntegrityViolationException
import grails.plugins.springsecurity.Secured
import java.util.Date;

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_USER','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class WebhookController {

	def springSecurityService
	def webhookService
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
   
    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
		def user = springSecurityService.isLoggedIn() ? User.get(springSecurityService.principal.id) : null
		boolean superuser = 0
		def roleNames = principal.authorities*.authority
		roleNames.each(){
			if(it=='ROLE_YANA_SUPERUSER' || it=='ROLE_YANA_ADMIN'){
				superuser==1
			}
		}
		def webhookList = (superuser==1)?Webhook.list(params):Webhook.findAllByUser(user)
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [webhookInstanceList: webhookList, webhookInstanceTotal: webhookList.size()]
    }

    def create() {
		def format = ['XML','JSON']
        [webhookInstance: new Webhook(params),service:grailsApplication.config.webhook.services,format:format]
    }

    def save() {

		Webhook webhookInstance = Webhook.findByUrlAndService(params.url,params.service)
		def protocol = webhookService.checkProtocol(params.url)
		if(!webhookInstance){
			if(protocol){
				def user = springSecurityService.isLoggedIn() ? User.get(springSecurityService.principal.id) : null
				params.user=user
				params.dateCreated= new Date()
				webhookInstance = new Webhook(params)
			}else{
				flash.message = "BAD PROTOCOL: URL MUST BE FORMATTED WITH HTTP/HTTPS. PLEASE TRY AGAIN."
				render(view:"create",model:[params:params])
			}
		}else{
			flash.message = "URL EXISTS: PLEASE CHECK YOUR REGISTERED WEBHOOKS TO MAKE SURE THIS IS NOT A DUPLICATE."
			render(view:"create",model:[params:params])
		}
		if (!webhookInstance.save(flush: true)) {
			flash.message = "INVALID/MALFORMED DATA: PLEASE SEE DOCS FOR 'JSON' FORMED STRING AND PLEASE TRY AGAIN."
			render(view:"create",model:[params:params])
		}else{
			flash.message = message(code: 'default.created.message', args: [message(code: 'webhook.label', default: 'Webhook'), webhookInstance.id])
			redirect(action:"show", id: webhookInstance.id)
		}
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
