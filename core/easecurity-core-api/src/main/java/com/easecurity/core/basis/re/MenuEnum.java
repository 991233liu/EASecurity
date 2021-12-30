package com.easecurity.core.basis.re;

import java.util.ArrayList;
import java.util.List;

/**
 * Menu类下所有枚举类
 */
public class MenuEnum {

    /**
     * 状态<br>
     */
    public enum Status {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "禁用");

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

    /**
     * 显示状态<br>
     */
    public enum DisplayStatus {
        DISPLAY("DISPLAY", "始终隐藏"),
        HIDDEN("HIDDEN", "始终显示"),
        DISABLEDHIDDEN("DISABLEDHIDDEN", "禁用隐藏"),
        NOPERMISSIONSHIDDEN("NOPERMISSIONSHIDDEN", "无权限隐藏");

        private String index;
        private String title;

        // 普通方法
        public static String getTitle(String index) {
            for (DisplayStatus ms : DisplayStatus.values()) {
                if (ms.getIndex().equals(index)) {
                    return ms.title;
                }
            }
            return null;
        }

        public static String getIndex(String title) {
            for (DisplayStatus ms : DisplayStatus.values()) {
                if (ms.title.equals(title)) {
                    return ms.index;
                }
            }
            return null;
        }

        public static DisplayStatus getDisplayStatus(String index) {
            for (DisplayStatus ms : DisplayStatus.values()) {
                if (ms.getIndex().equals(index)) {
                    return ms;
                }
            }
            return null;
        }

        public static List<DisplayStatus> getAllEnum() {
            List<DisplayStatus> mss = new ArrayList<DisplayStatus>();
            for (DisplayStatus ms : DisplayStatus.values()) {
                mss.add(ms);
            }
            return mss;
        }

        private DisplayStatus() {
        }

        private DisplayStatus(String index, String title) {
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
     * 类型<br>
     */
    public enum AccessType {
        ANONYMOUS("ANONYMOUS", "匿名访问"),
        LOGIN("LOGIN", "登录用户访问"),
        AUTHORIZATION("AUTHORIZATION", "授权访问");

        private String index;
        private String title;

        // 普通方法
        public static String getTitle(String index) {
            for (AccessType ms : AccessType.values()) {
                if (ms.getIndex().equals(index)) {
                    return ms.title;
                }
            }
            return null;
        }

        public static String getIndex(String title) {
            for (AccessType ms : AccessType.values()) {
                if (ms.title.equals(title)) {
                    return ms.index;
                }
            }
            return null;
        }

        public static AccessType getAccessType(String index) {
            for (AccessType ms : AccessType.values()) {
                if (ms.getIndex().equals(index)) {
                    return ms;
                }
            }
            return null;
        }

        public static List<AccessType> getAllEnum() {
            List<AccessType> mss = new ArrayList<AccessType>();
            for (AccessType ms : AccessType.values()) {
                mss.add(ms);
            }
            return mss;
        }

        private AccessType() {
        }

        private AccessType(String index, String title) {
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
