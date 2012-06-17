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
# Login and create a session
#
curl --fail --silent \
    --data "j_username=admin&j_password=admin" \
    ${URL}/springSecurityApp/j_spring_security_check \
    --cookie-jar ${cookie} || rerun_die "login failed for admin"
#
# Import the file
#

curl --fail --silent \
    --form yanaimport=@${FILE} \
    "${URL}/import/savexml" \
    --cookie ${cookie} -o ${response} || rerun_die "failed loading data to server"
# ------------------------------

exit $?

# Done
