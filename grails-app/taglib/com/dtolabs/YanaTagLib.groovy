package com.dtolabs

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import static java.util.Calendar.*
import java.sql.Timestamp;

class YanaTagLib {

	static namespace = 'dto'
	def breadcrumbService
	def grailsApplication
	
	def breadcrumbs = { attrs,body ->

		def crumbs = breadcrumbService.getCrumb(controllerName,actionName)
		def output = "<div class='breadcrumbs'>"
		def count = 0

		if(crumbs!=null){
			crumbs.each{

				String link = (String)it.link
				def links = link.split("[/]")
				if(links[0]!='index' && !(links[0].empty && links[1]=='index')){
					if(count > 0){
						output += ">"
					}
					output += (it.name==actionName || (count+1)==crumbs.size())?"""<div class="crumbpart">${it.name}</div>""":"""<a href="${createLink(controller:links[0],action:links[1])}">${it.name}</a>"""
					++count
				}
			}
		}else{
			output += """<a href="${createLink(controller:controllerName,action:'index')}">${controllerName}</a> &gt; """
			output += """<div class="crumbpart">${actionName}</div>"""
		}
		
		output += "</div>"

		if(!output.contains('null')){
			out << body(output)
		}
	}
}
