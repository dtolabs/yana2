# generated by stubbs:add-option
# Fri May 25 14:45:50 PDT 2012

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

# options: [action attributes description file id status tags typeid cfg]
while [ "$#" -gt 0 ]; do
    OPT="$1"
    case "$OPT" in
  -D|--depth) rerun_option_check $# ; DEPTH=$2 ; shift ;;
  -F|--format) rerun_option_check $# ; FORMAT=$2 ; shift ;;
  -i|--id) rerun_option_check $# ; ID=$2 ; shift ;;
  -A|--action) rerun_option_check $# ; ACTION=$2 ; shift ;;
  -n|--name) rerun_option_check $# ; NAME=$2 ; shift ;;
  -c|--child) rerun_option_check $# ; CHILD=$2 ; shift ;;
  -p|--parent) rerun_option_check $# ; PARENT=$2 ; shift ;;
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
[ -z "$ACTION" ] && ACTION=children
[ -z "$CFG" ] && CFG="$HOME/.yanarc"
[ -z "$DEPTH" ] && DEPTH=1
[ -z "$FORMAT" ] && FORMAT='${ID}:${RELATIONSHIP}:${CHILDNODE}:${NAME}:${TYPE}'
# Check required options are set

[ -z "$CFG" ] && { echo "missing required option: --cfg" ; return 2 ; }
[ -z "$ID" ] && { echo "missing required option: --id" ; return 2 ; }
#
return 0
