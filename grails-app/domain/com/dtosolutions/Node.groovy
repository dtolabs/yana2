package com.dtosolutions

class Node {

    static searchable = true
	static hasMany = [templateValues:TemplateValue,nodes:Node]
	
    String name
    String description
	//Template template
	Status status
	Importance importance
	String tags
	NodeType nodetype
    Date dateCreated
    Date dateModified = new Date()
	Node parent

    static constraints = {
        name(blank:false)
        description(blank:true, nullable:true)
<<<<<<< HEAD
		//template(nullable:false)
=======
>>>>>>> owens_branch
		status(nullable:false)
		importance(nullable:false)
        tags(nullable:true)
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