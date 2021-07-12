<dl>
	<g:each in="${domainProperties}" var="p">
%{--		<g:if test="${p.type == java.util.Set}">--}%
%{--			<div class="col-md-3 title"><g:message code="${domainClass.name}.${p.name}"--}%
%{--												   default="${p.defaultLabel}" /></div>--}%
%{--			<div class="col-md-9 data data-ay text">--}%
%{--				${body(p)}--}%
%{--			</div>--}%
%{--		</g:if> <g:else>--}%
%{--			<div class="col-md-3 title"><g:message code="${domainClass.name}.${p.name}"--}%
%{--												   default="${p.defaultLabel}" /></div>--}%
%{--			<div class="col-md-9 data text">--}%
%{--				${body(p)}--}%
%{--			</div>--}%
%{--		</g:else>--}%
%{--		--}%
		<div class="col-md-3 title"><g:message code="${domainClass.name}.${p.name}"
											   default="${p.defaultLabel}" /></div>
		<g:if test="${p.type == java.util.Set}">
			<div class="col-md-9 data text text-ay">
				${body(p)}
			</div>
		</g:if><g:else>
			<div class="col-md-9 data text">
				${body(p)}
			</div>
		</g:else>
	</g:each>
</dl>
