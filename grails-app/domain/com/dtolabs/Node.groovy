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
    Date lastUpdated

    static constraints = {
        name(blank:false, unique: ['project', 'nodetype'])
        description(blank:true, nullable:true)
        tags(nullable:true)
		nodetype(nullable:false)
        project(nullable: false)
    }

    def String toString() {
        return "${name}[${nodetype.name}]"
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

    Map toMap() {
        def map = [
                id: this.id,
                name: this.name,
                description: this.description,
                tags: this.tags,
                type: this.nodetype.name,
                typeId: this.nodetype.id
        ]

        if (this.nodeValues) {
            map.attributes = []
            this.nodeValues.each { NodeValue attr ->
                map.attributes << attr.toMap()
            }
        }
        if (this.children) {
            map.children = []
            this.children.each {ChildNode child ->
                map.children << child.toMap()
            }
        }
        if (this.parents) {
            map.parents = []
            this.parents.each {ChildNode child ->
                map.parents << child.toMap()
            }
        }

        return map
    }

}

