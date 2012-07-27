package yana2

class ProjectFilters {

    def filters = {
        all(controller:'*', controllerExclude: 'project|login', action:'*') {
            before = {
                if(!session.project && !params.project){
                    redirect(controller: 'project', action: 'list', params: ['mustChoose': 1])
                    return false
                }else if(!params.project){
                    params.project=session.project
                }else if(!session.project){
                    session.project=params.project
                }
            }
        }
    }
}
