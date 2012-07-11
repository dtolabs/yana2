#!/bin/bash
#
# NAME
#
#   relations
#
# DESCRIPTION
#
#   manage node relationss
#

# Read module function library
source $RERUN_MODULES/yana/lib/functions.sh || exit 1 ;

# Parse the command options
[ -r $RERUN_MODULES/yana/commands/relations/options.sh ] && {
  source $RERUN_MODULES/yana/commands/relations/options.sh || exit 2 ;
}


# ------------------------------
flags="" #-v --silent --fail --show-error"

cookie=/tmp/yana-relations-cookiejar.txt
response=/tmp/yana-relations-response.txt
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
# Function to format the output
# 
format() {
    oIFS=$IFS
    while read line
    do
	IFS=:
	arr=( $line )
	[ ${#arr[*]} -eq 5 ] || continue
	IFS=$oIFS
	yana_expand "$FORMAT"  CHILDNODE=${arr[0]} RELATIONSHIP=${arr[1]} ID=${arr[2]} NAME=${arr[3]} TYPE=${arr[4]} 
    done 
}

#
# Execute specified action
#

case $ACTION in
    children)
	source $RERUN_MODULES/yana/commands/relations/children.sh
	;;
    parents)
	source $RERUN_MODULES/yana/commands/relations/parents.sh
	;;
    create)
	[ -z "$NAME" ] && { echo "missing required option: --name" ; exit 2 ; }
	[ -z "$CHILD" ] && { echo "missing required option: --child" ; exit 2 ; }
	source $RERUN_MODULES/yana/commands/relations/create.sh
	;;
    delete)
	source $RERUN_MODULES/yana/commands/relations/delete.sh
	;;
    *)
	echo "Invalid action: $ACTION"
	exit 2
esac


# ------------------------------

exit $?

# Done
