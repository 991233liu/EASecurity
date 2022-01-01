package com.easecurity.admin

import grails.util.TypeConvertingMap
import grails.web.mapping.LinkGenerator
import grails.web.mapping.UrlMapping
import org.grails.encoder.CodecLookup
import org.grails.encoder.Encoder
import org.grails.taglib.TagOutput
import org.grails.taglib.encoder.OutputContextLookupHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.support.RequestContextUtils

class FormTagLib {
    static namespace = "s"
//    static defaultEncodeAs = [taglib:'html']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    CodecLookup codecLookup

    @Autowired
    LinkGenerator linkGenerator

    /**
     * Renders a sortable column to support sorting in list views.<br/>
     *
     * Attribute title or titleKey is required. When both attributes are specified then titleKey takes precedence,
     * resulting in the title caption to be resolved against the message source. In case when the message could
     * not be resolved, the title will be used as title caption.<br/>
     *
     * Examples:<br/>
     *
     * &lt;g:sortableColumn property="title" title="Title" /&gt;<br/>
     * &lt;g:sortableColumn property="title" title="Title" style="width: 200px" /&gt;<br/>
     * &lt;g:sortableColumn property="title" titleKey="book.title" /&gt;<br/>
     * &lt;g:sortableColumn property="releaseDate" defaultOrder="desc" title="Release Date" /&gt;<br/>
     * &lt;g:sortableColumn property="releaseDate" defaultOrder="desc" title="Release Date" titleKey="book.releaseDate" /&gt;<br/>
     *
     * @emptyTag
     *
     * @attr property - name of the property relating to the field
     * @attr defaultOrder default order for the property; choose between asc (default if not provided) and desc
     * @attr title title caption for the column
     * @attr titleKey title key to use for the column, resolved against the message source
     * @attr params a map containing request parameters
     * @attr action the name of the action to use in the link, if not specified the list action will be linked
     * @attr namespace controller namespace (optional)
     * @attr mapping A map containing URL query parameters
     * @attr class CSS class name
     */
    Closure sortableColumn = { Map attrs ->
        def writer = out
        if (!attrs.property) {
            throwTagError("Tag [sortableColumn] is missing required attribute [property]")
        }

        if (!attrs.title && !attrs.titleKey) {
            throwTagError("Tag [sortableColumn] is missing required attribute [title] or [titleKey]")
        }

        def property = attrs.remove("property")
        def action = attrs.action ? attrs.remove("action") : (actionName ?: "list")
        def namespace = attrs.remove("namespace")

        def defaultOrder = attrs.remove("defaultOrder")
        if (defaultOrder != "desc") defaultOrder = "asc"

        // current sorting property and order
        def sort = params.sort
        def order = params.order

        // add sorting property and params to link params
        Map linkParams = [:]
        if (params.id) linkParams.put("id", params.id)
        def paramsAttr = attrs.remove("params")
        if (paramsAttr instanceof Map) linkParams.putAll(paramsAttr)
        linkParams.sort = property

        // propagate "max" and "offset" standard params
        if (params.max) linkParams.max = params.max
        if (params.offset) linkParams.offset = params.offset

        // determine and add sorting order for this column to link params
        attrs['class'] = (attrs['class'] ? "${attrs['class']} sortable" : "sortable")
        if (property == sort) {
            attrs['class'] = (attrs['class'] as String) + " sorted " + order
            if (order == "asc") {
                linkParams.order = "desc"
            } else {
                linkParams.order = "asc"
            }
        } else {
            linkParams.order = defaultOrder
        }

        // determine column title
        String title = attrs.remove("title") as String
        String titleKey = attrs.remove("titleKey") as String
        Object mapping = attrs.remove('mapping')
        if (titleKey) {
            if (!title) title = titleKey
            def messageSource = grailsAttributes.messageSource
            def locale = RequestContextUtils.getLocale(request)
            title = messageSource.getMessage(titleKey, null, title, locale)
        }

        writer << "<th "
        // process remaining attributes
        Encoder htmlEncoder = codecLookup.lookupEncoder('HTML')
        attrs.each { k, v ->
            writer << k
            writer << "=\""
            writer << htmlEncoder.encode(v)
            writer << "\" "
        }
        writer << '>'
        Map linkAttrs = [:]
        linkAttrs.params = linkParams
        if (mapping) {
            linkAttrs.putAll(mapping)
//            linkAttrs.mapping = mapping
        }

        linkAttrs.action = action
        linkAttrs.namespace = namespace

        writer << callLink((Map) linkAttrs) {
            title
        }
        writer << '</th>'
    }

