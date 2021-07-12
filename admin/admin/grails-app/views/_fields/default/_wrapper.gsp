<div class="col-md-3 title">
	<g:message code="${beanClass}.${property}" default="${label}" />
	<g:if test="${required}">*</g:if>
</div>
<g:if test="${type == java.util.Set}">
	<div class="col-md-9 data text text-ay">
		<f:widget property="${property}" class="form-control"/>
	</div>
</g:if><g:else>
	<div class="col-md-9 data text">
		<g:if test="${((value instanceof Number) && !(value instanceof Boolean))}">
		%{--		${constraints.callMethod('enums',constraints)}--}%
			<g:if test="${required}">
				<g:textField name="${property}" value="${value}" required="" class="form-control"/>
			</g:if><g:else>
			<g:textField name="${property}" value="${value}" class="form-control"/>
		</g:else>
		</g:if><g:elseif test="${value instanceof Boolean}">
			<f:widget property="${property}"/>
		</g:elseif><g:else>
			<f:widget property="${property}" class="form-control"/>
		</g:else>
	</div>
</g:else>
<!-- .form-group row -->
