<#if (actionMessages?exists && actionMessages?size > 0)>
	<div id="msg"><strong>��ʾ��</strong><#list actionMessages as message>${message}</#list></div>
</#if>
<#if (actionErrors?exists && actionErrors?size > 0)>
	<div id="msg"><strong>����</strong><#list actionErrors as error>${error}</#list></div>
</#if>