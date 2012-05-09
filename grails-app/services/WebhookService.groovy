package com.dtosolutions

class WebhookService {

	def xmlService
	
    static transactional = false
    static scope = "prototype"

    def postToURL(String service, ArrayList data) { 

		def hooks = Webhook.findAll("from Webhook where service='${service}' and attempts<5")

		hooks.each { hook ->

			//println("HOOK >> "+hook.url)
			def url="${hook.url}"
			
			//def conn = new URL(url).openConnection()
			def conn = hook.url.toURL().openConnection()
			conn.setRequestMethod("POST")
			conn.doOutput = true
	
			def queryString = "data=yana"
			def writer = new OutputStreamWriter(conn.outputStream)
			writer.write(queryString)
			writer.flush()
			writer.close()
			conn.connect()
			println conn.content.text
			
			//def resp = conn.content.text
			//log.info(resp)
			//conn.disconnect()
		}
	}

}
