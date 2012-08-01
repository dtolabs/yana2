# generated by stubbs:add-option
# Thu May 24 18:06:53 PDT 2012

# print USAGE and exit
rerun_option_error() {
    [ -z "$USAGE"  ] && echo "$USAGE" >&2
    [ -z "$SYNTAX" ] && echo "$SYNTAX $*" >&2
    return 2
}

# check option has its argument
rerun_option_check() {
    [ "$1" -lt 2 ] && rerun_option_error
}

# options: [file cfg]
while [ "$#" -gt 0 ]; do
    OPT="$1"
    case "$OPT" in
          -f|--file) rerun_option_check $# ; FILE=$2 ; shift ;;
  -C|--cfg) rerun_option_check $# ; CFG=$2 ; shift ;;
  -P|--project) rerun_option_check $# ; PROJECT=$2 ; shift ;;
  -a|--action)  rerun_option_check $# ; ACTION=$2 ; shift ;;
        # unknown option
        -?)
            rerun_option_error
            ;;
        # end of options, just arguments left
        *)
          break
    esac
    shift
done

# If defaultable options variables are unset, set them to their DEFAULT
[ -z "$CFG" ] && CFG="$HOME/.yanarc"
[ -z "$PROJECT" ] && PROJECT=$YANA_PROJECT
[ -z "$ACTION" ] && ACTION=import

# Check required options are set
[ -z "$FILE" ] && { echo "missing required option: --file" ; return 2 ; }
[ -z "$CFG" ] && { echo "missing required option: --cfg" ; return 2 ; }
[ -z "$PROJECT" ] && { echo "missing required option: --project" ; return 2 ; }

#
return 0
