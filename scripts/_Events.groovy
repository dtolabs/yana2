
eventCompileEnd = {
    ant.copy(todir:classesDirPath) {
      fileset(file:"${userHome}/.grails/config.properties")
    }
}