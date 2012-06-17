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
# Login and create a session
#
curl --silent --fail \
    --data "j_username=admin&j_password=admin" \
    "${URL}/springSecurityApp/j_spring_security_check" \
    --cookie-jar ${cookie} || rerun_die "server login failed"

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
