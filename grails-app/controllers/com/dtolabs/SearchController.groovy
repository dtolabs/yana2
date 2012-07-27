package com.dtolabs

import org.compass.core.engine.SearchEngineQueryParseException
import grails.plugins.springsecurity.Secured
import grails.converters.JSON
import com.dtolabs.Node

@Secured(['ROLE_YANA_ADMIN', 'ROLE_YANA_USER', 'ROLE_YANA_ARCHITECT', 'ROLE_YANA_SUPERUSER'])
class SearchController {

    def iconService
    def springSecurityService
    def searchableService
    def jsonService
    def xmlService

    /**
     * Search Nodes only
     */
    def index = {
        String path = iconService.getSmallIconPath()
        if (!params.q?.trim()) {
            return [:]
        }
        try {
            def results = Node.search( {
                                           must(term("project", params.project))
                                           must(queryString(params.q))
                                       }, params.subMap(['offset', 'max', 'sort', 'order']))
            if (params.format) {


                ArrayList nodes = results.results

                switch (params.format.toLowerCase()) {
                    case 'xml':
                        def xml = xmlService.formatNodes(nodes)
                        render(text: xml, contentType: "text/xml")
                        break;
                    case 'json':
                        def json = jsonService.formatNodes(nodes)
                        render(text: json, contentType: "text/json")
                        break;
                }
            } else {
                return [searchResult: results, path: path]
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
