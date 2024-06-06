package com.easecurity.core.basis.s;

/**
 * certificates类下所有枚举类
 */
public class CertificatesEnum {

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
     * 类型<br>
     */
    public enum Type {
        RSA("RSA", "RSA"), //
        EC("EC", "EC"), //
        AES("AES", "AES");

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
}
