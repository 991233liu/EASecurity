<div class="col-md-3 title">
	<g:message code="${bean.getClass().canonicalName}.${property}" default="${label}" />
	<g:if test="${required}">*</g:if>
</div>
<div class="col-md-9 data text">
	<f:widget property="${property}"/>
</div>
<!-- .form-group row -->
