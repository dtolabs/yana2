package com.dtolabs

import grails.converters.JSON
import com.dtolabs.Node
import com.dtolabs.Webhook

class WebhookService {

	def xmlService
	def jsonService
	
    static transactional = false
    static scope = "prototype"

    def postToURL(String service, ArrayList data, String state) { 


        def c = Webhook.createCriteria()
        def hooks = c {
            eq("service", service)
            //TODO: set attempts max in config.properties so we can override
            between("attempts", 0, 5)
        }
		hooks.each { hook ->
			try{
				String hookData
				switch(hook.format.toLowerCase()){
					case 'xml':
						hookData = xmlService.formatNodes(data).toString()
						break
					case 'json':
					default:
						hookData = jsonService.formatNodes(data)
						break
				}
				def conn = hook.url.toURL().openConnection()
				conn.setRequestMethod("POST")
				conn.doOutput = true
				def queryString = []
				queryString << "state=${state}&data=${hookData}"
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
				response = [xmlService.formatNodes(data).toString()]
				break;
			case 'json':
			default:
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
