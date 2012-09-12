<%@ page import="com.dtolabs.yana2.YanaConstants; com.dtolabs.yana2.springacl.YanaPermission; com.dtolabs.Project" %>

<div >
    <div>
        <span class="fake_button grantButton">Add Permission</span>
    </div>
    <div class="permissionForm" style="display: none;">
        <g:form action="saveProjectPermission" id="saveProjectPermission">
            <g:hiddenField name="name" value="${project.name}"/>
            <fieldset >
                <g:select name="permissionGrant" from="['grant','deny']"/>
                <span class="permissionName" id="permName"></span>
                <g:textField id="recipient" name="recipient" value="" placeholder="Username or ROLE_*"
                             autocomplete='off'/>
                <g:select name="permission" from="${YanaPermission.byName.keySet()}"/>
                <input type="button"  class="grantCancelButton" name="cancel" value="Cancel"/>
                <g:submitButton name="Save" />
            </fieldset>
        </g:form>
    </div>
</div>

<div class="list">
    <table>
        <thead>
        <tr>
            <td>${g.message(code: 'recipient.type.label', default: 'Type')}</td>
            <td>${g.message(code: 'name.label', default: 'Name')}</td>
            <td colspan="2">${g.message(code: 'permission.label', default: 'Permission')}</td>
            <td>${g.message(code: 'action.label', default: 'Action')}</td>
        </tr>
        </thead>

        <tbody>

        <g:each in="${acls}" var="aclEntry" status="i">
            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
            <g:set var="recipient" value="${aclEntry.role ?: aclEntry.username}"/>
                <td>
                    <span class="recipientType">${g.message(code:'recipient.type.'+(aclEntry.role ? 'role' : 'user')+'.label')}</span>
                </td>
                <td>
                    <span class="recipient">${recipient}</span>
                </td>
                <td>
                    <span class="granted">${aclEntry.granted ? 'ALLOW' : 'DENY'}</span>
                </td>
                <td>
                    <span class="permission">${g.message(code: 'permission.'+ aclEntry.permission+'.label', default: aclEntry.permission)}</span>
                </td>
                <td>
                    %{--
                    Hide delete button if action would be disallowed:
                    not allowed to delete ADMINISTRATION perm for the ROLE_YANA_ADMIN or ROLE_YANA_SUPERUSER
                    --}%
                    <g:if test="${!(recipient in [YanaConstants.ROLE_ADMIN,YanaConstants.ROLE_SUPERUSER]) || aclEntry.permission!= YanaPermission.nameFor(YanaPermission.ADMINISTRATION)}">
                    <g:form controller="project" action="deleteProjectPermission">
                        <g:hiddenField name="name" value="${project.name}"/>
                        <g:hiddenField name="recipient" value="${recipient}"/>
                        <g:hiddenField name="permission" value="${aclEntry.permission}"/>
                        <g:hiddenField name="permissionGrant" value="${aclEntry.granted ? 'grant' : 'deny'}"/>
                        <g:submitButton
                            name="${g.message(code:'default.button.delete.label')}"
                            onclick="return confirm('${g.message(code: 'default.button.delete.confirm.message').encodeAsJavaScript()}');"/>
                    </g:form>
                    </g:if>
                </td>
            </tr>
            </g:each>
        </tbody>
    </table>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $('span.grantButton').click(function () {
            $('div.permissionForm').show();
        });
        $('input.grantCancelButton').click(function () {
            $('div.permissionForm').hide();
        });
        $("#recipient").focus().autocomplete({
                                                 minLength:3,
                                                 cache:false,
                                                 source:"${createLink(action: 'ajaxUserRoleSearch',controller: 'user')}",
                                             });
        $('.permissionForm form').submit(function (evt) {
            if($('#recipient').val()==''){

                $("#recipient").effect('highlight', {}, 1000);
                evt.preventDefault();
                return false;
            }
            return true;
        });
    });
</script>