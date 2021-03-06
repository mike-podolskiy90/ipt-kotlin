<#escape x as x?html>
    <#include "/inc/header.ftl">
    <#import "/macros/legacy_macros.ftl" as s>
    <title><#if id?has_content><@s.text name="admin.user.title.edit"/><#else><@s.text name="admin.user.title.new"/></#if></title>
    <script src="http://localhost:8080/js/jconfirmation.jquery.js"></script>
    <script>
        $(document).ready(function(){
            $('.confirm').jConfirmAction({titleQuestion : "<@s.text name="basic.confirm"/>", yesAnswer : "<@s.text name="basic.yes"/>", cancelAnswer : "<@s.text name="basic.no"/>", buttonType: "danger"});
        });
    </script>
    <#assign currentMenu = "admin"/>
    <#include "/inc/menu.ftl">
    <#include "/macros/forms.ftl">

    <div class="container-fluid bg-body border-bottom">
        <div class="container my-3">
            <#include "/inc/action_alerts.ftl">
        </div>

        <div class="container my-3 p-3">
            <div class="text-center">
                <div class="text-uppercase fw-bold fs-smaller-2">
                    <span><@s.text name="menu.admin"/></span>
                </div>

                <h1 class="pb-2 mb-0 pt-2 text-gbif-header fs-2 fw-normal">
                    <#if id?has_content><@s.text name="admin.user.title.edit"/><#else><@s.text name="admin.user.title.new"/></#if>
                </h1>

                <div class="mt-2">
                    <@s.submit cssClass="button btn btn-sm btn-outline-gbif-primary top-button" form="user" name="save" key="button.save"/>
                    <#if id?has_content>
                        <@s.submit cssClass="confirm btn btn-sm btn-outline-gbif-danger top-button" form="user" name="delete" key="button.delete"/>
                        <@s.submit cssClass="button btn btn-sm btn-outline-gbif-danger top-button" form="user" name="resetPassword" key="button.resetPassword" />
                    </#if>
                    <a href="http://localhost:8080/admin/users" class="btn btn-sm btn-outline-secondary top-button"><@s.text name="button.cancel"/></a>
                </div>
            </div>
        </div>
    </div>

    <main class="container">
        <div class="my-3 p-3">
            <p class="mb-0">
                <@s.text name="admin.user.intro"/>
            </p>
            <p class="mb-0">
                <@s.text name="admin.user.intro2"/>
            </p>

            <form id="user" class="needs-validation" action="/admin/user" method="post">
                <div class="row g-3 mt-2">
                    <#if user?? && user.email??>
                        <@s.hidden name="id" value="${user.email}" required="true"/>
                    </#if>

                    <div class="col-md-6">
                        <@input name="firstname" i18nkey="user.firstname" value="${(user.firstname)!}" />
                    </div>

                    <div class="col-md-6">
                        <@input name="lastname" i18nkey="user.lastname" value="${(user.lastname)!}" />
                    </div>

                    <div class="col-md-6">
                        <@input name="email" i18nkey="user.email" value="${(user.email)!}" disabled=id?has_content/>
                    </div>

                    <div class="col-md-6">
                        <@select name="role" i18nkey="user.role" value=(user.role)! javaGetter=false options={"User":"user.roles.user", "Manager":"user.roles.manager", "Publisher":"user.roles.publisher", "Admin":"user.roles.admin"}/>
                    </div>

                    <#if !id?has_content>
                        <div class="col-md-6">
                            <@input name="password" i18nkey="user.password" type="password" value="${(user.password)!}" />
                        </div>
                        <div class="col-md-6">
                            <@input name="password2" i18nkey="user.password2" type="password" value="${password2!}"/>
                        </div>
                    </#if>
                </div>
            </form>
        </div>
    </main>

    <#include "/inc/footer.ftl">
</#escape>
