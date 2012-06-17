package com.dtolabs

import java.util.List;
import static groovy.io.FileType.FILES

class IconService {
	
	static transactional = false
	static scope = "prototype"
	
	def grailsApplication
	
	List listImages(String defaultPath, String externalPath){
		List file = []

		def i = 0
		def testpath = new File("${grailsApplication.config.images.icons.large}")
		if(testpath.isDirectory()){
			new File(externalPath).eachFileRecurse(FILES) {
				if(it.name.endsWith('.png')||it.name.endsWith('.gif')) {
					file[i] = it.getName()
					i++
				}
			}
		}else{
			new File(defaultPath).eachFileRecurse(FILES) {
				if(it.name.endsWith('.png')||it.name.endsWith('.gif')) {
					file[i] = it.getName()
					i++
				}
			}
		}

		return file
	}
	
	String getIconPath(){
		def testpath = new File("${grailsApplication.config.images.icons.large}")
		return (testpath.isDirectory())? "${grailsApplication.config.images.icons.large}":"/images/icons/"
	}
	
	String getLargeIconPath(){
		def testpath = new File("${grailsApplication.config.images.icons.large}")
		return (testpath.isDirectory())? "${grailsApplication.config.images.icons.large}":"/images/icons/64/"
	}
	
	String getMedIconPath(){
		def testpath = new File("${grailsApplication.config.images.icons.med}")
		return (testpath.isDirectory())? "${grailsApplication.config.images.isons.med}":"/images/icons/32/"
	}
	
	String getSmallIconPath(){
		def testpath = new File("${grailsApplication.config.images.icons.small}")
		return (testpath.isDirectory())? "${grailsApplication.config.images.icons.small}":"/images/icons/16/"
	}
}
