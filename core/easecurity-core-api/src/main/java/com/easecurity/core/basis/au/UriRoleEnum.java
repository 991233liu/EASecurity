package com.easecurity.core.basis.au;

/**
 * UriRole类下所有枚举类
 */
public class UriRoleEnum {
    /**
     * 来源<br>
     */
    public enum FromTo {
        MANUALLY("10", "人工"), //
        SOURCECODE("20", "程序源代码");

        public final String id;
        public final String title;

        private FromTo(String id, String title) {
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
}
