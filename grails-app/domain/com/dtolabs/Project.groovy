package com.dtolabs

class Project {

    static constraints = {
        name unique: true, nullable: false
        description nullable: false
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
