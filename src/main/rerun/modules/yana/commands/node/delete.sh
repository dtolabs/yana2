#
# ACTION
#
#   delete
#
# DESCRIPTION
#
#   delete a node
#

#
# Send the delete request 
#

http_code=$(curl -w "%{http_code}" --silent --fail --request GET \
    ${URL}/api/node/xml/$ID --cookie ${cookie} -o $response)
[ "${http_code}" -eq 404 ] && return 0


# ------------------------------

return $?

# Done
