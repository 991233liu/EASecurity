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

    /**
     * 显示状态<br>
     */
    public enum DisplayStatus {
        DISPLAY("DISPLAY", "始终隐藏"),
        HIDDEN("HIDDEN", "始终显示"),
        DISABLEDHIDDEN("DISABLEDHIDDEN", "禁用隐藏"),
        NOPERMISSIONSHIDDEN("NOPERMISSIONSHIDDEN", "无权限隐藏");

        private String code;
        private String title;

        // 普通方法
        public static String getTitle(String code) {
            for (DisplayStatus ms : DisplayStatus.values()) {
                if (ms.getCode().equals(code)) {
                    return ms.title;
                }
            }
            return null;
        }

        public static String getCode(String title) {
            for (DisplayStatus ms : DisplayStatus.values()) {
                if (ms.title.equals(title)) {
                    return ms.code;
                }
            }
            return null;
        }

        public static DisplayStatus getDisplayStatus(String code) {
            for (DisplayStatus ms : DisplayStatus.values()) {
                if (ms.getCode().equals(code)) {
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

        private DisplayStatus(String code, String title) {
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
     * 类型<br>
     */
    public enum AccessType {
        ANONYMOUS("ANONYMOUS", "匿名访问"),
        LOGIN("LOGIN", "登录用户访问"),
        AUTHORIZATION("AUTHORIZATION", "授权访问");

        private String code;
        private String title;

        // 普通方法
        public static String getTitle(String code) {
            for (AccessType ms : AccessType.values()) {
                if (ms.getCode().equals(code)) {
                    return ms.title;
                }
            }
            return null;
        }

        public static String getCode(String title) {
            for (AccessType ms : AccessType.values()) {
                if (ms.title.equals(title)) {
                    return ms.code;
                }
            }
            return null;
        }

        public static AccessType getAccessType(String code) {
            for (AccessType ms : AccessType.values()) {
                if (ms.getCode().equals(code)) {
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

        private AccessType(String code, String title) {
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
     * 菜单级别<br>
     */
    public enum Level {
        ROOT("ROOT", "根"),
        FIRST("FIRST", "一级"),
        SECOND("SECOND", "二级"),
        THIRD("THIRD", "三级"),
        FOURTH("FOURTH", "四级"),
        FIFTH("FIFTH", "五级");

        private String code;
        private String title;

        // 普通方法
        public static String getTitle(String code) {
            for (Level ms : Level.values()) {
                if (ms.getCode().equals(code)) {
                    return ms.title;
                }
            }
            return null;
        }

        public static String getCode(String title) {
            for (Level ms : Level.values()) {
                if (ms.title.equals(title)) {
                    return ms.code;
                }
            }
            return null;
        }

        public static Level getLevel(String code) {
            for (Level ms : Level.values()) {
                if (ms.getCode().equals(code)) {
                    return ms;
                }
            }
            return null;
        }

        public static List<Level> getAllEnum() {
            List<Level> mss = new ArrayList<Level>();
            for (Level ms : Level.values()) {
                mss.add(ms);
            }
            return mss;
        }

        private Level() {
        }

        private Level(String code, String title) {
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
