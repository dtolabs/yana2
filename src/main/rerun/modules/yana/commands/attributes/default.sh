#!/bin/bash
#
# NAME
#
#   attributes
#
# DESCRIPTION
#
#   manage node attributess
#

# Read module function library
source $RERUN_MODULES/yana/lib/functions.sh || exit 1 ;

#
# Initialize the context
#
yana_initialize $CFG || rerun_die "Yana initialization failed"

# Parse the command options
[ -r $RERUN_MODULES/yana/commands/attributes/options.sh ] && {
  source $RERUN_MODULES/yana/commands/attributes/options.sh || exit 2 ;
}


# ------------------------------
flags="" #-v --silent --fail --show-error"

cookie=/tmp/yana-attributes-cookiejar.txt
response=/tmp/yana-attributes-response.txt
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
	[ ${#arr[*]} -eq 3 ] || continue
	IFS=$oIFS
	yana_expand "$FORMAT"  ID=${arr[0]} NAME=${arr[1]} FILTER_DATATYPE=${arr[2]} 
    done 
}

#
# Execute specified action
#

case $ACTION in
    get)
	[ -z "$ID" ] && { echo "missing required option: --id" ; exit 2 ; }
	source $RERUN_MODULES/yana/commands/attributes/get.sh
	;;
    create)
	[ -z "$ID" ] && { echo "missing required option: --id" ; exit 2 ; }

	[ -z "$NAME" ] && { echo "missing required option: --name" ; exit 2 ; }
	source $RERUN_MODULES/yana/commands/attributes/create.sh
	;;
    delete)
	source $RERUN_MODULES/yana/commands/attributes/delete.sh
	;;
    list)
	source $RERUN_MODULES/yana/commands/attributes/list.sh
	;;
    *)
	echo "Invalid action: $ACTION"
	exit 2
esac


# ------------------------------

exit $?

# Done
