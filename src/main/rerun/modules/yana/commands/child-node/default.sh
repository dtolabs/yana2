#!/bin/bash
#
# NAME
#
#   child-node
#
# DESCRIPTION
#
#   manage a child node relationship
#

# Read module function library
source $RERUN_MODULES/yana/lib/functions.sh || exit 1 ;

# Parse the command options
[ -r $RERUN_MODULES/yana/commands/child-node/options.sh ] && {
  source $RERUN_MODULES/yana/commands/child-node/options.sh || exit 2 ;
}


# ------------------------------

cookie=/tmp/yana-child-node-cookiejar.txt
response=/tmp/yana-child-node-response.txt
[ -f $response ] && rm $response


#
# Initialize the context
#
yana_initialize $CFG || rerun_die "Yana initialization failed"

#
# Login and create a session
#
yana_authenticate $YANA_URL $YANA_USER $YANA_PASSWORD ${cookie} || rerun_die "Yana authentication failed"

case $ACTION in
    create)
	[ -z "$CHILD" ] && { echo "missing required option: --child <id>" ; exit 2 ; }
	source $RERUN_MODULES/yana/commands/child-node/create.sh
	;;
    get)
	[ -z "$ID" ] && { echo "missing required option: --id" ; exit 2 ; }
	source $RERUN_MODULES/yana/commands/child-node/get.sh
	;;
    list)
	source $RERUN_MODULES/yana/commands/child-node/list.sh
	;;
   delete)
	[ -z "$ID" ] && { echo "missing required option: --id" ; exit 2 ; }
	source $RERUN_MODULES/yana/commands/child-node/delete.sh
	;;
    *)
	echo actions: $RANGE
	exit 2
	;;
esac


# ------------------------------

exit $?

# Done
