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
}
