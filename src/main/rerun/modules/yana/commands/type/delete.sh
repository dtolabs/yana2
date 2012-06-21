#
# ACTION
#
#   delete
#
# DESCRIPTION
#
#   delete the node type
#


[ -z "$ID" ] && {
    echo "Missing argument: --id <>"
    return 2
}

#
# Look up this type
#
http_code=$(curl -w "%{http_code}" --silent --fail --request GET \
    ${URL}/api/nodeType/xml/$ID --cookie ${cookie} -o $response)
[ "${http_code}" -eq 404 ] && return 0

# Validate the response
xmlstarlet val --well-formed \
    --quiet ${response} 2>/dev/null || rerun_die "Server response failed XML validation"

nodeCount=$(xmlstarlet sel -t -m /nodetypes/nodetype -v @nodeCount  $response)
if [ "${nodeCount}" != "0" ]
then
    echo "Deleting NodeType with associated nodes forbidden. (nodeCount=${nodeCount})"
    exit 1
fi
#
# Find all the TempalateAttributes associated with this Node and delete them.
#
curl --silent --fail \
    --request POST \
    --header "Content-Type: application/json" \
    ${URL}/api/nodeAttribute/list/XML \
    --cookie ${cookie} -o ${response}
# Validate the response
xmlstarlet val --well-formed \
    --quiet ${response} 2>/dev/null || rerun_die "Server response failed XML validation"
# Collect the IDs 
nodeAttributeIDs=$(xmlstarlet sel -t \
    -m /nodeAttributes/nodeAttribute[@nodetypeId=\'$ID\'] \
    -v @id -i 'not(position()=last())' -o "," $response)
if [ -n "$nodeAttributeIDs" ]
then
curl --silent --fail \
    --request DELETE \
    "${URL}/api/nodeAttribute/none/{$nodeAttributeIDs}" \
    --cookie $cookie -o $response || rerun_die "Error deleting associated NodeAttributes"
fi
#
# Send the delete request 
#
http_code=$(curl --silent --fail -w "%{http_code}" --request DELETE \
    ${URL}/api/nodeType/none/${ID} \
    --cookie ${cookie} -o $response) 
case ${http_code} in
    20*|404|410)
	: # Acceptable server responses when type is deleted or did not exist. 
	;;
    400)
	echo "No ID sent"
	exit 1
	;;
    401)
	echo "authorization failure"
	exit 1
	;;
    403)
	echo "Nodes exist for this type"
	exit 1
	;;
    500|*)
	rerun_die "Server error deleting type"
esac
# ------------------------------

return $?

# Done
