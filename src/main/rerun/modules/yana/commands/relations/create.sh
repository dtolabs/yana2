#
# ACTION
#
#   create
#
# DESCRIPTION
#
#   create a new relation
#

http_code=$(curl -w "%{http_code}" --silent --fail --request POST \
    --header "Content-Type: application/json" \
    -d "{relationshipName:'${NAME}',parent:'${NODE}',child:'${CHILD}'}" \
    ${YANA_URL}/api/childNode/xml \
    -o $response --cookie $cookie )

format() {
    while read line
    do
	IFS=:
	arr=( $line )
	[ ${#arr[*]} -eq 6 ] || continue
	IFS=$oIFS
	yana_expand "$FORMAT" NAME=${arr[0]} ID=${arr[1]} PARENT_NAME=${arr[2]} PARENT_ID=${arr[3]} CHILD_NAME=${arr[4]} CHILD_ID=${arr[5]}
    done 
}


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
	(xmlstarlet sel -t -m /childNodes/childNode \
	    -v @relationshipName -o ":" -v @id -o ":" \
	    -v @parentName -o ":" -v @parentNodeId -o ":" \
	    -v @childName  -o ":" -v @childNodeId \
	    $response | format ) || rerun_die "failed parsing server response"
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
