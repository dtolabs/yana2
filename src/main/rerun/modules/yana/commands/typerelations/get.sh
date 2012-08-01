#
# ACTION
#
#   get
#
# DESCRIPTION
#
#   get type relation
#

http_code=$(curl -w "%{http_code}" --silent --fail --request GET \
    ${YANA_URL}/api/nodeTypeRelationship/xml/${ID}?project=${PROJECT} \
    -o $response --cookie $cookie )


case "$http_code" in
    200)
	#
	# validate the response
	#
	xmlstarlet val --well-formed \
	    --quiet ${response} 2>/dev/null || rerun_die "server response failed XML validation"
	#
	# Output the response
	#
	(xmlstarlet sel -t -m /nodeTypeRelationships/nodeTypeRelationship \
	    -v @id  -o ":" -v @roleName -o ":" \
	    -v @parentNodeTypeId -o ":"  -v @parentNodeTypeName -o ":" \
	    -v @childNodeTypeId  -o ":" -v @childNodeTypeName -o ":" 
		-n \
	    $response |format ) || rerun_die "failed parsing server response"
	;;
    400)
	rerun_die "Relation not permitted. Create appropriate NodeTypeRelationship."
	;;
    404)
	rerun_die "Existing relationship for that parent and child already exists."
	;;
    500)
	rerun_die "Unknown server error"
	;;
esac

# ------------------------------

return $?

# Done
