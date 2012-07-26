package com.dtolabs

class Project {

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
