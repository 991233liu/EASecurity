<script>
    var menuJson = [
        {
            "id": 3,
            "name": "首页",
            "code": "Home",
            "icon": "class=\"fa fa-dashboard\"",
            "active":${'Home'==openMenu?'true':'false'},
            "level": {
                "enumType": "com.easecurity.core.basis.re.MenuEnum$Level",
                "name": "FIRST"
            },
            "sortNumber": 1,
            "fullName": "admin系统菜单/首页/",
            "displayStatus": {
                "enumType": "com.easecurity.core.basis.re.MenuEnum$DisplayStatus",
                "name": "NOPERMISSIONSHIDDEN"
            },
            "disablePrompt": null,
            "parentId": 2,
            "accessType": {
                "enumType": "com.easecurity.core.basis.re.MenuEnum$AccessType",
                "name": "AUTHORIZATION"
            },
            "openUrl": "/admin/home/index",
            "noPermissionsPrompt": null,
            "status": {
                "enumType": "com.easecurity.core.basis.re.MenuEnum$Status",
                "name": "ENABLED"
            }
        },
        <sec:access expression="hasRole('ROLE_root#admin')||hasRole('ROLE_PM')">
        {
            "code": "basis",
            "level": {
                "enumType": "com.easecurity.core.basis.re.MenuEnum$Level",
                "name": "FIRST"
            },
            "sortNumber": 2,
            "icon": "class=\"fa fa-user-plus\"",
            "fullName": "admin系统菜单/基础数据/",
            "displayStatus": null,
            "disablePrompt": null,
            "parentId": 2,
            "accessType": null,
            "openUrl": null,
            "children": [
                {
                    "code": "UserList",
                    "level": {
                        "enumType": "com.easecurity.core.basis.re.MenuEnum$Level",
                        "name": "SECOND"
                    },
                    "sortNumber": 4,
                    "icon": "class=\"fa fa-circle-o\"",
                    "fullName": "admin系统菜单/基础数据/用户管理/",
                    "displayStatus": {
                        "enumType": "com.easecurity.core.basis.re.MenuEnum$DisplayStatus",
                        "name": "NOPERMISSIONSHIDDEN"
                    },
                    "disablePrompt": null,
                    "parentId": 6,
                    "accessType": {
                        "enumType": "com.easecurity.core.basis.re.MenuEnum$AccessType",
                        "name": "AUTHORIZATION"
                    },
                    "openUrl": "/admin/user/index",
                    "name": "用户管理",
                    "noPermissionsPrompt": null,
                    "id": 5,
                    "status": {
                        "enumType": "com.easecurity.core.basis.re.MenuEnum$Status",
                        "name": "ENABLED"
                    }
                }
            ],
            "name": "基础数据",
            "noPermissionsPrompt": null,
            "id": 6,
            "status": null
        },
        </sec:access>
        {
            "code": "systemSetting",
            "level": {
                "enumType": "com.easecurity.core.basis.re.MenuEnum$Level",
                "name": "FIRST"
            },
            "sortNumber": 3,
            "icon": "class=\"fa fa-cogs\"",
            "fullName": "admin系统菜单/系统管理/",
            "displayStatus": {
                "enumType": "com.easecurity.core.basis.re.MenuEnum$DisplayStatus",
                "name": "NOPERMISSIONSHIDDEN"
            },
            "disablePrompt": null,
            "parentId": 2,
            "accessType": {
                "enumType": "com.easecurity.core.basis.re.MenuEnum$AccessType",
                "name": "AUTHORIZATION"
            },
            "openUrl": null,
            "name": "系统管理",
            "noPermissionsPrompt": null,
            "id": 4,
            "status": {
                "enumType": "com.easecurity.core.basis.re.MenuEnum$Status",
                "name": "ENABLED"
            }
        }
    ];
    menuObj.loadMenu(menuJson,'${activeMenu}');
</script>