#
# NAME
#
#   create
#
# DESCRIPTION
#
#   create a new node
#

lookupAttrId() {
    attrName=$1
    shift
    declare -a attrArray
    attrArray=( $@ )
    for a in ${attrArray[@]}; do
	name=${a%%:*}
	[ $attrName == $name ] && { echo ${a##*:} ; break ; } || continue
    done
}

#
# Lookup the NodeType to get its template attribute info
#
curl --fail --silent \
    --request GET \
    ${YANA_URL}/api/nodeType/xml/${TYPEID} \
    --cookie $cookie -o $response

[ $? -eq 0 ] || { echo "Type not found with that id: $TYPEID" ; exit 1 ; }
TYPE=$(xmlstarlet sel -t -m /nodetypes/nodetype -v @name $response)

#
# Map attribute names to formatted attribute ID keys
# eg attrName:att#id
#
declare -a attributeKeys
attributeKeys=( $(xmlstarlet sel -t \
    -m /nodetypes/nodetype/nodeAttributes/nodeAttribute \
    -v @attributeName -o ":" -o "att" -v @id  -o " " $response) )

declare -a attributeData
inattrs=( $ATTRIBUTES )
for i in ${inattrs[*]}
do
    attrName=${i%%:*}
    attrValue=${i##*:}
    attKey=$(lookupAttrId $attrName "${attributeKeys[@]}")
    [ -n "${attKey}" ] && attributeData=("${attributeData[@]}" $attKey:\'$attrValue\',)
done

data=
data="$data $(printf "%s:%s," nodetype "$TYPEID")"
data="$data $(printf "%s:'%s'," name "$NAME")"
data="$data $(printf "%s:'%s'," description "$DESCRIPTION")"
data="$data $(printf "%s:'%s'," status "$STATUS")"
data="$data $(printf "%s:'%s'" tags "$TAGS")"
data="$data, ${attributeData[@]}"

# 
# Example data:
#
# "{name:'new node',description:'this is the description',nodetype:1,status:'DEV',tags:'this,is,a,test',att1:'value1',att2:'value2'}"
#
#

#
# Post the data to Yana and create the node
#
curl --fail --silent --request POST --header "Content-Type: application/json" \
    -d {"$data"}  \
    ${YANA_URL}/api/node/xml \
    --cookie ${cookie} -o $response || rerun_die "failed posting Yana data"

nodeId=$(xmlstarlet sel -t -m /nodes/node -v @id $response)

echo "$TYPE:$NAME:$nodeId"

# ------------------------------

return $?

# Done
