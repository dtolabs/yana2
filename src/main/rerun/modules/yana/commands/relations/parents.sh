#!/bin/bash
#
# ACTION
#
#   parents
#
# DESCRIPTION
#
#   list parent nodes
#

#
# Retrieve the data from Yana
#
curl --fail --silent ${YANA_URL}/node/show/${NODE}?format=xml \
    --cookie ${cookie} -o ${response} || rerun_die "failed obtaining Yana data"


#
# Validate the response is well formed XML
#
xmlstarlet val --well-formed --quiet ${response} 2>/dev/null || die "Yana response failed XML validation"

#
# Output the data
#

if [ -n "$DEPTH" -a "$DEPTH" -gt 0 ]
then
#
# For each parent, print its info, then recurse
#
    for parent in $(xmlstarlet sel -t -m /nodes/node/parents/node \
	-v @childnodeId -o ":" -v @relationshipName -o ":" \
	-v @id  -o ":"  -v @name  -o ":" -v @type  -n $response)
    do
  #
  # Print this parent's info to stdout
  #
	echo $parent | format
  #
  # Parse the parent info to lookup its ID (3rd element) 
  #      ex: environment:myfoobar:3
  #
	arr=( ${parent//:/ } ) # Split on ':' 
	regex="[0-9]+"
	[[ ( ${#arr[*]} -eq 5 ) && ( ${arr[0]} =~ $regex ) ]] || continue ;# bad record
  #
  # ... and recurse another level 
  #
	$RERUN yana:relations --action parents --depth $(( $DEPTH - 1 )) --node ${arr[2]} --format $FORMAT \
	    || rerun_die "Failed recursing through parents"
    done
else
    xmlstarlet sel -t -m /nodes/node/parents/node \
	-v @childnodeId -o ":" -v @relationshipName -o ":" \
	-v @id  -o ":"  -v @name  -o ":" -v @type  -n $response | sort | format
fi

# ------------------------------

exit $?

# Done
