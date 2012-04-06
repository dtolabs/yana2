package com.dtosolutions

import org.compass.core.engine.SearchEngineQueryParseException
import groovy.xml.MarkupBuilder
import org.custommonkey.xmlunit.*

class SearchController {
    def searchableService

    /**
     * Index page with search form and results
     */
    def index = {
        if (!params.q?.trim()) {
            return [:]
        }
        try {
			if(params.format){
				def writer = new StringWriter()
				def xml = new MarkupBuilder(writer)
				def results = searchableService.search(params.q, params)
				xml.nodes() {
				results.results.each{ val1 ->
					if(val1.class==Node){
						def nd = Node.get(val1.id.toLong())
						def attributequery = "select new map(TV.value as value,A.name as attribute,TA.required as required) from TemplateValue as TV left join TV.node as N left join TV.templateattribute as TA left join TA.attribute as A where N.id=${val1.id.toLong()}"
						def values = TemplateValue.executeQuery(attributequery)
						
						node(id:nd.id,name:nd.name,type:nd.nodetype.name,tags:nd.tags){
							description(nd.description)
							attributes(){
								values.each{ val2 ->
									attribute(name:val2.attribute,value:val2.value,required:val2.required)
								}
							}
						}
					}
				}
				}
				render(text: writer.toString(), contentType: "text/xml")
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
