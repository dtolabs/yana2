#!/bin/bash
#
# NAME
#
#   search
#
# DESCRIPTION
#
#   node search
#

# Read module function library
source $RERUN_MODULES/yana/lib/functions.sh || exit 1 ;

# Parse the command options
[ -r $RERUN_MODULES/yana/commands/search/options.sh ] && {
  source $RERUN_MODULES/yana/commands/search/options.sh || exit 2 ;
}


# ------------------------------

cookie=/tmp/yana-search-cookiejar.txt
response=/tmp/yana-search-response.txt
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
# Define the query params
#

qParams=( )
[ -n "${TYPE}" ] && qParams=( ${qParams[*]} "nodetype%3A${TYPE}" )
[ -n "${NAME}" ] && qParams=( ${qParams[*]} "name%3A+${NAME}" )
i=0
qString=
for q in ${qParams[*]}
do
    [[ $i -gt 0 ]] && qString="${qString}+AND+${q}" || qString=$q
    i=$(( $i + 1 ))
done

[ -z "$qString" ] && { echo "specify --type or --name"  ; exit 1 ; }


#
# Submit the search
#

curl --fail --silent \
    "${YANA_URL}/search/index?q=${qString}&format=xml" \
    --cookie ${cookie} -o ${response} || rerun_die "failed loading data to server"

grep -q 'Invalid search' ${response} && { echo 'invalid search criteria' ; exit 1 ; }

#
# Validate the response is well formed XML
#
xmlstarlet val --well-formed --quiet ${response} 2>/dev/null || rerun_die "Yana response failed XML validation"


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
	yana_expand "$FORMAT" TYPE=${arr[0]} NAME=${arr[1]} ID=${arr[2]}

    done 
}

#
# Output the data
#
xmlstarlet sel -t -m /nodes/node -v @type -o ":" -v @name -o ":" -v @id -n $response | format


# ------------------------------

exit $?

# Done
