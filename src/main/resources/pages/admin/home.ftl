<#include "/inc/header.ftl">
<#import "/macros/legacy_macros.ftl" as s>
<title><@s.text name="title"/></title>
<#assign currentMenu = "admin"/>
<#include "/inc/menu.ftl">
<#--<#include "../macros/popover.ftl"/>-->

<div class="container-fluid bg-body">
    <div class="container my-3">
<#--        <#include "../inc/action_alerts.ftl">-->
    </div>
</div>

<main class="container">
    <div class="row mt-xl-5">
        <div class="col-sm-12 p-0 border-xl shadow-sm">
            <div class="card admin-card">
                <div class="card-body m-0 p-0">
                    <div class="row gx-0 text-center admin-col-listing">
<#--                        <div class="col-xl-3 col-12">-->
<#--                            <a href="/admin/config" title="" class="admin-col-listing-item border-xl-right border-bottom">-->
<#--                                <div class="admin-icon-wrapper">-->
<#--                                    <i class="bi bi-gear-fill admin-icon"></i>-->
<#--                                </div>-->
<#--                                <h5 class="admin-card-title fw-400">-->
<#--                                    <@s.text name="admin.home.editConfig"/>-->
<#--                                </h5>-->
<#--                            </a>-->
<#--                        </div>-->
<#--                        <div class="col-xl-3 col-12">-->
<#--                            <a href="admin/bulk-publication" title="" class="admin-col-listing-item border-xl-right border-bottom">-->
<#--                                <div class="admin-icon-wrapper">-->
<#--                                    <i class="bi bi-stack admin-icon"></i>-->
<#--                                </div>-->
<#--                                <h5 class="admin-card-title fw-400">-->
<#--                                    <@s.text name="admin.home.bulkPublication"/>-->
<#--                                </h5>-->
<#--                            </a>-->
<#--                        </div>-->
                        <div class="col-xl-3 col-12">
<#--                            <a href="/admin/users" title="" class="admin-col-listing-item border-xl-right border-bottom">-->
                            <a href="/admin/users" title="" class="admin-col-listing-item border-xl-right">
                                <div class="admin-icon-wrapper">
                                    <i class="bi bi-people-fill admin-icon"></i>
                                </div>
                                <h5 class="admin-card-title fw-400">
                                    <@s.text name="admin.home.manageUsers"/>
                                </h5>
                            </a>
                        </div>
<#--                        <div class="col-xl-3 col-12">-->
<#--                            <a href="/admin/registration" title="" class="admin-col-listing-item border-bottom">-->
<#--                                <div class="admin-icon-wrapper">-->
<#--                                    <i class="bi bi-cloud-arrow-up-fill admin-icon"></i>-->
<#--                                </div>-->
<#--                                <h5 class="admin-card-title fw-400">-->
<#--                                    <@s.text name="admin.home.editRegistration"/>-->
<#--                                </h5>-->
<#--                            </a>-->
<#--                        </div>-->
<#--                        <div class="col-xl-3 col-12">-->
<#--                            <#if !registeredIpt?has_content>-->
<#--                                <div href="javascript:void(0);" title="" class="admin-col-listing-item border-xl-right border-xl-max-bottom text-gbif-header admin-col-listing-item-disabled">-->
<#--                                    <i class="bi bi-building admin-icon text-gbif-header"></i>-->
<#--                                    <h5 class="admin-card-title fw-400">-->
<#--                                        <a tabindex="0" role="button"-->
<#--                                           class="popover-link"-->
<#--                                           data-bs-toggle="popover"-->
<#--                                           data-bs-trigger="focus"-->
<#--                                           data-bs-html="true"-->
<#--                                           data-bs-content="<@s.text name="admin.home.editOrganisations.disabled" escapeHtml=true/>">-->
<#--                                            <i class="bi bi-exclamation-triangle-fill text-secondary"></i>-->
<#--                                        </a>-->
<#--                                        <@s.text name="admin.home.editOrganisations"/>-->
<#--                                    </h5>-->
<#--                                </div>-->
<#--                            <#else>-->
<#--                                <a href="/admin/organisations" title="" class="admin-col-listing-item border-xl-right border-xl-max-bottom">-->
<#--                                    <div class="admin-icon-wrapper">-->
<#--                                        <i class="bi bi-building admin-icon"></i>-->
<#--                                    </div>-->
<#--                                    <h5 class="admin-card-title fw-400">-->
<#--                                        <@s.text name="admin.home.editOrganisations"/>-->
<#--                                    </h5>-->
<#--                                </a>-->
<#--                            </#if>-->
<#--                        </div>-->
<#--                        <div class="col-xl-3 col-12">-->
<#--                            <a href="/admin/extensions" title="" class="admin-col-listing-item border-xl-right border-xl-max-bottom">-->
<#--                                <div class="admin-icon-wrapper">-->
<#--                                    <i class="bi bi-collection-fill admin-icon"></i>-->
<#--                                </div>-->
<#--                                <h5 class="admin-card-title fw-400">-->
<#--                                    <@s.text name="admin.home.manageExtensions"/>-->
<#--                                </h5>-->
<#--                            </a>-->
<#--                        </div>-->
<#--                        <div class="col-xl-3 col-12">-->
<#--                            <a href="/admin/schemas" title="" class="admin-col-listing-item border-xl-right border-xl-max-bottom">-->
<#--                                <div class="admin-icon-wrapper">-->
<#--                                    <i class="bi bi-columns-gap admin-icon"></i>-->
<#--                                </div>-->
<#--                                <h5 class="admin-card-title fw-400">-->
<#--                                    <@s.text name="admin.home.manageSchemas"/>-->
<#--                                </h5>-->
<#--                            </a>-->
<#--                        </div>-->
<#--                        <div class="col-xl-3 col-12">-->
<#--                            <a href="/admin/logs" title="" class="admin-col-listing-item">-->
<#--                                <div class="admin-icon-wrapper">-->
<#--                                    <i class="bi bi-journal-text admin-icon"></i>-->
<#--                                </div>-->
<#--                                <h5 class="admin-card-title fw-400">-->
<#--                                    <@s.text name="admin.home.manageLogs"/>-->
<#--                                </h5>-->
<#--                            </a>-->
<#--                        </div>-->
                    </div>
                </div>
            </div>
        </div>
    </div>

</main>

<#include "../inc/footer.ftl">
