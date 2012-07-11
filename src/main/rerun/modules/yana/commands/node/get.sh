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

format() {
    regex='^([^:]+):(.*)'
    while read line
    do
	[[ "$line" =~ $regex ]] || continue; #skip bad entries
	yana_expand "$FORMAT" ATTRIBUTE=${BASH_REMATCH[1]} VALUE='${BASH_REMATCH[2]}'
    done 
}


#
# Output the data
#
NAME=$(xmlstarlet sel -t -m /nodes/node -v @name $response)
TYPE=$(xmlstarlet sel -t -m /nodes/node -v @type $response)
DESCRIPTION=$(xmlstarlet sel -t -m /nodes/node -v description $response)
TAGS=$(xmlstarlet sel -t -m /nodes/node -v @tags $response)
echo "name:$NAME" | format
echo "type:$TYPE" | format
echo "description:$DESCRIPTION" | format
echo "tags:$TAGS" | format

xmlstarlet sel -t -m /nodes/node/attributes/attribute -v @name -o ":" -v @value -n $response|sort|format
