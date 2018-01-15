/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.alibaba.dcm.agent;

import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Map;

import com.alibaba.dcm.DnsCacheManipulator;
import com.alibaba.dcm.internal.HostFileUtil;
import com.alibaba.dcm.internal.LogUtil;
import com.alibaba.dcm.internal.StringUtils;

/**
 *
 * @author tinghe
 * @version $Id: DcmPreAgent.java, v 0.1 2018年01月15日 下午5:32 tinghe Exp $
 */
public class DcmPreAgent {

    public static void premain(String args, Instrumentation instrumentation) {
        if (StringUtils.isBlank(args)) {
            LogUtil.log("no arguments");
            return;
        }
        String file = null;
        String[] params = args.split(",");
        for (String param : params) {
            String[] kv = param.split("=");
            if (kv.length == 2) {
                if ("file".equals(kv[0])) {
                    file = kv[1];
                }
            }
        }
        if (StringUtils.isBlank(file)) {
            LogUtil.log("no argument [file]");
            return;
        }

        Map<String, List<String>> ipsMap = HostFileUtil.loadHostFile(file);
        setDns(ipsMap);
    }

    public static void setDns(Map<String, List<String>> ipsMap) {
        if (null == ipsMap) {
            return;
        }
        for (Map.Entry<String, List<String>> entry : ipsMap.entrySet()) {
            String domain = entry.getKey();
            List<String> ips = entry.getValue();
            if (StringUtils.isNotBlank(domain) && null != ips && ips.size() > 0) {
                String[] arr = ips.toArray(new String[ips.size()]);
                LogUtil.log("set " + domain + " " + StringUtils.join(ips, ','));
                DnsCacheManipulator.setDnsCache(domain, arr);
            }
        }
    }
}
