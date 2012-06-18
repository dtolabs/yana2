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
# Login and create a session
#
curl --fail --silent \
    --data "j_username=admin&j_password=admin" \
    ${URL}/springSecurityApp/j_spring_security_check \
    --cookie-jar ${cookie} || rerun_die "login failed for admin"

#
# Execute specified action
#

case $ACTION in
    create)
	source $RERUN_MODULES/yana/commands/node/create.sh
	;;
    children)
	source $RERUN_MODULES/yana/commands/node/children.sh
	;;
    parents)
	source $RERUN_MODULES/yana/commands/node/parents.sh
	;;
    delete)
	source $RERUN_MODULES/yana/commands/node/delete.sh
	;;
    get)
	source $RERUN_MODULES/yana/commands/node/get.sh
	;;
    *)
	echo "Invalid action: $ACTION"
	exit 2
esac


# ------------------------------

exit $?

# Done