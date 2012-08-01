package com.dtolabs.yana2

import grails.plugins.springsecurity.Secured

@Secured(['permitAll'])
class ErrorsController {
    def error404 = {}
}
