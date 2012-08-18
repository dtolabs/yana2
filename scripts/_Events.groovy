
eventCompileEnd = {
    ant.copy(todir:classesDirPath) {
      fileset(file:"${userHome}/.grails/config.properties")
    }
}


eventConfigureTomcat = {tomcat ->
  if(grails.util.Environment.current.name == 'development'){
      tomcat.addUser("admin", "admin")
      tomcat.addRole("admin", "admin")
      tomcat.addRole("admin", "yana")

      tomcat.addUser("op1", "op1")
      tomcat.addRole("op1", "operator")
      tomcat.addRole("op1", "yana")

      tomcat.addUser("arch1", "arch1")
      tomcat.addRole("arch1", "architect")
      tomcat.addRole("arch1", "yana")
    }
}
