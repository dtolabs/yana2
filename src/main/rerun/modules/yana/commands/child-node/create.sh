#
# ACTION
#
#   create
#
# DESCRIPTION
#
#   create a new child node
#

http_code=$(curl -w "%{http_code}" --silent --fail --request POST \
    --header "Content-Type: application/json" \
    -d "{relationshipName:'${NAME}',parent:'${ID}',child:'${CHILD}'}" \
    ${YANA_URL}/api/childNode/xml \
    -o $response --cookie $cookie )

case $http_code in
    200)
	xmlstarlet val --well-formed \
	    --quiet ${response} 2>/dev/null || rerun_die "Yana response failed XML validation"
	xmlstarlet sel -t -m /childNodes/childNode \
	    -v @relationshipName -o ":" -v @id -o ":" \
	    -v @parentName -o ":" -v @parentNodeId -o ":" \
	    -v @childName  -o ":" -v @childNodeId \
	    $response || rerun_die "failed parsing result"
	;;
    404)
	rerun_die "child-node action not permitted for node id: $ID"
	;;
    500)
	rerun_die "server request failed"
	;;
esac

# ------------------------------

return $?

# Done
