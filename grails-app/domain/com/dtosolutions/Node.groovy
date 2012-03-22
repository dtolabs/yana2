package com.dtosolutions

class Node {

    static searchable = true
	static hasMany = [instances:Instance,templateValues:TemplateValue,nodes:Node]
	static belongsTo = [Solution]
	
    String name
    String description
	Template template
	Status status
	Importance importance
	String tags
	Location location
	Solution solution
	NodeType nodetype
    Date dateCreated
    Date dateModified = new Date()
	Node parent

    static constraints = {
        name(unique:true, blank:false)
        description(blank:true, nullable:true)
		template(nullable:false)
		status(nullable:false)
		importance(nullable:false)
        tags(nullable:true)
		location(nullable:false)
		solution(nullable:true)
		nodetype(nullable:false)
		parent(nullable:true)
    }

    def String toString() {
        return name
    }


   // A dynamic (like?) find method
   static Set<Node> findAllTagsByName(String name)  {
       println "DEBUG: inside findAllTagsByName. name="+name
       return Node.withCriteria {
		   like('tags', "%${name}%")
       }
   }

   // A dynamic (like?) find method
   static Set<Node> findAllByNameLikeAndTagsByName(String nameLike, String tagName)  {
       println "DEBUG: inside findAllByNameLikeAndTagsByName. name="+tagName
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

public enum Importance {
	HIGH("High"),
	LOW("Low"),
	MED("Medium")
   
	private final String value
   
	Importance(String value){
	 this.value = value;
	}
   
	String toString() {
	 value
	}
   
	String getKey() {
	 name()
	}
   
	static list(){
		[HIGH,LOW,MED]
	}
}