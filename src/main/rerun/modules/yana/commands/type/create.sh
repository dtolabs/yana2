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
data="$data $(printf "%s:'%s'," image "${IMAGE:-unknown.png}")"

#
# Create the type
#

http_code=$(curl -w "%{http_code}" --silent --fail --request POST \
    --header "Content-Type: application/json" \
    -d {"$data"} \
    ${YANA_URL}/api/nodeType/xml --cookie ${cookie} -o ${response})

case ${http_code} in
    500)
	rerun_die "Internal server error"
	;;
esac


# Validate the response
xmlstarlet val --well-formed \
    --quiet ${response} 2>/dev/null || rerun_die "Server response failed XML validation"


# ------------------------------

return $?

# Done
