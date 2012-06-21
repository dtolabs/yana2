#
# ACTION
#
#   get
#
# DESCRIPTION
#
#   get the node type
#


#
# Send the types request 
#

if [ -n "$TYPE" -a -z "$ID" ]
then
    curl --silent --fail \
	--request POST --header "Content-Type: application/json" \
	${URL}/api/nodeType/list/xml --cookie ${cookie} -o ${response}
elif [ -z "$TYPE" -a -n "$ID" ]
then
    http_code=$(curl --silent --fail \
	-w "%{http_code}" \
	--request GET \
	"${URL}/api/nodeType/xml/$ID" --cookie ${cookie} -o ${response})
    #
    # Exit with an error if the type does not exist
    #
    [[ "${http_code}" =~ 404 ]] && rerun_die "NodeType not found with that id: ${ID}"
else 
    echo "Choose --type or --id"
    return 2
fi
#
# Validate the response is well formed XML
#
xmlstarlet val --well-formed --quiet ${response} 2>/dev/null || rerun_die "Server response failed XML validation"

#
# Look up the type name if addressed by ID.
#
[ -z "$TYPE" ] &&  TYPE=$(xmlstarlet sel -t -m /nodetypes/nodetype -v @name $response)


#
# Output the data
#
xmlstarlet sel -t -m /nodetypes/nodetype[@name=\'"${TYPE}"\'] \
    -o "name:" -v @name -n \
    -o "description:" -v @description -n \
    -o "id:" -v @id  $response
printf "%s" "attributes:"
xmlstarlet sel -t -m /nodetypes/nodetype[@name=\'"${TYPE}"\']/nodeAttributes/nodeAttribute \
    -v @attributeName -i 'not(position()=last())' -o "," \
    $response
printf "%s" "relationships:"
xmlstarlet sel -t -m /nodetypes/nodetype[@name=\'"${TYPE}"\']/nodetypeRelationships/nodetypeRelationship \
    -v @roleName -i 'not(position()=last())' -o "," \
    $response



# ------------------------------

return $?

# Done
