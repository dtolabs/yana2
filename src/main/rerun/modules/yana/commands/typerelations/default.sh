#!/bin/bash
#
# NAME
#
#   typerelations
#
# DESCRIPTION
#
#   manage nodetype relations
#

# Read module function library
source $RERUN_MODULES/yana/lib/functions.sh || exit 1 ;

#
# Initialize the context
#
yana_initialize $CFG || rerun_die "Yana initialization failed"

# Parse the command options
[ -r $RERUN_MODULES/yana/commands/typerelations/options.sh ] && {
  source $RERUN_MODULES/yana/commands/typerelations/options.sh || exit 2 ;
}


# ------------------------------
flags="" #-v --silent --fail --show-error"

cookie=/tmp/yana-typerelations-cookiejar.txt
response=/tmp/yana-typerelations-response.txt
[ -f $response ] && rm $response

#
# Login and create a session
#
yana_authenticate $YANA_URL $YANA_USER $YANA_PASSWORD ${cookie} || rerun_die "Yana authentication failed"



#
# Function to format the output
# 
format() {
    oIFS=$IFS
    while read line
    do
	IFS=:
	arr=( $line )
	[ ${#arr[*]} -eq 6 ] || continue
	IFS=$oIFS
	yana_expand "$FORMAT"  ID=${arr[0]} ROLE=${arr[1]} \
	    PARENT_TYPE_ID=${arr[2]} PARENT_TYPE_NAME=${arr[3]} \
	    CHILD_TYPE_ID=${arr[4]} CHILD_TYPE_NAME=${arr[5]} 
    done 
}



#
# Execute specified action
#

case $ACTION in
    list)
	source $RERUN_MODULES/yana/commands/typerelations/list.sh
	;;
    get)
	[ -z "$ID" ] && { echo "missing required option: --id" ; exit 2 ; }
	source $RERUN_MODULES/yana/commands/typerelations/get.sh
	;;
    delete)
	[ -z "$ID" ] && { echo "missing required option: --id" ; exit 2 ; }
	source $RERUN_MODULES/yana/commands/typerelations/delete.sh
	;;
    create)
	[ -z "$CHILD" ] && { echo "missing required option: --child" ; exit 2 ; }
	[ -z "$PARENT" ] && { echo "missing required option: --parent" ; exit 2 ; }
	[ -z "$NAME" ] && { echo "missing required option: --name" ; exit 2 ; }
	source $RERUN_MODULES/yana/commands/typerelations/create.sh
	;;
    *)
	echo "Invalid action: \"$ACTION\""
	exit 2
esac


# ------------------------------

exit $?

# Done
