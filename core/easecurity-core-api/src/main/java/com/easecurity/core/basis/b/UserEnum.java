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

        private String index;
        private String title;

        // 普通方法
        public static String getTitle(String index) {
            for (AcStatus ms : AcStatus.values()) {
                if (ms.getIndex().equals(index)) {
                    return ms.title;
                }
            }
            return null;
        }

        public static String getIndex(String title) {
            for (AcStatus ms : AcStatus.values()) {
                if (ms.title.equals(title)) {
                    return ms.index;
                }
            }
            return null;
        }

        public static AcStatus getAcStatus(String index) {
            for (AcStatus ms : AcStatus.values()) {
                if (ms.getIndex().equals(index)) {
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

        private AcStatus(String index, String title) {
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
     * 用户状态<br>
     */
    public enum PdStatus {
        ENABLED("ENABLED", "启用"),
        EXPIRED("EXPIRED", "过期"),
        MAXTIMES("MAXTIMES", "超过尝试次数");

        private String index;
        private String title;

        // 普通方法
        public static String getTitle(String index) {
            for (PdStatus ms : PdStatus.values()) {
                if (ms.getIndex().equals(index)) {
                    return ms.title;
                }
            }
            return null;
        }

        public static String getIndex(String title) {
            for (PdStatus ms : PdStatus.values()) {
                if (ms.title.equals(title)) {
                    return ms.index;
                }
            }
            return null;
        }

        public static PdStatus getPdStatus(String index) {
            for (PdStatus ms : PdStatus.values()) {
                if (ms.getIndex().equals(index)) {
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

        private PdStatus(String index, String title) {
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
