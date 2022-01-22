/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.core.basis;

import java.io.Serializable;
import java.util.List;

import com.easecurity.core.basis.au.UriIp;
import com.easecurity.core.basis.au.UriOrg;
import com.easecurity.core.basis.re.Uri;

/**
 * domain：接口（服务）
 *
 */
public class UriDo implements Serializable {

    private static final long serialVersionUID = -7808950507994121828L;

    /**
     * 接口（服务）
     */
    public Uri uri;
    /**
     * 有接口（服务）权限的IPs
     */
    public UriIp uriIp;
    /**
     * 有接口（服务）权限的组织
     */
    public List<UriOrg> uriOrg;
    /**
     * 接口（服务）的权限规则组的数量
     */
    private int maxGroup = -1;

    private String _auOrgStr;

    /**
     * 从组织判断是否拥有此接口的权限。
     * 
     * @return true 有权限；false 无权限。
     */
    // TODO 需要优化，临时凑合着写的
    public boolean havePermissionByOrgId(String orgId, int group) {
	if (uriOrg != null && !uriOrg.isEmpty()) {
	    if (_auOrgStr == null) {
		_auOrgStr = ",";
		for (UriOrg uo : uriOrg) {
		    if ("0".equals(uo.status))
			_auOrgStr += uo.getGroup() + "." + uo.orgid + ",";
		}
	    }
	    return _auOrgStr.indexOf("," + group + "." + orgId + ",") > -1;
	} else
	    return false;
    }

    /**
     * 从组织判断是否拥有此接口的权限。
     * 
     * @return true 有权限；false 无权限。
     */
    public boolean havePermissionByIp(String clientIp, int group) {
	if (uriIp != null && uriIp.ips != null && !uriIp.ips.isEmpty() && (group == 0 || group == uriIp.group)) {
	    return uriIp.ips.indexOf(clientIp) > -1;
	} else
	    return false;
    }

    /**
     * 获取接口（服务）的权限规则组的数量
     */
    public int getMaxGroup() {
	if (maxGroup == -1) { // 初始化
//	    maxGroup=?
	}
	return maxGroup;
    }
}
