import org.springframework.security.access.AccessDeniedException
import org.springframework.security.acls.model.NotFoundException

class UrlMappings {

    static mappings = {

        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
		
		"/api/$controller/$format"(action:"api", parseRequest: true)
		"/api/$controller/$format/$id"(action:"api", parseRequest: true)
		"/api/$controller/list/$format"(action:"listapi", parseRequest: true)

		"/api/webhook/$format/$id"(controller:"webhook",action:"api", parseRequest: true)
		"/api/webhook/$format"(controller:"webhook",action:"api", parseRequest: true)
		
		
		
		"/"(controller:"node",action:"list")
        "500"(view:'/error')

        "403"(controller: "login", action: "denied")
        "404"(controller: "errors", action: "error404")
        "500"(controller: "login", action: "denied", exception: AccessDeniedException)
        "500"(controller: "login", action: "denied", exception: NotFoundException)
    }
}
