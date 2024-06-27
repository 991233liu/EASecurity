package com.easecurity.core.basis.b;

import com.easecurity.db.BaseEnum;

/**
 * Org类下所有枚举类
 */
public class OrgEnum {

    /**
     * 类型<br>
     */
    public enum Type implements BaseEnum {
        ROOT("root", "根"), //
        COMPANY("10", "公司"), //
        DEPARTMENT("20", "部门"), //
        INSTITUTIONS("30", "机构"), //
        OFFICE("40", "办事处"), //
        TEMP("50", "临时");

        public final String id;
        public final String title;

        private Type(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public String getId() {
            return this.id;
        }
    }

    /**
     * 状态<br>
     */
    public enum Status {
        ENABLED("ENABLED", "启用"), //
        DISABLED("DISABLED", "禁用"), //
        DEREGISTRATION("DEREGISTRATION", "注销"), //
        DELETION("DEREGISTRATION", "删除");

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
}
