g#
# ACTION
#
#   create
#
# DESCRIPTION
#
#   create a new attribute
#

http_code=$(curl -w "%{http_code}" --silent --fail --request POST \
    --header "Content-Type: application/json" \
    -d "{name:'${NAME}',description:'${DESCRIPTION}',filter.id:'${FILTER}'}" \
    ${YANA_URL}/api/attribute/xml?project=${PROJECT} \
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
	(xmlstarlet sel -t -m /yana/attributes/attribute \
	    -v @id -o ":" -v @name -o ":" -v @filter \
	    $response | format ) || rerun_die "failed parsing server response"
	;;
    400)
	rerun_die "Not permitted."
	;;
    404)
	rerun_die "Existing attribute already exists."
	;;
    500)
	rerun_die "Unknown server error"
	;;
esac

# ------------------------------

return $?

# Done
