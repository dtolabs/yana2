#!/bin/bash
#
# NAME
#
#   export
#
# DESCRIPTION
#
#   export the yana model
#

# Read module function library
source $RERUN_MODULES/yana/lib/functions.sh || exit 1 ;

#
# Initialize the context
#
yana_initialize $CFG || rerun_die "Yana initialization failed"

# Parse the command options
[ -r $RERUN_MODULES/yana/commands/export/options.sh ] && {
  source $RERUN_MODULES/yana/commands/export/options.sh || exit 2 ;
}


# ------------------------------

cookie=/tmp/yana-export-cookiejar.txt
response=/tmp/yana-export-response.txt
[ -f $response ] && rm $response


#
# Login and create a session
#
yana_authenticate $YANA_URL $YANA_USER $YANA_PASSWORD ${cookie} || rerun_die "Yana authentication failed"

#
# Export the file
#

curl --fail --silent \
    "${YANA_URL}/export/xml?project=${PROJECT}" \
    --cookie ${cookie} -o ${response} || rerun_die "failed loading data to server"

#
# Print it to stdout
#

cat ${response} > ${FILE} || rerun_die "failed writing to file: $FILE"

# ------------------------------

exit $?

# Done
