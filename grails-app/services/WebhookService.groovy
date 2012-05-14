package com.dtosolutions

import grails.converters.JSON

class WebhookService {

	def xmlService
	
    static transactional = false
    static scope = "prototype"

    def postToURL(String service, ArrayList data, String state) { 
		def hooks = Webhook.findAll("from Webhook where service='${service}' and attempts<5")
		hooks.each { hook ->
			try{
				def conn = hook.url.toURL().openConnection()
				conn.setRequestMethod("POST")
				conn.doOutput = true
				def queryString = []
				if(hook.format.toLowerCase()=='xml'){
					queryString << "state=${state}&data=[${xmlService.formatNodes(data).toString()}]"
				}else if(hook.format.toLowerCase()=='json'){
					queryString << "state=${state}&data=[${data.encodeAsJSON()}]"
				}
				def writer = new OutputStreamWriter(conn.outputStream)
				writer.write(queryString)
				writer.flush()
				writer.close()
				conn.connect()
				if(conn.content.text!='connected'){
					hook.attempts+=1
					hook.save(flush: true)
				}
			}catch(Exception e){
				hook.attempts+=1
				hook.save(flush: true)
				log.info("[YANA] WebhookService: No Url > ${hook.url} :"+e)
			}

		}
	}
	
	String formatWebhook(String format, ArrayList data){
		String response
		switch(format.toLowerCase()=='xml'){
			case 'xml':
				println("xml")
				response = [xmlService.formatNodes(data).toString()]
				break;
			case 'json':
			default:
				println("json")
				response = data.encodeAsJSON()
		}
		return response
	}

	boolean checkProtocol(String url){
		if(url.size()>=4){
			if(url[0..3]=='http'){
				return true
			}else{
				return false
			}
		}else{
			return false
		}
	}
}
