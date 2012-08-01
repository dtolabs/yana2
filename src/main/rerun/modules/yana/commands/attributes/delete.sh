#
# ACTION
#
#   delete
#
# DESCRIPTION
#
#   delete the attribute
#

curl --silent --fail --request DELETE \
    ${YANA_URL}/api/attribute/none/${ID}?project=${PROJECT} \
    -o ${response} --cookie ${cookie}

# ------------------------------

return $?

# Done

