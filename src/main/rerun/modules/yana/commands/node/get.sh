#
# NAME
#
#   get
#
# DESCRIPTION
#
#   get node info
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
NAME=$(xmlstarlet sel -t -m /nodes/node -v @name $response)
TYPE=$(xmlstarlet sel -t -m /nodes/node -v @type $response)
DESC=$(xmlstarlet sel -t -m /nodes/node -v description $response)
echo "name:$NAME"
echo "type:$TYPE"
printf "description:$DESC"

xmlstarlet sel -t -m /nodes/node/attributes/attribute -v @name -o ":" -v @value -n $response|sort

if [ -n "$CHILDREN" ]
then
    for id in $(xmlstarlet sel -t -m /nodes/node/children/node -v @id $response)
    do
	$RERUN yana:node --id $id
    done
fi
