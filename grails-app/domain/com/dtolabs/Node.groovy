package com.dtolabs

class Node {

    static searchable = {
        nodetype component:true
        project component:true
    }
	
	static mappedBy = [children: 'child', parents: 'parent']
	static hasMany = [nodeValues:NodeValue,children:ChildNode,parents:ChildNode]
	
    String name
    String description
	String tags
	NodeType nodetype
    Project project;
    Date dateCreated
    Date dateModified = new Date()

    static constraints = {
        name(blank:false)
        description(blank:true, nullable:true)
        tags(nullable:true)
		nodetype(nullable:false)
        project(nullable: false)
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

