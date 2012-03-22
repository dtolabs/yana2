% NODE-V10(5) YANA Reference 
% Alex Honor
% August 03, 2011

# NAME

node-v10 - describes the Yana node model document

The Node Model XML document declares a node model that can also be
uploaded to a Yana server. This document describes the
format and necessary elements.

## node

The root (aka "top-level") element of the node file is called `node`.

*Attributes*

name

:   The node name. This is a logical identifier from the node and must be unique. (required)

description

:   A brief description about the node. (optional)

osFamily

:   The operating system family, such as unix or windows.  (required)

osName

:   The operating system name such as Linux or Mac OS X.  (optional)

id

:   The database identifier. If left out, a new object will be created.


*Nested Elements*

[attributes](#attributes)

:   Collection of user defined attributes

[tags](#tags)

:   Collection of user defined tags

[externalAttributes](#externalAttributes)

:   Collection of external defined attributes

### attributes

Defines a collection of attribute elements for the node.

*Nested Elements*

[attribute](#attribute)

:   Further user defined attribute.

### attribute

A single metadata attribute.

*Attributes*

name

:   The name of the attribute

value

:   The value of the attribute

### tags

Collection of symbolic names for the node

*Nested Elements*

[tag](#tag)

:   Single tag definition

### tag

A single tag definition

*Nexted element*

## name

:   The name. Must be unqiue.




## Examples

Define a node named `strongbad`:

    <node>
      <name>strongbad</name>
      <description>a development host</description>
      <osFamily>unix</osFamily>
      <osName>Darwin</osName>
      <attributes/>
      <tags/>
      <externalAttributes/>
    </node>

Define a node that has a `https-port` attribute:

    <node>
      <name>strongbad</name>
      <description>a development host</description>
      <osFamily>unix</osFamily>
      <osName>Darwin</osName>
      <attributes>
        <attribute>
          <name>https-port</name>
          <value>443</value>
        </attribute>
      </attributes>
      <tags/>
      <externalAttributes/>
    </node>
    
Define a node that has a tag named `web`:

    <node>
      <name>strongbad</name>
      <description>a development host</description>
      <osFamily>unix</osFamily>
      <osName>Darwin</osName>
      <tags>
        <tag>
          <name>web</name>
        </tag>
      </tags>
      <attributes/>
      <externalAttributes/>
    </node>

The Yana source code and all documentation may be downloaded from
<https://github.com/dtolabs/yana/>.
