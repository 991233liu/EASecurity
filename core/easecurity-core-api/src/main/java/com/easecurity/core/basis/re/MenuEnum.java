package com.easecurity.core.basis.re;

/**
 * Menu类下所有枚举类
 */
public class MenuEnum {

    /**
     * 状态<br>
     */
    public enum Status {
        ENABLED("10", "启用"), //
        DISABLED("20", "禁用");

        public final String id;
        public final String title;

        private Status(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public String getId() {
            return this.id;
        }
    }

    /**
     * 显示状态<br>
     */
    public enum DisplayStatus {
        DISPLAY("10", "始终显示"), //
        HIDDEN("20", "始终隐藏"), //
        DISABLEDHIDDEN("30", "禁用隐藏"), //
        NOPERMISSIONSHIDDEN("40", "无权限隐藏");

        public final String id;
        public final String title;

        private DisplayStatus(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public String getId() {
            return this.id;
        }
    }

    /**
     * 类型<br>
     */
    public enum AccessType {
        ANONYMOUS("10", "匿名访问"), //
        LOGIN("20", "登录用户访问"), //
        AUTHORIZATION("30", "授权访问");

        public final String id;
        public final String title;

        private AccessType(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public String getId() {
            return this.id;
        }

    }

    /**
     * 菜单级别<br>
     */
    public enum Level {
        ROOT("root", "根"), //
        FIRST("10", "一级"), //
        SECOND("20", "二级"), //
        THIRD("30", "三级"), //
        FOURTH("40", "四级"), //
        FIFTH("50", "五级");

        public final String id;
        public final String title;

        private Level(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public String getId() {
            return this.id;
        }
    }
}
