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


#
# Initialize the context
#
yana_initialize $CFG || rerun_die "Yana initialization failed"

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
yana_authenticate $YANA_URL $YANA_USER $YANA_PASSWORD ${cookie} || rerun_die "Yana authentication failed"

case $ACTION in
    validate)
	curl --fail --silent \
	    --form yanaimport=@${FILE} \
	    "${YANA_URL}/import/validatexml?project=${PROJECT}" \
	    --cookie ${cookie} -o ${response} || rerun_die "failed loading data to server"

	status=$(xmlstarlet sel -t -m /response/status -v @valid ${response})
	echo valid:$status
	;;
    import)
	curl --fail --silent \
	    --form yanaimport=@${FILE} \
	    "${YANA_URL}/import/savexml?project=${PROJECT}" \
	    --cookie ${cookie} -o ${response} || rerun_die "failed loading data to server"
	;;
    *)
	echo "Invalid action: $ACTION" ; exit 2 
esac
# ------------------------------


exit $?

# Done
