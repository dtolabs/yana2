class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/webhook/$controller/$director"(action:"webhook",params:["director",director], parseRequest: true)
		"/api/$controller/$id"(action:"api", parseRequest: true)
		
        "/"(view: "/index")
        "500"(view:'/error')
    }
}
