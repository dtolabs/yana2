package com.dtolabs

import org.springframework.dao.DataIntegrityViolationException
import com.dtolabs.Webhook
import com.dtolabs.User
import grails.plugins.springsecurity.Secured
import java.util.Date;

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_USER','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class WebhookController {

	def springSecurityService
	def xmlService
	def jsonService
	def webhookService
	
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
   
	def api(){
		switch(request.method){
			case "POST":
				def json = request.JSON
				this.save()
				break
			case "GET":
				def json = request.JSON
				this.show()
				break
			case "DELETE":
				def json = request.JSON
				if(params.id){
			        def hook = Webhook.get(params.id)
			        if(hook){
			          hook.delete()
					  response.status = 200
					  render "Successfully Deleted."
			        }else{
			          response.status = 404 //Not Found
			          render "${params.id} not found."
			        }
				}else{
					response.status = 400 //Bad Request
					render """DELETE request must include the id"""
				}
				break
		  }
		return
	}
	
    def index() {
        redirect(action: "list", params: params)
    }

    def list() {
		def user = springSecurityService.isLoggedIn() ? User.get(springSecurityService.principal.id) : null
		boolean superuser = 0
		def roleNames = springSecurityService.principal.authorities*.authority
		roleNames.each(){
			if(it=='ROLE_YANA_SUPERUSER' || it=='ROLE_YANA_ADMIN'){
				superuser=1
			}
		} 
		def webhookList = (superuser)?Webhook.list(params):Webhook.findAllByUser(user)

		if(params.format && params.format!='none'){
			switch(params.format.toLowerCase()){
				case 'xml':
					def xml = xmlService.formatHooks(webhookList)
					render(text: xml, contentType: "text/xml")
					break;
				case 'json':
					def json = jsonService.formatHooks(webhookList)
					render(text:json, contentType: "text/json")
					break;
			}
		}
		
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
				webhookInstance = new Webhook(params)
			}else{
				flash.message = "BAD PROTOCOL: URL MUST BE FORMATTED WITH HTTP/HTTPS. PLEASE TRY AGAIN."
				render(view:"create",model:[params:params])
				return
			}
		}else{
			flash.message = "URL EXISTS: PLEASE CHECK YOUR REGISTERED WEBHOOKS TO MAKE SURE THIS IS NOT A DUPLICATE."
			render(view:"create",model:[params:params])
			return
		}
		if (!webhookInstance.save(flush: true)) {
			flash.message = "INVALID/MALFORMED DATA: PLEASE SEE DOCS FOR 'JSON' FORMED STRING AND PLEASE TRY AGAIN."
			render(view:"create",model:[params:params])
			return
		}else{
			flash.message = message(code: 'default.created.message', args: [message(code: 'webhook.label', default: 'Webhook'), webhookInstance.id])
			redirect(action:"show", id: webhookInstance.id)
			return
		}
    }

    def show() {
		def user = springSecurityService.isLoggedIn() ? User.get(springSecurityService.principal.id) : null
		boolean superuser = 0
		def roleNames = springSecurityService.principal.authorities*.authority
		roleNames.each(){
			if(it=='ROLE_YANA_SUPERUSER' || it=='ROLE_YANA_ADMIN'){
				superuser=1
			}
		}
		def webhookInstance = (superuser)?Webhook.findById(params.id.toLong()):Webhook.findByUserAndId(user,params.id.toLong())
		
        if (!webhookInstance) {
			if(params.format && params.format!='none'){
				response.status = 404 //Not Found
				render "Webhook with id [${params.id}] not found."
			}else{
				flash.message = message(code: 'default.not.found.message', args: [message(code: 'webhook.label', default: 'Webhook'), params.id])
				redirect(action: "list")
			}
            return
        }else{
			if(params.format && params.format!='none'){
				ArrayList hooks = [webhookInstance]
				if(webhookInstance){
					switch(params.format.toLowerCase()){
						case 'xml':
							def xml = xmlService.formatHooks(hooks)
							render(text: xml, contentType: "text/xml")
							break;
						case 'json':
							def json = jsonService.formatHooks(hooks)
							render(text:json, contentType: "text/json")
							break;
					}
				}else{
			          response.status = 404 //Not Found
			          render "${params.id} not found."
				}
			}else{
				render(view:"show",model:[webhookInstance: webhookInstance])
				return
			}
		}
    }

    def delete() {
		def user = springSecurityService.isLoggedIn() ? User.get(springSecurityService.principal.id) : null
		boolean superuser = 0
		def roleNames = springSecurityService.principal.authorities*.authority
		roleNames.each(){
			if(it=='ROLE_YANA_SUPERUSER' || it=='ROLE_YANA_ADMIN'){
				superuser=1
			}
		}
		def webhookInstance = (superuser)?Webhook.findById(params.id.toLong()):Webhook.findByUserAndId(user,params.id.toLong())

        if (!webhookInstance) {
			if(params.format && params.format!='none'){
				response.status = 404 //Not Found
				render "Webhook with id [${params.id}] not found."
			}else{
				flash.message = message(code: 'default.not.found.message', args: [message(code: 'webhook.label', default: 'Webhook'), params.id])
	            redirect(action: "list")
			}
            return
        }

        try {
            webhookInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'webhook.label', default: 'Webhook'), params.id])
            redirect(action: "list")
			return
        }catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'webhook.label', default: 'Webhook'), params.id])
            redirect(action: "show", id: params.id)
			return
        }
    }
}
