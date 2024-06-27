package com.easecurity.core.basis.b;

import com.easecurity.db.BaseEnum;

/**
 * Posts类下所有枚举类
 */
public class PostsEnum {

    /**
     * 职务类别<br>
     */
    public enum Type implements BaseEnum {
        LEADERSHIP("10", "领导"), //
        EMPLOYEE("20", "员工");

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
