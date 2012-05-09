package com.dtosolutions

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
			def queryString = "data=${formatWebhook(hook.format,data)}"
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
				response = xmlService.formatNodes(data).toString()
				break;
			case 'json':
			default:
				response = data.encodeAsJSON()
		}
		return response
	}

}
