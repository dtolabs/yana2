#
# ACTION
#
#   get
#
# DESCRIPTION
#
#   get a child node
#

curl --silent --fail --request GET \
    ${YANA_URL}/api/childNode/xml/${ID} \
    -o $response --cookie $cookie

[ $? == 0 -a ! -f $response ] && rerun_die "ChildNode not found. (id: ${ID})"

xmlstarlet val --well-formed \
    --quiet ${response} 2>/dev/null || rerun_die "Yana response failed XML validation"
xmlstarlet sel -t -m /childNodes/childNode \
    -v @relationshipName -o ":" -v @id -o ":" \
    -v @parentName -o ":" -v @parentNodeId -o ":" \
    -v @childName  -o ":" -v @childNodeId \
    $response || rerun_die "failed parsing result"

# ------------------------------

return $?

# Done
