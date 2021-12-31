package com.easecurity.core.basis.b;

import java.util.ArrayList;
import java.util.List;

/**
 * Posts类下所有枚举类
 */
public class PostsEnum {

    /**
     * 职务类别<br>
     */
    public enum Type {
        LEADERSHIP("LEADERSHIP", "领导"),
        EMPLOYEE("DISABLED", "员工");

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

        public static Type getEnumByCode(String code) {
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
}
