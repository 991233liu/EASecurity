package com.easecurity.admin

class EnumClassController {

    def show() {
        def results = []
        if (params.id instanceof String && params.id.indexOf('.') > -1)
            results = Class.forName(params.id).values()
        else
            results = Class.forName(params.id + '.' + params.format).values()
        List items = []
        results.each { it ->
            Map<String, Object> map = [:]
            map.id = it.id
            map.code = it.name()
            map.title = it.title
            items.add(map)
        }
        respond [items: items], model: []
    }
}
