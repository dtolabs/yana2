package com.dtosolutions

class WebhookService {

    static transactional = false
    static scope = "prototype"
    
    def postToURL(String service, String postData) { 
		def hooks = Webhook.findByService(service)

		hooks.each(){ hook ->
			def queryString = "data=${postData}"
			def url = new URL(hookurl)
			def connection = url.openConnection()
			connection.setRequestMethod("POST")
			connection.doOutput = true
	
			def writer = new OutputStreamWriter(connection.outputStream)
			writer.write(queryString)
			writer.flush()
			writer.close()
			connection.connect()
		}
	}
}
