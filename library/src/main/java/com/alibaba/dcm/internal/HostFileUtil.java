/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2018 All Rights Reserved.
 */
package com.alibaba.dcm.internal;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author tinghe
 * @version $Id: HostFileUtil.java, v 0.1 2018年01月15日 下午6:18 tinghe Exp $
 */
public class HostFileUtil {
    static Pattern SPACE_SEPARATOR = Pattern.compile("\\s+");

    public static Map<String, List<String>> loadHostFile(String hostFile) {
        File file = new File(hostFile);
        if (!file.isFile()) {
            LogUtil.log(file.getAbsolutePath() + " is not file");
            return null;
        }
        FileInputStream fis = null;
        Scanner scanner = null;

        try {
            Map<String, List<String>> ipMap = new HashMap<String, List<String>>();
            fis = new FileInputStream(file);
            scanner = new Scanner(fis);
            while (scanner.hasNextLine()) {
                String line = StringUtils.trim(scanner.nextLine());
                if (StringUtils.isBlank(line)) {
                    continue;
                }
                if (StringUtils.startsWith(line, "#")) {
                    continue;
                }
                String[] parts = SPACE_SEPARATOR.split(line);
                if (parts.length < 2) {
                    continue;
                }
                String ip = StringUtils.trim(parts[0]);
                for (int i = 1; i < parts.length; i++) {
                    String domain = StringUtils.trim(parts[i]);
                    addIp(ipMap, domain, ip);
                }
            }
            return ipMap;
        } catch (Exception e) {
            LogUtil.log("parse host file error.");
        } finally {
            closeQuietly(fis);
            closeQuietly(scanner);
        }
        return null;
    }

    public static void addIp(Map<String, List<String>> ipMap, String domain, String ip) {
        if (StringUtils.isBlank(domain) || StringUtils.isBlank(ip)) {
            return;
        }
        List<String> ips = ipMap.get(domain);
        if (ips == null) {
            ips = new ArrayList<String>();
            ipMap.put(domain, ips);
        }
        ips.add(ip);
    }

    public static void closeQuietly(Scanner scanner) {
        if (scanner != null) {
            scanner.close();
        }
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
}