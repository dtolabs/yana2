#
# ACTION
#
#   list
#
# DESCRIPTION
#
#   list all relations
#

curl --silent --fail --request POST \
    --header "Content-Type: application/json" \
    ${YANA_URL}/api/nodeTypeRelationship/list/xml \
    -o $response --cookie $cookie


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
	    -v @childNodeTypeId  -o ":" -v @childNodeTypeName -o ":" \
		-n \
    $response | format ) || rerun_die "failed parsing server response"

# ------------------------------

return $?

# Done
