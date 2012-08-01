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
    ${YANA_URL}/api/childNode/list/xml?project=${PROJECT} \
    -o $response --cookie $cookie

format() {
    while read line
    do
	IFS=:
	arr=( $line )
	[ ${#arr[*]} -eq 8 ] || continue
	IFS=$oIFS
	yana_expand "$FORMAT" ID=${arr[0]} RELATIONSHIP=${arr[1]} \
	    PARENT_ID=${arr[2]} PARENT_NAME=${arr[3]} PARENT_TYPE=${arr[4]} \
	    CHILD_ID=${arr[5]} CHILD_NAME=${arr[6]} CHILD_TYPE=${arr[7]} \
	    CHILDNODE=${arr[5]} NAME=${arr[4]}  TYPE=${arr[7]}
    done 
}


#
# validate the response
#

xmlstarlet val --well-formed \
    --quiet ${response} 2>/dev/null || rerun_die "server response failed XML validation"

#
# Output the response
#
(xmlstarlet sel -t -m /childNodes/childNode \
     -v @id  -o ":" -v @relationshipName -o ":" \
     -v @parentNodeId -o ":" -v @parentName -o ":"  -v @parentNodeType -o ":" \
     -v @childNodeId  -o ":" -v @childName  -o ":" -v @childNodeType -n \
    $response | format ) || rerun_die "failed parsing server response"

# ------------------------------

return $?

# Done
