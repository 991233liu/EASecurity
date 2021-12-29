<div class="col-md-3 title">
    <g:message code="${beanClass}.${property}" default="${label}"/>
    <g:if test="${required}">*</g:if>
</div>
<g:if test="${type == java.util.Set}">
    <div class="col-md-9 data text text-ay">
        <f:widget property="${property}" class="form-control"/>
    </div>
</g:if><g:else>
    <div class="col-md-9 data text">
        <g:if test="${((value instanceof Number) && !(value instanceof Boolean))}">
            <g:if test="${required}">
                <g:textField name="${property}" value="${value}" required="" class="form-control"/>
            </g:if><g:else>
            <g:textField name="${property}" value="${value}" class="form-control"/>
        </g:else>
        </g:if><g:elseif test="${value instanceof Boolean}">
            <f:widget property="${property}"/>
        </g:elseif>
        <g:elseif test="${type.isEnum()}">
%{--            ${(value instanceof Enum)}--}%
%{--            <f:widget property="${property}" class="form-control"/>--}%
%{--            <f:widget property="${property}"/>--}%
            <g:select name="${property}"
                      from="${type.values()}"
                      value="${value}"
                      noSelection="${['':'']}"
                      valueMessagePrefix="${beanClass}.${property}"
                      class="form-control"/>
%{--                      optionKey="index" />--}%
        </g:elseif><g:else>
            <f:widget property="${property}" class="form-control"/>
        </g:else>
    </div>
</g:else>
<!-- .form-group row -->
