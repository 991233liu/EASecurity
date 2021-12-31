package com.easecurity.core.basis.b;

import java.util.ArrayList;
import java.util.List;

/**
 * User类下所有枚举类
 */
public class UserEnum {

    /**
     * 密码状态<br>
     */
    public enum AcStatus {
        ENABLED("ENABLED", "启用"),
        DISABLED("DISABLED", "禁用"),
        DEREGISTRATION("DEREGISTRATION", "注销"),
        DELETION("DEREGISTRATION", "删除");

        private String code;
        private String title;

        // 普通方法
        public static String getTitle(String code) {
            for (AcStatus ms : AcStatus.values()) {
                if (ms.getCode().equals(code)) {
                    return ms.title;
                }
            }
            return null;
        }

        public static String getCode(String title) {
            for (AcStatus ms : AcStatus.values()) {
                if (ms.title.equals(title)) {
                    return ms.code;
                }
            }
            return null;
        }

        public static AcStatus getAcStatus(String code) {
            for (AcStatus ms : AcStatus.values()) {
                if (ms.getCode().equals(code)) {
                    return ms;
                }
            }
            return null;
        }

        public static List<AcStatus> getAllEnum() {
            List<AcStatus> mss = new ArrayList<AcStatus>();
            for (AcStatus ms : AcStatus.values()) {
                mss.add(ms);
            }
            return mss;
        }

        private AcStatus() {
        }

        private AcStatus(String code, String title) {
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
     * 用户状态<br>
     */
    public enum PdStatus {
        ENABLED("ENABLED", "启用"),
        EXPIRED("EXPIRED", "过期"),
        MAXTIMES("MAXTIMES", "超过尝试次数");

        private String code;
        private String title;

        // 普通方法
        public static String getTitle(String code) {
            for (PdStatus ms : PdStatus.values()) {
                if (ms.getCode().equals(code)) {
                    return ms.title;
                }
            }
            return null;
        }

        public static String getCode(String title) {
            for (PdStatus ms : PdStatus.values()) {
                if (ms.title.equals(title)) {
                    return ms.code;
                }
            }
            return null;
        }

        public static PdStatus getEnumByCode(String code) {
            for (PdStatus ms : PdStatus.values()) {
                if (ms.getCode().equals(code)) {
                    return ms;
                }
            }
            return null;
        }

        public static List<PdStatus> getAllEnum() {
            List<PdStatus> mss = new ArrayList<PdStatus>();
            for (PdStatus ms : PdStatus.values()) {
                mss.add(ms);
            }
            return mss;
        }

        private PdStatus() {
        }

        private PdStatus(String code, String title) {
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
