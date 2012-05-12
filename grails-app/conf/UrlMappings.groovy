class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/api/$controller/$format"(action:"api", parseRequest: true)
		"/api/$controller/$format/$id"(action:"api", parseRequest: true)
		"/webhook/$controller/"(action:"webhook", parseRequest: true)
		
		"/"(controller:"node",action:"list")
        "500"(view:'/error')
    }
}
