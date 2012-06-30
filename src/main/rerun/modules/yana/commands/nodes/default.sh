#!/bin/bash
#
# NAME
#
#   nodes
#
# DESCRIPTION
#
#   list nodes
#

# Read module function library
source $RERUN_MODULES/yana/lib/functions.sh || exit 1 ;

# Parse the command options
[ -r $RERUN_MODULES/yana/commands/nodes/options.sh ] && {
  source $RERUN_MODULES/yana/commands/nodes/options.sh || exit 2 ;
}


# ------------------------------
flags="" #-v --silent --fail --show-error"

cookie=/tmp/yana-nodes-cookiejar.txt
response=/tmp/yana-nodes-response.txt

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
# Retrieve the data from Yana
#
curl --fail --silent ${YANA_URL}/node/list?format=xml \
    --cookie ${cookie} -o ${response} || rerun_die "failed obtaining Yana data"


#
# Validate the response is well formed XML
#
xmlstarlet val --well-formed --quiet ${response} 2>/dev/null || rerun_die "Yana response failed XML validation"

#
# Output the data
#
if [ -n "$TYPE" ]
then
xmlstarlet sel -t -m /nodes/node[@type=\'"${TYPE}"\'] -v @type -o ":" -v @name -o ":" -v @id -n  $response
else
xmlstarlet sel -t -m /nodes/node -v @type -o ":" -v @name -o ":" -v @id  -n  $response
fi
# ------------------------------

exit $?

# Done
