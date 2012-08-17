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
    ${YANA_URL}/api/attribute/list/xml?project=${PROJECT} \
    -o $response --cookie $cookie

format() {
    while read line
    do
	IFS=:
	arr=( $line )
	[ ${#arr[*]} -eq 3 ] || continue
	IFS=$oIFS
	yana_expand "$FORMAT" ID=${arr[0]} NAME=${arr[1]} \
	     FILTER_DATATYPE=${arr[2]}
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
(xmlstarlet sel -t -m /yana/attributes/attribute \
     -v @id  -o ":" -v @name -o ":" -v @filter -n \
    $response | format ) || rerun_die "failed parsing server response"

# ------------------------------

return $?

# Done
