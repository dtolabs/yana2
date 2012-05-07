package com.dtosolutions

class WebhookService {

	def xmlService
	
    static transactional = false
    static scope = "prototype"
    
    def postToURL(String service, String postData) { 
		def hooks = Webhook.findByService(service)

		hooks.each(){ hook ->
			
			def queryString
			
			switch(hook.format.toLowerCase()=='xml'){
				case 'xml':
					def xml = xmlService.formatNodes(nodes)
					queryString = "${xml.toString()}"
					break;
				case 'json':
				default:
					queryString = "${postData.encodeAsJSON()}"
			}
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
