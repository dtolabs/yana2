# generated by stubbs:add-option
# Wed May 23 15:31:40 PDT 2012

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

# options: [action child id name cfg]
while [ "$#" -gt 0 ]; do
    OPT="$1"
    case "$OPT" in
          -a|--action) rerun_option_check $# ; ACTION=$2 ; shift ;;
  -c|--child) rerun_option_check $# ; CHILD=$2 ; shift ;;
  -i|--id) rerun_option_check $# ; ID=$2 ; shift ;;
  -n|--name) rerun_option_check $# ; NAME=$2 ; shift ;;
  -C|--cfg) rerun_option_check $# ; CFG=$2 ; shift ;;
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
[ -z "$ACTION" ] && ACTION="create"
[ -z "$CFG" ] && CFG="$HOME/.yanarc"
# Check required options are set
[ -z "$ACTION" ] && { echo "missing required option: --action" ; return 2 ; }
[ -z "$YANA_URL" ] && { echo "missing required option: --url" ; return 2 ; }
#
return 0
