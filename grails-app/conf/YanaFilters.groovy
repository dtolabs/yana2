import java.text.SimpleDateFormat

//import net.sf.antcontrib.net.httpclient.Params;

class YanaFilters {
	def filters = {
		
		saveValidation(controller:'nodeTypeRelationship', action:'save'){
			before = {
				println(params)
				params.parentCardinality=(params.parentCardinality=='*')?'999999999':params.parentCardinality
				params.childCardinality=(params.childCardinality=='*')?'999999999':params.childCardinality
				println(params)
			}
		}
		
		updateValidation(controller:'nodeTypeRelationship', action:'update'){
			before = {
				println(params)
				params.parentCardinality=(params.parentCardinality=='*')?'999999999':params.parentCardinality
				params.childCardinality=(params.childCardinality=='*')?'999999999':params.childCardinality
				println(params)
			}
		}
	}
}