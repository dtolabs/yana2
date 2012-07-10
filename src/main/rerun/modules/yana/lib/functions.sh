#
# common shell functions for yana commands
#

#
# colorizing functions -
#

# bold - bold the given text
bold() { echo -e "\033[1m$*\033[0m" ; reset ; }
# reset the terminal
reset () { tput sgr0 ; }

#
# error handling functions -
#

# print error message and exit
rerun_die() {
    [[ "$RERUN_COLOR" == "true" ]] && bold "$*" >&2 || echo "$*" >&2
    exit 1
}


PAD="  "

# print usage for a single option
rerun_option_usage() {
    module=${1%:*} command=${1#*:} option=$2
    opt_metadata=$RERUN_MODULES/$module/commands/$command/$option.option
    [ ! -f $opt_metadata ] && return
    opt_name=`awk -F= '/^NAME/ {print $2}' $opt_metadata`
    opt_desc=`awk -F= '/^DESCRIPTION/ {print $2}' $opt_metadata`
    opt_arg=`awk -F= '/^ARGUMENTS/ {print $2}' $opt_metadata`
    opt_req=`awk -F= '/^REQUIRED/ {print $2}' $opt_metadata`
    opt_def=`awk -F= '/^DEFAULT/ {print $2}' $opt_metadata`
    opt_short=`awk -F= '/^SHORT/ {print $2}' $opt_metadata`
    argstring=
    [ -n "${opt_short}" ] && {
	argstring=$(printf ' -%s|--%s' "${opt_short}" "${opt_name}")
    } || {
	argstring=$(printf " --%s" "${opt_name}" )
    }
    [ "true" == "${opt_arg}" ] && {
	argstring=$(printf "%s <%s>" $argstring ${opt_def})
    }
    [ "true" != "${opt_req}" ] && {
	opt_usage=$(printf "[%s: %s]" "${argstring}" "${opt_desc}") 
    } || {
	opt_usage=$(printf "%s: %s" "${argstring}" "${opt_desc}")
    }
    printf "%s %s\n" "$PAD" "$opt_usage"
}

# print usage for all options in the command
rerun_command_usage() {
    module=${1%:*} command=${1#*:}
    metadata=$RERUN_MODULES/$module/commands/${command}/metadata
    [ -f $metadata ] && desc=`awk -F= '/^DESCRIPTION/ {print $2}' $metadata`
    echo "Usage: "
    echo " ${command}: ${desc}"
    printf "%s%s\n" "$PAD" "[options]"
    shopt -s nullglob # enable
    for opt_metadata in $RERUN_MODULES/$module/commands/${command}/*.option; do
	option=$(basename $(echo ${opt_metadata%%.option}))
	rerun_option_usage ${module}:${command} $option
    done
}


# print command usage and return
rerun_option_error() {
    module=${1%:*} command=${1#*:}
    rerun_command_usage $module $command >&2
    return 2
}

# check option has its argument
rerun_option_check() {
    [ "$1" -lt 2 ] && return 2
}


#
# Yana authentication function
#

yana_authenticate() {
    local url=$1
    local user=$2
    local pass=$3
    local cookie=$4
    curl --fail --silent \
	--data "j_username=${user}&j_password=${pass}" \
	${url}/springSecurityApp/j_spring_security_check \
	--cookie-jar ${cookie} || rerun_die "login failed for admin"
}

#
# Yana initialization function
#

yana_initialize() {
    local cfg=$1

    if [ -r $cfg ] 
    then 
	source $cfg
    else
	#echo "config file not found: $cfg"
	YANA_URL=http://localhost:8080
	YANA_USER=admin
	YANA_PASSWORD=admin	
    fi
    [ -z "$YANA_URL" -o -z "$YANA_USER" -o -z "$YANA_PASSWORD" ] && {
	echo "missing server connection info in file: $cfg"
	return 1 
    }

    #
    # Check for a bundled xmlstarlet and alias to it accordingly
    #
    os_arch=$(printf "%s_%s" $(uname -s) $(uname -m))
    if [ -x $RERUN_MODULES/yana/lib/xmlstarlet/${os_arch} ] 
    then
	alias xmlstarlet=$RERUN_MODULES/yana/lib/xmlstarlet/${os_arch}
    fi
}

#
# Expand a line based on a format string and variable bindings
#
#  Example: yana_expand '${one}:${two}' one=1 two=2
#         ==> 1:2
#
yana_expand() {
    local fmtString=$1
    shift
    for kv in $@; do
	local key=${kv%=*}
	local val="${kv#*=}"
	eval $key="$val" ; # sets the variable
    done
    eval echo ${fmtString} 
}


