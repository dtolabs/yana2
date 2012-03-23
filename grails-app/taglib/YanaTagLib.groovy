package com.dtosolutions

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
		def output = "<div style='width:600px;font:12px Helvetica, Arial, sans-serif;color:#50575c;padding:0px 5px 3px 5px;'>"
		def count = 0

		if(crumbs!=null){
			crumbs.each{

				String link = (String)it.link
				def links = link.split("[/]")
				if(links[0]!='index' && !(links[0].empty && links[1]=='index')){
					if(count > 0){
						output += " > "
					}
					output += (it.name==actionName || (count+1)==crumbs.size())?"""<div style="color:#ff6600;display:inline;">${it.name}</div>""":"""<a href="${createLink(controller:links[0],action:links[1])}">${it.name}</a>"""
					++count
				}
			}
		}else{
			output += """<a href="${createLink(controller:controllerName,action:'index')}">${controllerName}</a> > """
			output += """<div style="color:#ff6600;display:inline">${actionName}</div>"""
		}
		
		output += "</div>"

		if(!output.contains('null')){
			out << body(output)
		}
	}
}
