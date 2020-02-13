package com.example.bysj2020.utils;

import com.example.bysj2020.global.Config;

import java.io.File;
import java.math.BigDecimal;

/**
 * Author : wxz
 * Time   : 2020/02/13
 * Desc   : 缓存工具
 */
public class CacheUtil {
    private static Double cacheSize = 0.0; //缓存大小

    /**
     * 读取缓存文件
     *
     * @param fileName
     */
    private static void ReadCache(String fileName) {
        File file = new File(fileName);
        File[] files = file.listFiles();
        if (files != null) {
            int len = files.length;
            if (len != 0) {
                for (int i = 0; i < len; i++) {
                    if (files[i].isDirectory()) {
                        //不是文件递归获取
                        ReadCache(files[i].getAbsolutePath());
                    } else {
                        cacheSize += files[i].length();
                    }
                }
            } else {
                cacheSize = cacheSize + 0.0;
            }
        }
    }

    /**
     * 得到缓存文件的大小
     *
     * @param fileNAme
     * @return
     */
    public static String getCacheSize(String fileNAme) {
        ReadCache(fileNAme);
        String s;
        if (cacheSize / 1024 > 1024) {
            cacheSize = cacheSize / (1024 * 1024);
            BigDecimal b = new BigDecimal(cacheSize);
            cacheSize = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            s = cacheSize + "M";
        } else {
            cacheSize = cacheSize / 1024;
            BigDecimal b = new BigDecimal(cacheSize);
            cacheSize = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            s = cacheSize + "K";
        }
        return s;
    }

    /**
     * 清除缓存
     *
     * @param fileName
     */
    public static void cleanCache(String fileName) {
        File file = new File(fileName);
        File[] files = file.listFiles();
        if (files != null) {
            int len = files.length;
            if (len != 0) {
                for (int i = 0; i < len; i++) {
                    if (files[i].isDirectory()) {
                        cleanCache(files[i].getAbsolutePath());
                    } else {
                        File delFile = new File(files[i].getAbsolutePath());
                        delFile.delete();
                    }
                }
            }
        }
    }

    /**
     * 创建用户的缓存文件夹
     *
     * @param id
     */
    public static void creatCacheFolder(String id) {
        File cacheFile = new File(Config.Companion.getCACHE() + File.separator + id);
        if (!cacheFile.exists()) {
            cacheFile.mkdirs();
        }
    }
}
