
#### Setup Development/Staging/Test ####

Requirements
- Grails 2.0+
- MySql

Login to MySql and run the following commands as 'root':

> create database yana3;
> grant ALL on yana3.* to 'yana'@'localhost' identified by '1234';

If these passwords don't word for you, please change them in the config>DataSource.groovy file

To run locally, type the following from within your project:

> grails run-app -https


#### Setup Production ####

To run in production, package as WAR and deploy with JNDI in context as 'mydatasource' (http://refactor.com.au/blog/idiots-guide-tomcat-6-grails-jndi-datasource)


#### Import Resources ####

In admin/importxml, there is an interface provided for uploading a file for including all resources in your structure to get them into YANA without hand loading them. The example XML file for this is in the docs folder called 'yana.xml' and uses the schema called 'yana.xsd' found in same folder.

Use this document as a template for uploading your structure into Yana.
