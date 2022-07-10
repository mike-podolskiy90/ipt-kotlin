<#ftl output_format="HTML">

<#macro text name>
    <@spring.message name/>
</#macro>

<#macro property value><#rt>
    <#if value??>${value?eval}</#if><#t>
</#macro>

<#macro submit name key form="" cssClass="" cssStyle="" id="${name}">
    <input type="submit" id="${id}" value="<@text name='${key}'/>" name="${name}" class="${cssClass}" style="${cssStyle}" <#if form??>form="${form}"</#if> />
</#macro>

<#macro hidden name value required>
    <input type="hidden" name="${name}" value="${value}" id="${name}" required="${required}">
</#macro>
