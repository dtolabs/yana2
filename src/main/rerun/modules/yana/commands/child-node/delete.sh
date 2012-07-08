#
# ACTION
#
#   delete
#
# DESCRIPTION
#
#   delete a child node
#

curl --silent --fail --request DELETE \
    ${YANA_URL}/api/childNode/none/${ID} \
    -o ${response} --cookie ${cookie}

# ------------------------------

return $?

# Done

