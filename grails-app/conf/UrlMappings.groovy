class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/api/$controller/$id"(action:"api", parseRequest: true)
		"/webhook/$controller/"(action:"webhook", parseRequest: true)
		
		"/"(controller:"node",action:"list")
        "500"(view:'/error')
    }
}
