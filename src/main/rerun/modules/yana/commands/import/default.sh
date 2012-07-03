#!/bin/bash
#
# NAME
#
#   import
#
# DESCRIPTION
#
#   get import
#

# Read module function library
source $RERUN_MODULES/yana/lib/functions.sh || exit 1 ;

# Parse the command options
[ -r $RERUN_MODULES/yana/commands/import/options.sh ] && {
  source $RERUN_MODULES/yana/commands/import/options.sh || exit 2 ;
}


# ------------------------------

[ -r ${FILE} ] || rerun_die "File not found: ${FILE}"

cookie=/tmp/yana-import-cookiejar.txt
response=/tmp/yana-import-response.txt
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
# Import the file
#

curl --fail --silent \
    --form yanaimport=@${FILE} \
    "${YANA_URL}/import/savexml" \
    --cookie ${cookie} -o ${response} || rerun_die "failed loading data to server"
# ------------------------------

exit $?

# Done
