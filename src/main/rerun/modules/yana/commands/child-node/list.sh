#
# ACTION
#
#   list
#
# DESCRIPTION
#
#   list the child nodes
#

curl --silent --fail --request POST \
    --header "Content-Type: application/json" \
    ${YANA_URL}/api/childNode/list/xml \
    -o $response --cookie $cookie 
xmlstarlet val --well-formed \
    --quiet ${response} 2>/dev/null || rerun_die "Yana response failed XML validation"
xmlstarlet sel -t -m /childNodes/childNode \
    -v @relationshipName -o ":" -v @id -o ":" \
    -v @parentName -o ":" -v @parentNodeId -o ":" \
    -v @childName  -o ":" -v @childNodeId \
    -n  $response || rerun_die "failed parsing result"

# ------------------------------

return $?

# Done
