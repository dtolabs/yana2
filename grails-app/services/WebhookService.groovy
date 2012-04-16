package com.dtosolutions

class WebhookService {

    static transactional = false
    static scope = "prototype"
    
    def postToURL(String hookurl, String postData) { 
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

		def recaptchaResponse = connection.content.text
		log.debug(recaptchaResponse)

		recaptchaResponse.startsWith("true")
	}
}
