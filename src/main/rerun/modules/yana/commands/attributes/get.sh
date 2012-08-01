#
# ACTION
#
#   get
#
# DESCRIPTION
#
#   get attribute
#

curl --silent --fail --request GET \
    ${YANA_URL}/api/attribute/xml/${ID}?project=${PROJECT} \
    -o $response --cookie $cookie

format() {
    while read line
    do
	IFS=:
	arr=( $line )
	[ ${#arr[*]} -eq 4 ] || continue
	IFS=$oIFS
	yana_expand "$FORMAT" ID=${arr[0]} NAME=${arr[1]} \
	    FILTER_ID=${arr[2]} FILTER_DATATYPE=${arr[3]}
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
(xmlstarlet sel -t -m /attributes/attribute \
     -v @id  -o ":" -v @name -o ":" \
     -v @filterId  -o ":" -v @filterDataType -n \
    $response | format ) || rerun_die "failed parsing server response"

# ------------------------------

return $?

# Done
