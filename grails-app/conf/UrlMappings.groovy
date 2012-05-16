class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/api/$controller/$format"(action:"api", parseRequest: true)
		"/api/$controller/$format/$id"(action:"api", parseRequest: true)

		"/api/webhook/$format/$id"(controller:"webhook",action:"api", parseRequest: true)
		"/api/webhook/$format"(controller:"webhook",action:"api", parseRequest: true)
		
		"/"(controller:"node",action:"list")
        "500"(view:'/error')
    }
}
