# YANA API

Yana provides a Web API for use with your application.
 
## URLs

The Yana server has a "Base URL", where you access the server. 
Your Yana Server URL may look like: `http://myserver:8080/yana`.

The root URL path for all calls to the API in this version is:

    $YANA_SERVER_URL/api

In this document we will leave off the `$YANA_SERVER_URL/api` 
and simply display URLs as `/...`.

## XML

The Yana API calls require XML for input and output.  
JSON support is planned for a future API version.

## Authentication

None currently. TBD

## Response Format

### Item Lists ###

Some API requests will return an item list as a result.  
These are typically in the form:

    <list>
        <[item] ...>
        <[item] ...>
    </list>

When an API path declares its results as an "Item List" this 
is the format that will be returned.


## API Contents

### Listing Nodes

List the nodes that exist

URL:

    /api/nodes

Via `curl`:

    curl -X GET http://localhost:8080/api/nodes

Required parameters:

None.

Result: An Item List of `nodes`. Each `node` is of the form:


       <node id="ID">
          <name>Node Name</name>
          <description>...</description>
          <osName>...</osName>
          <osFamily>...</osFamily>
          <attributes />
          <externalAttributes />
          <tags/>
       </node>

   
### Adding a Node ###

Import node definition in XML format.

URL:

    /api/nodes

Method: `POST`

Required Content-Type: `multipart/form-data`

A Node definition will be of the form [node-v10](node-v10.html). 
Here's an example minimally describing a node:

File listing: `node1.xml`

    <node>
        <name>node1</name>
        <description>the first node</description>
    </node>

Via `curl`:

    curl -X POST -H 'Content-Type: text/xml' --data @node1.xml http://localhost:8080/yana/api/nodes

Result:

    <results>
       <result>Created new node. id: 1</result>
    </results>

### Getting a Node Definition ###

Get a node definition.

URL:

    /api/nodes/[ID]

Method: `GET`

Via `curl`:

    curl -X http://localhost:8080/yana/api/nodes/1
    
Result:

    <node>
        <name>node1</name>
        <description>the first node</description>
    </node>    

### Updating a Node definition 

Change a value in a node definition.

URL:

    /api/nodes/[ID]

File listing: `node1.xml`

    <node id="1">
        <name>node1</name>
        <description>the first node and the best so far!</description>
    </node>

Via `curl`

    curl -X POST -H 'Content-Type: text/xml' --data @node1.xml http://localhost:8080/yana/api/nodes/1

    
### Deleting a Node Definition ###

Delete a single node definition.

URL:

    /api/nodes/[ID]

Method: `DELETE`

Result:

The common `result` element described in the 
[Response Format](#response-format) section, 
indicating success or failure and any messages.

Via `curl`:

    curl -X DELETE http://localhost:8080/yana/api/nodes/1

If successful, then the `result` will contain a `message`
element with the result message:

    <results>
       <result>Node removed. id: 1</result>
    </results>


