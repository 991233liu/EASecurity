/*! common.js
 * ================
 * 框架js，公共的js
 *
 */

/*
 * 全局函数
 */
var $g = {

    /**
     * @public
     * 选中/取消，所有复选框
     */
    selectAll: function (obj) {
        if (obj.checked == true) {
            $.each($('input:checkbox[name=' + obj.id.replace('_all', '') + ']').get(), function (index, it) {
                it.checked = true;
            });
        } else {
            $.each($('input:checkbox[name=' + obj.id.replace('_all', '') + ']').get(), function (index, it) {
                it.checked = false;
            });
        }
    }
}

/*
 * 菜单相关
 */
var menuObj = {
    menuList: [],
    config: {
        pmenu_icon_class: "fa",
        lmenu_icon_class: "fa",
        pmenu_expand_class: "fa fa-angle-left pull-right",
        label_icon_class: "label pull-right",
        // treeviewHtml:'<li class="treeview"> <a href="#"> <i class="fa fa-pie-chart"></i> <span>XXX</span> <span class="pull-right-container"> <i class="fa fa-angle-left pull-right"></i></span> </a> <ul class="treeview-menu"> </ul> </li>',
        treeviewHtml:'<li class="treeview"></li>',
        treeviewHtml_ico:'<i class="fa fa-files-o"></i>',
        treeviewHtml_pull:'<span class="pull-right-container"><i class="fa fa-angle-left pull-right"></i></span>',
        menuHtml:'<li id=""></li>',
        menuHtml_ico:'<i class="fa fa-circle-o"></i>'
    },
    loadMenu: function (menuJson) {
        this.setMenuList(menuJson);
    },
    loadMenuUrl: function (url) {
        $.ajax({
            url: url,
            method: "GET",
            asyn: true,
            dataType: "json",
            success: (response) => {
                this.setMenuList(response);
            }
        });
    },
    getMenuList: function () {
        return this.menuList;
    },
    setMenuList: function (menuList) {
        this.menuList = menuList;
        this._showMenuList();
    },
    _showMenuList: function () {
        if (this.menuList != null && this.menuList.length > 0) {
            var menuList = this.menuList;
            var menuContainer = $(".sidebar-menu");
            menuContainer.html('<li class="header">菜单</li>');
            for (var i = 0; i < menuList.length; i++) {
                var menu = menuList[i];
                var mHtml = this._generateMenuHtml(menu);
                menuContainer.append(mHtml);
            }
        }
    },
    _generateMenuHtml: function (menu) {
        var m_html = null;
        if (menu.children == null || menu.children.length == 0) {
            //叶子菜单
            var li = $(this.config.menuHtml);
            m_html = li;
            li.prop("id", menu.id);
            if(menu.active){
                li.addClass("active");
            }
            var a = $("<a></a>");
            a.attr("href", "javascript:;");
            var menu_str = JSON.stringify(menu);
            a.attr("onclick", "navigateTo(\'" + menu_str + "\')");
            var ico = $(this.config.menuHtml_ico);
            if (menu.ico != null && menu.ico != '') {
                ico.removeClass();
                ico.addClass(menu.ico);
            }
            // var i = $("<i></i>");
            // i.css("margin-right", "8px");
            // i.css("width", "14px");
            // i.css("height", "14px");
            // if (menu.icon != null && menu.icon != '') {
            //     i.addClass(this.config.lmenu_icon_class);
            //     i.css("background", "url(" + menu.icon + ")  no-repeat");
            //     i.css("background-size", "14px 14px");
            //     i.css("background-position", "center center");
            // } else {
            //     i.addClass(this.config.lmenu_icon_class);
            //     i.addClass("fa-circle-o");
            // }
            a.append(ico);
            var span = $("<span></span>");
            span.html(menu.name);
            a.append(span);
            // if (menu.label != null && menu.label.name != null && menu.label.name != '') {
            //     var label = menu.label;
            //     var small = $("<small></small>");
            //     small.html(label.name);
            //     if (label.bgColor != null && label.bgColor != '') {
            //         small.addClass(this.config.label_icon_class);
            //         small.css("background-color", label.bgColor + " !important");
            //     } else {
            //         small.addClass(this.config.label_icon_class);
            //         small.addClass("bg-green");
            //     }
            //     a.append(small);
            // }
            li.append(a);
        } else {
            var li = $(this.config.treeviewHtml);
            m_html = li;
            // li.addClass("treeview");
            var a = $("<a></a>");
            a.attr("href", "javascript:;");
            li.append(a);
            var ico = $(this.config.treeviewHtml_ico);
            if (menu.ico != null && menu.ico != '') {
                ico.removeClass();
                ico.addClass(menu.ico);
            }
            // i.css("margin-right", "8px");
            // i.css("width", "14px");
            // i.css("height", "14px");
            // if (menu.icon != null && menu.icon != '') {
            //     i.addClass(this.config.lmenu_icon_class);
            //     i.css("background", "url(" + menu.icon + ")  no-repeat");
            //     i.css("background-size", "14px 14px");
            //     i.css("background-position", "center center");
            // } else {
            //     i.addClass(this.config.pmenu_icon_class);
            //     i.addClass("fa-files-o");
            // }
            a.append(ico);
            var span = $("<span></span>");
            span.html(menu.name);
            a.append(span);
            var pull=$(this.config.treeviewHtml_pull);
            // if (menu.label != null && menu.label.name != null && menu.label.name != '') {
            //     var label = menu.label;
            //     var span = $("<span></span>");
            //     span.html(label.name);
            //     if (label.bgColor != null && label.bgColor != '') {
            //         span.addClass(this.config.label_icon_class);
            //         span.css("background-color", label.bgColor + " !important");
            //     } else {
            //         span.addClass(this.config.label_icon_class);
            //         span.addClass("bg-green");
            //     }
            //     a.append(span);
            // } else {
            //     var i = $("<i></i>");
            //     i.addClass(this.config.pmenu_expand_class);
            //     a.append(i);
            // }
            a.append(pull);
            var ul = $("<ul></ul>");
            ul.addClass("treeview-menu");
            li.append(ul);
            //遍历
            var mchidren = menu.children;
            for (var k = 0; k < mchidren.length; k++) {
                var childHtml = this._generateMenuHtml(mchidren[k]);
                ul.append(childHtml);
                if(mchidren[k].active){
                    li.addClass("active");
                }
            }
        }
        return m_html;
    }
};