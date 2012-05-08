package com.dtosolutions

class WebhookService {

	def xmlService
	
    static transactional = false
    static scope = "prototype"
    
    def postToURL(String service, ArrayList data) { 
		def hooks = Webhook.findAll("from Webhook where service='${service}' and attempts<5")

		hooks.each(){ hook ->
			
			def queryString
			
			switch(hook.format.toLowerCase()=='xml'){
				case 'xml':
					def xml = xmlService.formatNodes(data)
					queryString = "${xml.toString()}"
					break;
				case 'json':
				default:
					queryString = "${data.encodeAsJSON()}"
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
			log.info "[YANA] WebhookService.postToUrl: ${hook.name} , ${hook.url}"
		}
	}
}
