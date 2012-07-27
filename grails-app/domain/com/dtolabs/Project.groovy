package com.dtolabs

class Project {
    static searchable = {
        only = [ 'name']
        // only index name and rename as project
        // Project will be added as a searchable component to the Node class, which will
        // effectively import these properties into the Node's search index
        name name: 'project'
    }
    static constraints = {
        name unique: true, nullable: false, blank: false
        description nullable: false, blank: false
    }

    String name
    String description

    @Override
    public String toString() {
        return "Project{" +
               "name='" + name + '\'' +
               ", description='" + description + '\'' +
               '}';
    }
}
