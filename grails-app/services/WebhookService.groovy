package com.dtosolutions

import grails.converters.JSON

class WebhookService {

	def xmlService
	
    static transactional = false
    static scope = "prototype"

    def postToURL(String service, ArrayList data) { 

		def hooks = Webhook.findAll("from Webhook where service='${service}' and attempts<5")

		hooks.each { hook ->
			def conn = hook.url.toURL().openConnection()
			conn.setRequestMethod("POST")
			conn.doOutput = true
			def queryString = []
			if(hook.format.toLowerCase()=='xml'){
				queryString << "data=[${xmlService.formatNodes(data).toString()}]"
			}else if(hook.format.toLowerCase()=='json'){
				queryString << "data=[${data.encodeAsJSON()}]"
			}
			def writer = new OutputStreamWriter(conn.outputStream)
			writer.write(queryString)
			writer.flush()
			writer.close()
			conn.connect()
			println conn.content.text

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

}
