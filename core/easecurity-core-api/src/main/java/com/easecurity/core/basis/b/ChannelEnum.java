package com.easecurity.core.basis.b;

import com.easecurity.db.BaseEnum;

/**
 * Channel类下所有枚举类
 */
public class ChannelEnum {

    /**
     * 状态<br>
     */
    public enum Status implements BaseEnum {
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
