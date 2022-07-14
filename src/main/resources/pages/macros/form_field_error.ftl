<#ftl output_format="HTML">
<#import "/macros/legacy_macros.ftl" as s>
<#if errorfield=="">
    <@s.fielderror id="field-error-${name}" cssClass="invalid-feedback list-unstyled field-error my-1" fieldName="${name}"/>
<#else>
    <@s.fielderror id="field-error-${name}" cssClass="invalid-feedback list-unstyled field-error my-1" fieldName="${errorfield}"/>
</#if>
