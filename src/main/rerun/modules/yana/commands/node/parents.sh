#
# NAME
#
#   parents
#
# DESCRIPTION
#
#   get parents
#

#
# Retrieve the data from Yana
#
curl --fail --silent ${YANA_URL}/node/show/${ID}?format=xml \
    --cookie ${cookie} -o ${response} || rerun_die "failed obtaining Yana data"


#
# Validate the response is well formed XML
#
xmlstarlet val --well-formed --quiet ${response} 2>/dev/null || die "Yana response failed XML validation"

#
# Output the data
#
xmlstarlet sel -t -m /nodes/node/parents/node -v @type -o ":" -v @name -o ":" -v @id -n $response|sort


# ------------------------------

return $?

# Done
