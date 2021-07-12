<table class="table table-bordered table-striped table-hover dataTable">
	<thead>
		<tr>
			<th class="" style="padding-right:0px;">
				<input id="ids_all" type="checkbox" class="icheckbox_square-blue" onclick="$g.selectAll(this)">
			</th>
			<g:each in="${domainProperties}" var="p" status="i">
				<g:sortableColumn property="${p.property}" titleKey="${domainClass.name}.${p.name}" title="${p.label}" />
			</g:each>
			<th class="text-center">操作</th>
		</tr>
	</thead>
	<tbody>
		<g:each in="${collection}" var="bean" status="i">
			<tr>
				<td><input name="ids" type="checkbox" value="${bean.id}"></td>
				<g:each in="${domainProperties}" var="p" status="j">
					<g:if test="${j==0}">
						<td><g:link method="GET" resource="${bean}" >
								<g:if
									test="${bean.properties.get(p.name) instanceof java.sql.Timestamp}">
									<g:formatDate format="yyyy-MM-dd"
										date="${bean.properties.get(p.name)}" />
								</g:if>
								<g:else>
									${bean.properties.get(p.name)}
								</g:else>
							</g:link></td>
					</g:if>
					<g:else>
						<td><g:if
								test="${bean.properties.get(p.name) instanceof java.sql.Timestamp}">
								<g:formatDate format="yyyy-MM-dd"
									date="${bean.properties.get(p.name)}" />
							</g:if> <g:else>
									${bean.properties.get(p.name)}
								</g:else></td>
					</g:else>
				</g:each>
				<td class="text-center">
					<g:form resource="${bean}" method="DELETE">
						<g:link class="btn bg-olive btn-xs" action="show" resource="${bean}">
							<g:message code="default.button.show.label" default="Show" />
						</g:link>
						<g:link class="btn bg-olive btn-xs" action="edit" resource="${bean}">
							<g:message code="default.button.edit.label" default="Edit" />
						</g:link>
						<input class="btn bg-danger btn-xs" type="submit"
							   value="${message(code: 'default.button.delete.label', default: 'Delete')}"
							   onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
					</g:form>
				</td>
			</tr>
		</g:each>
	</tbody>
</table>