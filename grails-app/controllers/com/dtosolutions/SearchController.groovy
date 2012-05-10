package com.dtosolutions

import org.compass.core.engine.SearchEngineQueryParseException
import grails.plugins.springsecurity.Secured
import grails.converters.JSON

@Secured(['ROLE_YANA_ADMIN','ROLE_YANA_USER','ROLE_YANA_ARCHITECT','ROLE_YANA_SUPERUSER'])
class SearchController {
	
	def springSecurityService
    def searchableService
	def xmlService

    /**
     * Index page with search form and results
     */
    def index = {
        if (!params.q?.trim()) {
            return [:]
        }
        try {
			if(params.format){
				
				
				def results = searchableService.search(params.q, params)
				ArrayList nodes = []
				results.results.each{ val1 ->
					nodes.putAt(nodes.size(), Node.get(val1.id.toLong()))
				}
				
				
				switch(params.format.toLowerCase()){
						case 'xml':
							def xml = xmlService.formatNodes(nodes)
							render(text: xml, contentType: "text/xml")
							break;
						case 'json':
							def json = jsonService.formatNodes(nodes)
							render(text:json, contentType: "text/json")
							break;
				}
			}else{
            	return [searchResult: searchableService.search(params.q, params)]
			}
        } catch (SearchEngineQueryParseException ex) {
            return [parseException: true]
        }
    }

    /**
     * Perform a bulk index of every searchable object in the database
     */
    def indexAll = {
        Thread.start {
            searchableService.index()
        }
        render("bulk index started in a background thread")
    }

    /**
     * Perform a bulk index of every searchable object in the database
     */
    def unindexAll = {
        searchableService.unindex()
        render("unindexAll done")
    }
}
