#
# ACTION
#
#   get
#
# DESCRIPTION
#
#   get a child node
#

#
# Function to format the output
# 
format() {
    oIFS=$IFS
    while read line
    do
	IFS=:
	arr=( $line )
	[ ${#arr[*]} -eq 6 ] || continue
	IFS=$oIFS
	yana_expand "$FORMAT" NAME=${arr[0]} ID=${arr[1]} PARENT_NAME=${arr[2]} PARENT_ID=${arr[3]} CHILD_NAME=${arr[4]} CHILD_ID=${arr[5]}

    done 
}


curl --silent --fail --request GET \
    ${YANA_URL}/api/childNode/xml/${ID} \
    -o $response --cookie $cookie

[ $? == 0 -a ! -f $response ] && rerun_die "ChildNode not found. (id: ${ID})"

(xmlstarlet val --well-formed \
    --quiet ${response} 2>/dev/null || rerun_die "Yana response failed XML validation"
xmlstarlet sel -t -m /childNodes/childNode \
    -v @relationshipName -o ":" -v @id -o ":" \
    -v @parentName -o ":" -v @parentNodeId -o ":" \
    -v @childName  -o ":" -v @childNodeId \
    $response |format) || rerun_die "failed parsing result"

# ------------------------------

return $?

# Done
