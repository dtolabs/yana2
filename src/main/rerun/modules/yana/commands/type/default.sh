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

#
# Initialize the context
#
yana_initialize $CFG || rerun_die "Yana initialization failed"

# Parse the command options
[ -r $RERUN_MODULES/yana/commands/type/options.sh ] && {
  source $RERUN_MODULES/yana/commands/type/options.sh || exit 2 ;
}


# ------------------------------

cookie=/tmp/yana-type-cookiejar.txt
response=/tmp/yana-type-response.txt
[ -f $response ] && rm $response

#
# Login and create a session
#
yana_authenticate $YANA_URL $YANA_USER $YANA_PASSWORD ${cookie} || rerun_die "Yana authentication failed"

#
# Execute specified action
#

case $ACTION in
    attributes)
	source $RERUN_MODULES/yana/commands/type/attributes.sh
	;;
    create)
	[ -z "$TYPE" ] && { echo "missing required option: --type" ; return 2 ; }
	source $RERUN_MODULES/yana/commands/type/create.sh
	;;
    delete)
	source $RERUN_MODULES/yana/commands/type/delete.sh
	;;
    get)
	source $RERUN_MODULES/yana/commands/type/get.sh
	;;
    relations)
	source $RERUN_MODULES/yana/commands/type/relations.sh
	;;
    *)
	echo "Invalid action: $ACTION"
	exit 2
esac

# ------------------------------

exit $?

# Done
