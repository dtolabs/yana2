#!/bin/bash
#
# NAME
#
#   type
#
# DESCRIPTION
#
#   show the node type
#

# Read module function library
source $RERUN_MODULES/yana/lib/functions.sh || exit 1 ;

# Parse the command options
[ -r $RERUN_MODULES/yana/commands/type/options.sh ] && {
  source $RERUN_MODULES/yana/commands/type/options.sh || exit 2 ;
}


# ------------------------------

cookie=/tmp/yana-type-cookiejar.txt
response=/tmp/yana-type-response.txt
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
# Execute specified action
#

case $ACTION in
    delete)
	source $RERUN_MODULES/yana/commands/type/delete.sh
	;;
    get)
	source $RERUN_MODULES/yana/commands/type/get.sh
	;;
    *)
	echo "Invalid action: $ACTION"
	exit 2
esac

# ------------------------------

exit $?

# Done
