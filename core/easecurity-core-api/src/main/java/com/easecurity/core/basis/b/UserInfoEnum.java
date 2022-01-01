package com.easecurity.core.basis.b;

import java.util.ArrayList;
import java.util.List;

/**
 * UserInfo类下所有枚举类
 */
public class UserInfoEnum {

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

        public static Status getEnumByCode(String code) {
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
