#!/bin/bash
#
# NAME
#
#   node
#
# DESCRIPTION
#
#   manage node info
#

# Read module function library
source $RERUN_MODULES/yana/lib/functions.sh || exit 1 ;

# Parse the command options
[ -r $RERUN_MODULES/yana/commands/node/options.sh ] && {
  source $RERUN_MODULES/yana/commands/node/options.sh || exit 2 ;
}


# ------------------------------
flags="" #-v --silent --fail --show-error"

cookie=/tmp/yana-node-cookiejar.txt
response=/tmp/yana-node-response.txt
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
    create)
	[ -z "$TYPEID" ] && { echo "missing required option: --typeid" ; exit 2 ; }
	[ -z "$NAME" ] && { echo "missing required option: --name" ; exit 2 ; }
	source $RERUN_MODULES/yana/commands/node/create.sh
	;;
    children)
	[ -z "$ID" ] && { echo "missing required option: --id" ; exit 2 ; }
	source $RERUN_MODULES/yana/commands/node/children.sh
	;;
    parents)
	[ -z "$ID" ] && { echo "missing required option: --id" ; exit 2 ; }
	source $RERUN_MODULES/yana/commands/node/parents.sh
	;;
    delete)
	[ -z "$ID" ] && { echo "missing required option: --id" ; exit 2 ; }
	source $RERUN_MODULES/yana/commands/node/delete.sh
	;;
    get)
	[ -z "$ID" ] && { echo "missing required option: --id" ; exit 2 ; }
	source $RERUN_MODULES/yana/commands/node/get.sh
	;;
    *)
	echo "Invalid action: $ACTION"
	exit 2
esac


# ------------------------------

exit $?

# Done
