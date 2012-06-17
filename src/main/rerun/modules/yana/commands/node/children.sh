#
# NAME
#
#   children
#
# DESCRIPTION
#
#   get children
#

#
# Retrieve the data from Yana
#
curl --fail --silent ${URL}/node/show/${ID}?format=xml \
    --cookie ${cookie} -o ${response} || rerun_die "failed obtaining Yana data"


#
# Validate the response is well formed XML
#
xmlstarlet val --well-formed --quiet ${response} 2>/dev/null || rerun_die "Yana response failed XML validation"
#
# Output the data
#
xmlstarlet sel -t -m /nodes/node/children/node -v @type -o ":" -v @name -o ":" -v @id -n $response|sort
# ------------------------------

return $?

# Done
