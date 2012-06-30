#!/bin/bash
#
# NAME
#
#   types
#
# DESCRIPTION
#
#   list the node types
#

# Read module function library
source $RERUN_MODULES/yana/lib/functions.sh || exit 1 ;

# Parse the command options
[ -r $RERUN_MODULES/yana/commands/types/options.sh ] && {
  source $RERUN_MODULES/yana/commands/types/options.sh || exit 2 ;
}


# ------------------------------

cookie=/tmp/yana-types-cookiejar.txt
response=/tmp/yana-types-response.txt
[ -f $response ] && rm $response

#
# Initialize the context
#
yana_initialize $CFG || rerun_die "Yana initialization failed"

#
# Login and create a session
#
yana_authenticate $YANA_URL $YANA_USER $YANA_PASSWORD ${cookie} || rerun_die "Yana authentication failed"

#
# Send the types request 
#

curl --silent --fail \
    --request POST --header "Content-Type: application/json" \
    ${YANA_URL}/api/nodeType/list/xml --cookie ${cookie} -o ${response}

#
# Validate the response is well formed XML
#
xmlstarlet val --well-formed --quiet ${response} 2>/dev/null || rerun_die "Yana response failed XML validation"

#
# Output the data
#

if [ -n "$TYPE" ]
then
xmlstarlet sel -t -m /nodetypes/nodetype[@name=\'"${TYPE}"\'] -v @name -o ":" -v @description -o ":" -v @id -n  $response
else
xmlstarlet sel -t -m /nodetypes/nodetype -v @name -o ":" -v @description -o ":" -v @id  -n  $response
fi

# ------------------------------

exit $?

# Done
