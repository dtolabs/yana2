package com.dtolabs

import org.springframework.web.context.request.RequestContextHolder

class BreadcrumbService {

    static transactional = false
    static scope = "prototype"
	def grailsApplication
    
    def getCrumb(String controllerName, String actionName) { 
		
		def filepath = grailsApplication.parentContext.getResource("xml/breadcrumbs.xml").file.toString()
		def breadcrumbs = new XmlSlurper().parse(filepath)
		def controller = breadcrumbs.controller.findAll{ it.@name.text() == controllerName }
		
		def breadcrumb = []
		
		def count = 0
		def parent
		def defaultCrumb
		boolean actionFound = false
		
		controller[0].children().each{ it ->
			if(it.@name==actionName){
				actionFound = true
				breadcrumb[count] = (it.@url.isEmpty())? [name:it,link:controllerName+"/"+it.@name]:[name:it,link:it.@url]
				if(it.@parent.isEmpty()){
					defaultCrumb = breadcrumb[count]
					parent = null
				}else{
					parent = it.@parent
				}
				count++
			}else{
				if(actionFound && parent==it.@name){
					breadcrumb[count] = (it.@url.isEmpty())? [name:it,link:controllerName+"/"+it.@name]:[name:it,link:it.@url]	
					if(it.@parent.isEmpty()){
						defaultCrumb = breadcrumb[count]
						parent = null
					}else{
						parent = it.@parent
					}
					count++
				}
			}

			
		}

		if(actionFound==false){ breadcrumb = [];breadcrumb[0] = defaultCrumb }

		use(Collections){ breadcrumb.reverse()}
		if((breadcrumb.size()==1) && (breadcrumb[0]==null)){
			return null
		}else{
			return breadcrumb
		}

    }
}
