<script>
    var menuJson=[
        {
            "id": 1,
            "name": "\u4e00\u7ea7\u83dc\u53551",
            "url": "/admin/assets/pages/examples/lockscreen.html",
            "ico":"fa fa-dashboard",
            "active":${'Home'==openMenu?'true':'false'}
        },
        {
            "id": 2,
            "name": "\u4e00\u7ea7\u83dc\u53552",
            "url": null,
            "children": [
                {
                    "id": 3,
                    "name": "\u4e8c\u7ea7\u83dc\u53551",
                    "url": "http://www.jd.com"
                },
                <sec:access expression="hasRole('ROLE_root#admin')||hasRole('ROLE_PM')">
                {
                    "id": 4,
                    "name": "\u4e8c\u7ea7\u83dc\u53552",
                    "url": "http://www.jd.com"
                },
                </sec:access>
                {
                    "id": 5,
                    "name": "\u4e8c\u7ea7\u83dc\u53553",
                    "url": "http://www.taobao.com"
                }
            ]
        }
    ];
    menuObj.loadMenu(menuJson);
</script>