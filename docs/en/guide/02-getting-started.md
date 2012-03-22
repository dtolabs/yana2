# Getting Started

This chapter helps new users getting started with Yana. We will begin
by explaining the basics, covering essential Yana concepts and
terminology and then move on to installation and finally, setup.
At the end of this chapter you should understand what Yana is, how
you should use it and you should be all setup to do so.

## Concepts

Operational environments benefit from having an online copy of
a their node inventory. Further advantages can be had by 
building tools that integrate with inventory data and their
behavior driven from views of it.

*Domain model*

:    How does Yana represent host resources in my environment?
     Yana focusses on the following core concepts:

     * `Node`: A host on the network.
         * Data: name, description, osFamily, osName, `Attribute`, `Tag`
     * `Attribute`: A named key/value pair that can be associated with a `Node`
         * Data: name, value, description
     * `Tag`: A symbolic label that can be associated with a Node
         * Data: text

     For attributes that are shared between nodes, two other concepts can be useful:
     
     * `ExternalAttribute`: Like an `Attribute` but shareable between Nodes.
         * Data: name, value, description
     * `Attributes`: A named collection of `ExternalAttribute` values that can
     be associated with a `Node`.
         * Data: `ExternalAttribute`

	
*Context*

:    Where does it fit? Yana fits in with your operations management software.
     It aims to be a useful citizen in a "loosely coupled" tool chain.
     Tools that can integrate with Yana include: monitoring, configuration
     management, your scripts, reporting and your documentation.

*Deployment*

:    How does it install? Yana is deployed as a Java-based webapp
     storing data in a relational database. 

*Architecture*

:    What's it based on? Yana is a [http://grails.org](grails) application and
     uses the Spring Security plugin to manage access. 
     Yana provides two interaces:
     
     * WebAPI: A RESTy network programming interface 
     * GUI: A simple HTML data management tool

*Yana is Not a CMDB*

:    Yana is not a CMDB. Yana is a repository of data about your Nodes.
     Other kinds of resources exist your environment like services,
     network topology, packaged artifacts. Other tools
     may already be providing this info in your environment now.

## Installation

Assuming the system requirements are met, Yana can be installed
either from source or via WAR file.

### System Requirements

* [Java](#Java)
* Tomcat
* Network ports

#### Java

Yana is a Java-Servlet based server and therefore requires the Java
runtime.

The install process requires that the latest version of Java 1.6
be installed. Both the [Open JDK](http://openjdk.java.net/) and 
[Sun/Oracle](http://java.com/) JVMs can be used.
You must have the JAVA_HOME environment variable defined
in your environment before running the launcher.  The RPM will 
use the java found on your path. See [Setting JAVA_HOME](#setting-java_home) 
if you want to run a different version of java.

Verify your Java version to check it meets the requirement:

    $ java -version
    java version "1.6.0_22"
    Java(TM) SE Runtime Environment (build 1.6.0_22-b04-307-10M3261)
    Java HotSpot(TM) 64-Bit Server VM (build 17.1-b03-307, mixed mode)

### Install from source

Checkout the sources from [GitHub - https://github.com/dtolabs/yana](https://github.com/dtolabs/yana)

Install Grails
    
    export GRAILS_HOME=/path/to/your/grails
    PATH=$GRAILS_HOME/bin:$PATH
    
Build the WAR file

    grails war yana.war

Creates the file `yana.war`.

### Install from WAR

Copy war to the webapps directory...

## First time setup

### Logins

## Summary

You should now have a basic understanding of Yana. You
should also have a working version of Yana on your system
and login access. It is now time to learn how to use Yana.

  


