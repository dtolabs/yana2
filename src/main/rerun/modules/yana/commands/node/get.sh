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
    while read line
    do
	IFS=:
	arr=( $line )
	[ ${#arr[*]} -eq 2 ] || continue
	IFS=$oIFS
	yana_expand "$FORMAT" ATTRIBUTE=${arr[0]} VALUE=${arr[1]}
    done 
}


#
# Output the data
#
NAME=$(xmlstarlet sel -t -m /nodes/node -v @name $response)
TYPE=$(xmlstarlet sel -t -m /nodes/node -v @type $response)
DESCRIPTION=$(xmlstarlet sel -t -m /nodes/node -v description $response)
echo "name:$NAME" | format
echo "type:$TYPE" | format
printf "description:$DESCRIPTION" | format

xmlstarlet sel -t -m /nodes/node/attributes/attribute -v @name -o ":" -v @value -n $response|sort|format

#if [ -n "$CHILDREN" ]
#then
#    for id in $(xmlstarlet sel -t -m /nodes/node/children/node -v @id $response)
#    do
#	$RERUN yana:node --id $id
#    done
#fi
