#
# ACTION
#
#   attributes
#
# DESCRIPTION
#
#   list the attributes for this type
#


#
# Send the types request 
#

if [ -n "$TYPE" -a -z "$ID" ]
then
    curl --silent --fail \
	--request POST --header "Content-Type: application/json" \
	${YANA_URL}/api/nodeType/list/xml?project=${PROJECT} --cookie ${cookie} -o ${response}
elif [ -z "$TYPE" -a -n "$ID" ]
then
    http_code=$(curl --silent --fail \
	-w "%{http_code}" \
	--request GET \
	"${YANA_URL}/api/nodeType/xml/$ID?project=${PROJECT}" --cookie ${cookie} -o ${response})
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
# Function to format the output
# 
format() {
    oIFS=$IFS
    while read line
    do
	IFS=:
	arr=( $line )
	[ ${#arr[*]} -eq 2 ] || continue
	IFS=$oIFS
	yana_expand "$FORMAT" ATTRIBUTE=${arr[0]}  VALUE=${arr[1]}
    done 
}

#
# Look up the type name if addressed by ID.
#
[ -z "$TYPE" ] &&  TYPE=$(xmlstarlet sel -t -m /nodetypes/nodetype -v @name $response)


#
# Output the data
#
xmlstarlet sel -t \
    -m /nodetypes/nodetype[@name=\'"${TYPE}"\']/nodeAttributes/nodeAttribute \
    -v @id  -o ":" -v @attributeName -n \
    $response | format




# ------------------------------

return $?

# Done
