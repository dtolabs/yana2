#
# ACTION
#
#   create
#
# DESCRIPTION
#
#   create a new type relation
#

http_code=$(curl -w "%{http_code}" --silent --fail --request POST \
    --header "Content-Type: application/json" \
    -d "{roleName:'${NAME}', parent.id:'${PARENT}', child.id: '${CHILD}' }" \
    ${YANA_URL}/api/nodeTypeRelationship/xml?project=${PROJECT} \
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
	(xmlstarlet sel -t -m /yana/relationships/relationship \
	    -v @id  -o ":" -v @name -o ":" \
	    -v @parent -o ":"  -v @child -n \
	    $response  ) || rerun_die "failed parsing server response"
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
