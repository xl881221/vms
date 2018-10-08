<#if (actionMessages?exists && actionMessages?size > 0)>
	<div id="msg"><strong>ÌáÊ¾£º</strong><#list actionMessages as message>${message}</#list></div>
</#if>
<#if (actionErrors?exists && actionErrors?size > 0)>
	<div id="msg"><strong>´íÎó£º</strong><#list actionErrors as error>${error}</#list></div>
</#if>