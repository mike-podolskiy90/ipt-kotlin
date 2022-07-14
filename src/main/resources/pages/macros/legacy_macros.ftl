<#ftl output_format="HTML">

<#macro text name>
    <@spring.message name/>
</#macro>

<#macro property value><#rt>
    <#if value??>${value?eval}</#if><#t>
</#macro>

<#macro submit name key form="" cssClass="" cssStyle="" id="${name}">
    <input type="submit" id="${id}" value="<@text name='${key}'/>" name="${name}" class="${cssClass}" style="${cssStyle}" <#if form?has_content>form="${form}"</#if> />
</#macro>

<#macro hidden name value required>
    <input type="hidden" name="${name}" value="${value}" id="${name}" required="${required}">
</#macro>

<#macro fielderror id cssClass fieldName>
    <#if fieldErrors?? && fieldErrors?has_content && fieldErrors[fieldName]?has_content>
        <ul id="${id}" class="${cssClass}">
            <#list fieldErrors[fieldName] as fieldError>
                <li>
                    <span>${fieldError}</span>
                </li>
            </#list>
        </ul>
    </#if>
</#macro>
