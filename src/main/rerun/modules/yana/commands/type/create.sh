#
# ACTION
#
#   create
#
# DESCRIPTION
#
#   create the node type
#

data=
data="$data $(printf "%s:%s," name "$TYPE")"
data="$data $(printf "%s:'%s'," description "$DESCRIPTION")"
data="$data $(printf "%s:'%s'," image "${IMAGE:-none.png}")"

#
# Create the type
#

curl --silent --fail --request POST \
    --header "Content-Type: application/json" \
    -d {"$data"} \
    ${YANA_URL}/api/nodeType/xml --cookie ${cookie} -o ${response}

# Validate the response
xmlstarlet val --well-formed \
    --quiet ${response} 2>/dev/null || rerun_die "Server response failed XML validation"


# ------------------------------

return $?

# Done
