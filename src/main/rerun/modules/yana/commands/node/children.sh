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
curl --fail --silent ${YANA_URL}/node/show/${ID}?format=xml \
    --cookie ${cookie} -o ${response} || rerun_die "failed obtaining Yana data"


#
# Validate the response is well formed XML
#
xmlstarlet val --well-formed --quiet ${response} 2>/dev/null || rerun_die "Yana response failed XML validation"

#
# Output the data
#
if [ -n "$DEPTH" -a "$DEPTH" -gt 0 ]
then
#
# For each child, print its info, then recurse
#
    for child in $(xmlstarlet sel -t -m /nodes/node/children/node \
	-v @type -o ":" -v @name -o ":" -v @id -n $response)
    do	
	#
	# Print this child's info to stdout -
	#
	echo $child
	#
	# Parse the child info to lookup its ID (3rd element) 
	#      ex: Environment:com.apple.ist.ets.eai.prod:3
	#
	arr=( ${child//:/ } ) # Split on ':' 
	regex="[0-9]+"
	[[ ( ${#arr[*]} -eq 3 ) && ( ${arr[2]} =~ $regex ) ]] || continue ;# bad record
	#
	# ... and recursive another level 
	#
	$RERUN yana:node --action children --depth $(( $DEPTH - 1 )) --id ${arr[2]} \
	    || rerun_die "Failed recursing through children"
    done
else
    xmlstarlet sel -t -m /nodes/node/children/node -v @type -o ":" -v @name -o ":" -v @id  $response|sort
fi

# ------------------------------

return $?

# Done
