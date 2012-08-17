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
    ${YANA_URL}/api/nodeTypeRelationship/list/xml?project=${PROJECT} \
    -o $response --cookie $cookie


#
# validate the response
#

xmlstarlet val --well-formed \
    --quiet ${response} 2>/dev/null || rerun_die "server response failed XML validation"

#
# Output the response
#
(xmlstarlet sel -t -m /yana/relationships/relationship \
	    -v @id  -o ":" -v @name -o ":" \
	    -v @parent -o ":" \
	    -v @child -o ":" \
		-n \
    $response | format ) || rerun_die "failed parsing server response"

# ------------------------------

return $?

# Done