    /**
     * Creates next/previous links to support pagination for the current controller.<br/>
     *
     * &lt;g:paginate total="${Account.count()}" /&gt;<br/>
     *
     * @emptyTag
     *
     * @attr total REQUIRED The total number of results to paginate
     * @attr action the name of the action to use in the link, if not specified the default action will be linked
     * @attr controller the name of the controller to use in the link, if not specified the current controller will be linked
     * @attr id The id to use in the link
     * @attr params A map containing request parameters
     * @attr prev The text to display for the previous link (defaults to "Previous" as defined by default.paginate.prev property in I18n messages.properties)
     * @attr next The text to display for the next link (defaults to "Next" as defined by default.paginate.next property in I18n messages.properties)
     * @attr omitPrev Whether to not show the previous link (if set to true, the previous link will not be shown)
     * @attr omitNext Whether to not show the next link (if set to true, the next link will not be shown)
     * @attr omitFirst Whether to not show the first link (if set to true, the first link will not be shown)
     * @attr omitLast Whether to not show the last link (if set to true, the last link will not be shown)
     * @attr max The number of records displayed per page (defaults to 10). Used ONLY if params.max is empty
     * @attr maxsteps The number of steps displayed for pagination (defaults to 10). Used ONLY if params.maxsteps is empty
     * @attr offset Used only if params.offset is empty
     * @attr mapping The named URL mapping to use to rewrite the link
     * @attr fragment The link fragment (often called anchor tag) to use
     */
    def paginate = { attrs, body ->
        TypeConvertingMap tcm = (TypeConvertingMap) attrs
        if (tcm.total == null) {
            throwTagError("Tag [paginate] is missing required attribute [total]")
        }

        def messageSource = grailsAttributes.messageSource
        def locale = RequestContextUtils.getLocale(request)

        def total = tcm.int('total') ?: 0
        def offset = params.int('offset') ?: 0
        def max = params.int('max')
        def maxsteps = (tcm.int('maxsteps') ?: 10)

        if (!offset) offset = (tcm.int('offset') ?: 0)
        if (!max) max = (tcm.int('max') ?: 10)

        Map linkParams = [:]
        if (tcm.params instanceof Map) linkParams.putAll((Map) tcm.params)
        linkParams.offset = offset - max
        linkParams.max = max
        if (params.sort) linkParams.sort = params.sort
        if (params.order) linkParams.order = params.order

        Map linkTagtcm = [:]
        def action
        if (tcm.containsKey('mapping')) {
//            linkTagtcm.mapping = tcm.mapping
            linkTagtcm.putAll(tcm.mapping)
            action = tcm.action
        } else {
            action = tcm.action ?: params.action
        }
        if (action) {
            linkTagtcm.action = action
        }
        if (tcm.controller) {
            linkTagtcm.controller = tcm.controller
        }
        if (attrs.containsKey(UrlMapping.PLUGIN)) {
            linkTagtcm.put(UrlMapping.PLUGIN, tcm.get(UrlMapping.PLUGIN))
        }
        if (tcm.containsKey(UrlMapping.NAMESPACE)) {
            linkTagtcm.put(UrlMapping.NAMESPACE, tcm.get(UrlMapping.NAMESPACE))
        }
        if (tcm.id != null) {
            linkTagtcm.id = tcm.id
        }
        if (tcm.fragment != null) {
            linkTagtcm.fragment = tcm.fragment
        }
        linkTagtcm.params = linkParams

        // determine paging variables
        def steps = maxsteps > 0
        int currentstep = ((offset / max) as int) + 1
        int firststep = 1
        int laststep = Math.round(Math.ceil((total / max) as double)) as int
        // TODO 判断是否需要展示首页
        if (laststep > 5) {
            linkParams.offset = 0
            out << "<li>"
            out << callLink((Map) linkTagtcm.clone()) {
                messageSource.getMessage('paginate.first', null, messageSource.getMessage('default.paginate.first', null, 'First', locale), locale)
            }
            out << "</li>"

//            out << """<li><a href="#" aria-label="Previous">首页</a></li>"""
        }

        // display previous link when not on firststep unless omitPrev is true
        if (currentstep > firststep && !tcm.boolean('omitPrev')) {
//            linkTagtcm.put('class', 'prevLink')
            linkParams.offset = offset - max
            out << "<li>"
            out << callLink((Map) linkTagtcm.clone()) {
                (tcm.prev ?: messageSource.getMessage('paginate.prev', null, messageSource.getMessage('default.paginate.prev', null, 'Previous', locale), locale))
            }
            out << "</li>"
        }

        // display steps when steps are enabled and laststep is not firststep
        if (steps && laststep > firststep) {
//            linkTagtcm.put('class', 'step')

            // determine begin and endstep paging variables
            int beginstep = currentstep - (Math.round(maxsteps / 2.0d) as int) + (maxsteps % 2)
            int endstep = currentstep + (Math.round(maxsteps / 2.0d) as int) - 1

            if (beginstep < firststep) {
                beginstep = firststep
                endstep = maxsteps
            }
            if (endstep > laststep) {
                beginstep = laststep - maxsteps + 1
                if (beginstep < firststep) {
                    beginstep = firststep
                }
                endstep = laststep
            }

            // display firststep link when beginstep is not firststep
            if (beginstep > firststep && !tcm.boolean('omitFirst')) {
                linkParams.offset = 0
                out << callLink((Map) linkTagtcm.clone()) { firststep.toString() }
            }
            //show a gap if beginstep isn't immediately after firststep, and if were not omitting first or rev
            if (beginstep > firststep + 1 && (!tcm.boolean('omitFirst') || !tcm.boolean('omitPrev'))) {
                out << '<span class="step gap">..</span>'
            }

            // display paginate steps
            (beginstep..endstep).each { int i ->
                if (currentstep == i) {
                    out << "<li><span class=\"currentStep\">${i}</span></li>"
                } else {
                    linkParams.offset = (i - 1) * max
                    out << "<li>"
                    out << callLink((Map) linkTagtcm.clone()) { i.toString() }
                    out << "</li>"
                }
            }

            //show a gap if beginstep isn't immediately before firststep, and if were not omitting first or rev
            if (endstep + 1 < laststep && (!tcm.boolean('omitLast') || !tcm.boolean('omitNext'))) {
                out << '<span class="step gap">..</span>'
            }
            // display laststep link when endstep is not laststep
            if (endstep < laststep && !tcm.boolean('omitLast')) {
                linkParams.offset = (laststep - 1) * max
                out << callLink((Map) linkTagtcm.clone()) { laststep.toString() }
            }
        }

        // display next link when not on laststep unless omitNext is true
        if (currentstep < laststep && !tcm.boolean('omitNext')) {
//            linkTagtcm.put('class', 'nextLink')
            linkParams.offset = offset + max
            out << "<li>"
            out << callLink((Map) linkTagtcm.clone()) {
                (tcm.next ? tcm.next : messageSource.getMessage('paginate.next', null, messageSource.getMessage('default.paginate.next', null, 'Next', locale), locale))
            }
            out << "</li>"
        }

        // TODO 判断是否需要展示最后一页
        if (laststep > 5) {
            linkParams.offset = (laststep - 1) * max
            out << "<li>"
            out << callLink((Map) linkTagtcm.clone()) {
                messageSource.getMessage('paginate.last', null, messageSource.getMessage('default.paginate.last', null, 'Last', locale), locale)
            }
            out << "</li>"

//            out << """<li><a href="#" aria-label="Next">尾页</a></li>"""
        }

        if (total > 0) {

//                    <li><a href="#">上一页</a></li>
//                    <li><a href="#">1</a></li>
//                    <li><a href="#">2</a></li>
//                    <li><a href="#">3</a></li>
//                    <li><a href="#">4</a></li>
//                    <li><a href="#">5</a></li>
//                    <li><a href="#">下一页</a></li>
//            out << body() << html.toString()
        } else {
            out << body() << "&nbsp;"
        }
    }

    private callLink(Map attrs, Object body) {
        TagOutput.captureTagOutput(tagLibraryLookup, 'g', 'link', attrs, body, OutputContextLookupHelper.lookupOutputContext())
    }
}
