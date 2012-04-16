class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/api/$controller/$id"(action:"api", parseRequest: true)
		"/webhook/$controller/"(action:"webhook", parseRequest: true)
		
        "/"(view: "/index")
        "500"(view:'/error')
    }
}
