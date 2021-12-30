package com.easecurity.core.basis.b;

import java.util.ArrayList;
import java.util.List;

/**
 * Org类下所有枚举类
 */
public class OrgEnum {

    /**
     * 类型<br>
     */
    public enum Type {
        ROOT("ROOT", "根"),
        COMPANY("COMPANY", "公司"),
        DEPARTMENT("DEPARTMENT", "部门"),
        INSTITUTIONS("INSTITUTIONS", "机构"),
        OFFICE("OFFICE", "办事处"),
        TEMP("TEMP", "临时");

        private String code;
        private String title;

        // 普通方法
        public static String getTitle(String code) {
            for (Type ms : Type.values()) {
                if (ms.getCode().equals(code)) {
                    return ms.title;
                }
            }
            return null;
        }

        public static String getCode(String title) {
            for (Type ms : Type.values()) {
                if (ms.title.equals(title)) {
                    return ms.code;
                }
            }
            return null;
        }

        public static Type getType(String code) {
            for (Type ms : Type.values()) {
                if (ms.getCode().equals(code)) {
                    return ms;
                }
            }
            return null;
        }

        public static List<Type> getAllEnum() {
            List<Type> mss = new ArrayList<Type>();
            for (Type ms : Type.values()) {
                mss.add(ms);
            }
            return mss;
        }

        private Type() {
        }

        private Type(String code, String title) {
            this.code = code;
            this.title = title;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }

    /**
     * 状态<br>
     */
    public enum Status {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "禁用"),
        DEREGISTRATION("DEREGISTRATION", "注销"),
        DELETION("DEREGISTRATION", "删除");

        private String code;
        private String title;

        // 普通方法
        public static String getTitle(String code) {
            for (Status ms : Status.values()) {
                if (ms.getCode().equals(code)) {
                    return ms.title;
                }
            }
            return null;
        }

        public static String getCode(String title) {
            for (Status ms : Status.values()) {
                if (ms.title.equals(title)) {
                    return ms.code;
                }
            }
            return null;
        }

        public static Status getStatus(String code) {
            for (Status ms : Status.values()) {
                if (ms.getCode().equals(code)) {
                    return ms;
                }
            }
            return null;
        }

        public static List<Status> getAllEnum() {
            List<Status> mss = new ArrayList<Status>();
            for (Status ms : Status.values()) {
                mss.add(ms);
            }
            return mss;
        }

        private Status() {
        }

        private Status(String code, String title) {
            this.code = code;
            this.title = title;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }
}
