[#ftl output_format="HTML"]
[#setting date_format="yyyy-MM-dd"]
[#setting time_format="HH:mm:ss"]
[#setting datetime_format="iso"]
[#--[#setting locale="${locale}"]--]
[#setting locale="en"]
[#setting url_escaping_charset="UTF-8"]
<!DOCTYPE html>
<html lang="en" class="h-100">
<head>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" type="text/css" href="/styles/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="/styles/bootstrap/bootstrap-borders.css" />
    <link rel="stylesheet" type="text/css" href="/styles/dataTables/dataTables.bootstrap5-1.10.23.min.css" />

    <!-- Bootstrap icons -->
    <link rel="stylesheet" type="text/css" href="/styles/bootstrap-icons/font/bootstrap-icons.css" />

    <!-- IPT CSS -->
    <link rel="stylesheet" type="text/css" href="/styles/main.css" />

    <!-- Custom CSS for customizations -->
    <link rel="stylesheet" type="text/css" href="/styles/custom.css" />

    <!-- Favicon -->
    <link rel="shortcut icon" href="/images/icons/favicon-16x16.png" type="image/x-icon" />

    <!-- RSS Feed -->
    <link href="/rss.do" title="Latest Resources" rel="alternate" type="application/rss+xml" />

    <!-- JS and JQuery -->
    <script src="/js/jquery/jquery-3.5.1.min.js"></script>
    <script src="/js/jquery/jquery-ui.min-1.12.1.js"></script>
    <script src="/js/global.js"></script>


[#--    --][#-- GOOGLE ANALYTICS - asynchroneous: http://code.google.com/apis/analytics/docs/tracking/asyncTracking.html --]
[#--    [#if cfg.gbifAnalytics || (cfg.analyticsKey!"")?length>1]--]
[#--        <script>--]
[#--            var _gaq = _gaq || [];--]
[#--            [#if (cfg.analyticsKey!"")?length>1]--]
[#--            _gaq.push(['_setAccount', '${cfg.analyticsKey}']);--]
[#--            _gaq.push(['_trackPageview']);--]
[#--            [/#if]--]
[#--            [#if cfg.gbifAnalytics]--]
[#--            _gaq.push(['gbif._setAccount', '${cfg.getProperty("dev.analytics.gbif")}']);--]
[#--            _gaq.push(['gbif._trackPageview']);--]
[#--            [/#if]--]
[#--            (function() {--]
[#--                var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;--]
[#--                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';--]
[#--                var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);--]
[#--            })();--]
[#--        </script>--]
[#--    [/#if]--]

[#--    --][#-- If not logged, refresh page periodically to avoid CSRF token expiration --]
[#--    [#if !loggedIn]--]
[#--        <script>--]
[#--            setTimeout(function(){--]
[#--                window.location.reload();--]
[#--            }, ${cfg.getCsrfPageRefreshDelay()?c});--]
[#--        </script>--]
[#--    [/#if]--]

[#--    --][#-- Metadata used by browsers (title in browser toolbar, bookmark when added to favorites), search engines (keywords) --]
[#--    [#assign metaKeywords = "GBIF, Global Biodiversity Information Facility, IPT, Integrated Publishing Toolkit, checklist, occurrence, metadata, DwC-A, Darwin Core, Darwin Core Archive, biodiversity data, data paper, EML" /]--]
[#--    [#assign registeredIpt = action.getRegisteredIpt()!""/]--]
[#--    [#if resource?? && eml??]--]
[#--        <meta name="description" content="${eml.description!}" />--]
[#--        [#if eml.subject?has_content]--]
[#--            <meta name="keywords" content="${eml.subject?replace(";", ",")}" />--]
[#--        [/#if]--]
[#--        <meta name="foaf:topic" content="${cfg.getResourceUri(resource.shortname)}/#dataset"/>--]
[#--        <meta name="foaf:isPrimaryTopicOf" content="${cfg.getResourceUri(resource.shortname)}">--]
[#--    [#elseif registeredIpt?has_content]--]
[#--        <meta name="description" content="${registeredIpt.description!}" />--]
[#--        <meta name="keywords" content="${metaKeywords}" />--]
[#--    [#else]--]
[#--        <meta name="description" content="The Integrated Publishing Toolkit (IPT) is a tool developed by the Global Biodiversity Information Facility (GBIF) to provide an easy and efficient way of publishing biodiversity data." />--]
[#--        <meta name="keywords" content="${metaKeywords}" />--]
[#--    [/#if]--]
[#--    <meta name="generator" content="IPT ${cfg.version!}" />--]
[#--    <meta name="inventory" content="${baseURL}/inventory/dataset"/>--]
[#--    <meta name="foaf:seeAlso" content="${baseURL}/dcat"/>--]
[#--    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />--]
[#--    <meta name="viewport" content="width=device-width, initial-scale=1">--]

    [#assign currentMenu = "home"]
    [#import "/macros/messages.ftl" as s]
