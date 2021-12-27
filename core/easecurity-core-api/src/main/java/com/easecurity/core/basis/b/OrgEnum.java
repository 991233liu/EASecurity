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
        INSTITUTIONS("INSTITUTIONS", "机构"),
        COMPANY("COMPANY", "公司"),
        OFFICE("OFFICE", "办事处"),
        DEPARTMENT("DEPARTMENT", "部门"),
        TEMP("TEMP", "临时");

        private String index;
        private String title;

        // 普通方法
        public static String getTitle(String index) {
            for (Type ms : Type.values()) {
                if (ms.getIndex().equals(index)) {
                    return ms.title;
                }
            }
            return null;
        }

        public static String getIndex(String title) {
            for (Type ms : Type.values()) {
                if (ms.title.equals(title)) {
                    return ms.index;
                }
            }
            return null;
        }

        public static Type getType(String index) {
            for (Type ms : Type.values()) {
                if (ms.getIndex().equals(index)) {
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

        private Type(String index, String title) {
            this.index = index;
            this.title = title;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
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

        private String index;
        private String title;

        // 普通方法
        public static String getTitle(String index) {
            for (Status ms : Status.values()) {
                if (ms.getIndex().equals(index)) {
                    return ms.title;
                }
            }
            return null;
        }

        public static String getIndex(String title) {
            for (Status ms : Status.values()) {
                if (ms.title.equals(title)) {
                    return ms.index;
                }
            }
            return null;
        }

        public static Status getStatus(String index) {
            for (Status ms : Status.values()) {
                if (ms.getIndex().equals(index)) {
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

        private Status(String index, String title) {
            this.index = index;
            this.title = title;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }
}
