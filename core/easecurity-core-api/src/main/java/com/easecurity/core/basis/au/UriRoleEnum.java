package com.easecurity.core.basis.au;

import java.util.ArrayList;
import java.util.List;

/**
 * UriOrg类下所有枚举类
 */
public class UriRoleEnum {
    /**
     * 来源<br>
     */
    public enum FromTo {
	MANUALLY("MANUALLY", "人工"), 
	SOURCECODE("SOURCECODE", "程序源代码");

	private String code;
	private String title;

	// 普通方法
	public static String getTitle(String code) {
	    for (FromTo ms : FromTo.values()) {
		if (ms.getCode().equals(code)) {
		    return ms.title;
		}
	    }
	    return null;
	}

	public static String getCode(String title) {
	    for (FromTo ms : FromTo.values()) {
		if (ms.title.equals(title)) {
		    return ms.code;
		}
	    }
	    return null;
	}

	public static FromTo getEnumByCode(String code) {
	    for (FromTo ms : FromTo.values()) {
		if (ms.getCode().equals(code)) {
		    return ms;
		}
	    }
	    return null;
	}

	public static List<FromTo> getAllEnum() {
	    List<FromTo> mss = new ArrayList<FromTo>();
	    for (FromTo ms : FromTo.values()) {
		mss.add(ms);
	    }
	    return mss;
	}

	private FromTo() {
	}

	private FromTo(String code, String title) {
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
