package com.easecurity.core.basis.b;

/**
 * User类下所有枚举类
 */
public class UserEnum {

    /**
     * 用户状态<br>
     */
    public enum AcStatus {
        ENABLED("10", "启用"), //
        DISABLED("20", "禁用"), //
        DEREGISTRATION("30", "注销"), //
        DELETION("40", "删除");

        public final String id;
        public final String title;

        private AcStatus(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public String getId() {
            return this.id;
        }
    }

    /**
     * 密码状态<br>
     */
    public enum PdStatus {
        ENABLED("10", "启用"), //
        EXPIRED("20", "过期"), //
        MAXTIMES("30", "超过尝试次数");

        public final String id;
        public final String title;

        private PdStatus(String id, String title) {
            this.id = id;
            this.title = title;
        }

        public String getId() {
            return this.id;
        }
    }
}
