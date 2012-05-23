package com.dtosolutions

class Node {

    static searchable = {
        nodetype component:true
    }
	//static embedded = 'nodetype'

	
	
	static mappedBy = [children: 'child', parents: 'parent']
	static hasMany = [templateValues:TemplateValue,children:ChildNode,parents:ChildNode]
	
    String name
    String description
	Status status
	String tags
	NodeType nodetype
    Date dateCreated
    Date dateModified = new Date()

    static constraints = {
        name(blank:false)
        description(blank:true, nullable:true)
		status(nullable:false)
        tags(nullable:true)
		nodetype(nullable:false)
    }

    def String toString() {
        return name
    }

   // A dynamic (like?) find method
   static Set<Node> findAllTagsByName(String name)  {
       return Node.withCriteria {
		   like('tags', "%${name}%")
       }
   }

   // A dynamic (like?) find method
   static Set<Node> findAllByNameLikeAndTagsByName(String nameLike, String tagName)  {
       return Node.withCriteria {
            ilike('name',nameLike)
            like ('tags',"%${tagName}%")
       }
   }

}

public enum Status {
	 DEV("Development"),
	 TEST("Test"),
	 STAGE("Stage"),
	 PROD("Production"),
	 IMP("Implementation"),
	 BKP("Backup"),
	 OTHER("Other")
	
	 private final String value
	
	 Status(String value){
	  this.value = value;
	 }
	
	 String toString() {
	  value
	 }
	
	 String getKey() {
	  name()
	 }
	
	 static list(){
		 [DEV,TEST,STAGE,PROD,IMP,BKP,OTHER]
	 }
}