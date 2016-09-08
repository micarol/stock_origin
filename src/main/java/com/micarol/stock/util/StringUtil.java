package com.micarol.stock.util;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    public StringUtil() {
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * replace string
     *
     * @param text
     * @param sourceStr
     * @param targetStr
     * @return
     */
    public static String replaceAll(String text, String sourceStr, String targetStr) {
        StringBuffer sb = new StringBuffer(text);
        int l = sourceStr.length();
        int p = sb.indexOf(sourceStr);
        while (p > -1) {
            sb = sb.replace(p, p + l, targetStr);
            p = sb.indexOf(sourceStr);
        }
        return sb.toString();
    }

    /**
     * replace string of the first match by sourceStr
     *
     * @param text
     * @param sourceStr
     * @param targetStr
     * @return
     */
    public static String replaceFirst(String text, String sourceStr, String targetStr) {
        StringBuffer sb = new StringBuffer(text);
        int l = sourceStr.length();
        int p = sb.indexOf(sourceStr);
        if (p != -1) {
            sb = sb.replace(p, p + l, targetStr);
        }
        return sb.toString();
    }

    /**
     * split string by tokenizer
     *
     * @param subject    被分割的字符串
     * @param delimiters 分割符号
     * @return array
     */
    public static String[] splitUsingTokenizer(String subject, String delimiters) {
        return splitUsingTokenizerAsList(subject, delimiters).toArray(new String[0]);
    }

    /**
     * split string by tokenizer
     *
     * @param subject    被分割的字符串
     * @param delimiters 分割符号
     * @return list
     */
    public static List<String> splitUsingTokenizerAsList(String input, String delim) {
        List<String> l = new ArrayList<String>();
        String str;
        while (true) {
            int index = input.indexOf(delim);
            if (index == -1) {
                str = input;
                if (!"".equals(str)) {
                    l.add(str);
                }
                break;
            } else {
                str = input.substring(0, index);
                if (!"".equals(str)) {
                    l.add(str);
                }
                input = input.substring(index + delim.length());
            }
        }
        return l;
    }

    /**
     * 以「,」为分割符，切割一个字符串成一个String的数组。
     * 此方法会过滤掉空字符串和null值，
     * 只有在传入字符串切割后包含非空字符串的情况下，才返回相应的字符串数组。
     * 在以下情况将返回长度为0的字符串数组： split(",") 、split(" ") 、 split(null);
     * 此方法一定不会返回null值。
     *
     * @param str       将要被切割的字符串
     * @param delimiter 分割字符串
     * @return 切割后的字符串数组
     */
    static public String[] split(String str, String delimiter) {
        if (str == null) return new String[0];

        List<String> result = new ArrayList<String>();
        String[] tempRes = str.split(delimiter);

        for (int i = 0; i < tempRes.length; i++) {
            if (StringUtils.isNotBlank(tempRes[i]))
                result.add(tempRes[i]);
        }

        if (result.size() != 0) return result.toArray(new String[0]);
        else return new String[0];
    }

    /**
     * 以「,」为分割符，切割一个字符串成一个String的数组。 此方法会过滤掉空字符串、null值和非纯数字的值，
     * 只有在传入字符串切割后包含非空且只是纯数字字符串的情况下，才返回相应的字符串数组。 在以下情况将返回长度为0的字符串数组： split(",") 、split(" ") 、
     * split(null); 此方法一定不会返回null值。
     *
     * @param str       将要被切割的字符串
     * @param delimiter 分割字符串
     * @return 切割后的字符串数组
     */
    public static String[] splitAndFilterNonNumeric(String str, String delimiter) {
        if (str == null)
            return new String[0];

        List<String> result = new ArrayList<String>();
        String[] tempRes = str.split(delimiter);

        for (int i = 0; i < tempRes.length; i++) {
            if (StringUtils.isNotBlank(tempRes[i]) && StringUtils.isNumeric(tempRes[i]))
                result.add(tempRes[i]);
        }

        if (result.size() != 0)
            return result.toArray(new String[0]);
        else
            return new String[0];
    }

    public static void main(String[] args) {
        String subject = "1,2,hh,,13-5,,,4 5,4a1,4,5,,a";
        String[] aa = StringUtil.splitAndFilterNonNumeric(subject, ",");

        System.out.println("length=====" + aa.length);
        for (int i = 0; i < aa.length; i++) {
            System.out.println(aa[i]);
        }
    }

    public static String cut(String str) {
        str = str.replace(",", "");
        if (str.length() != 10) {
            str = "0123456789"; //如果获取到的userId字长不对，则统一赋值为0123456789
        }
        return str;
    }

    public static byte[] readFileImage(String filename) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(
                new FileInputStream(filename));
        int len = bufferedInputStream.available();
        byte[] bytes = new byte[len];
        int r = bufferedInputStream.read(bytes);
        if (len != r) {
            bytes = null;
            throw new IOException("读取文件不正确");
        }
        bufferedInputStream.close();
        return bytes;
    }

    /**
     * 判断两个字符串数组的值是否相等
     */
    public static boolean sameStringArray(String[] arr1, String[] arr2) {
        boolean flag = true;
        if (arr1.length == arr2.length) {
            for (int i = 0; i < arr1.length; i++) {
                if (!arr1[i].equals(arr2[i])) {
                    flag = false;
                    break;
                }
            }
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * 去除字符串中首尾的引号.ps:必须首尾同时有同样的引号
     *
     * @param text
     * @return
     * @author: micarol128@gmail.com
     * @since: 2013-5-7 下午2:40:48
     */
    public static String removeQuote(String text) {
        if (text.charAt(0) == '"') {
            int length = text.length() - 1;
            if (text.charAt(length) == '"') {
                return text.substring(1, length);
            }
        } else if (text.charAt(0) == '\'') {
            int length = text.length() - 1;
            if (text.charAt(text.length() - 1) == '\'') {
                return text.substring(1, length);
            }
        }
        return text;
    }

    public static List<String> splitString(String input, String delim) {
        List<String> l = new ArrayList<String>();
        String str;
        while (true) {
            int index = input.indexOf(delim);
            if (index == -1) {
                l.add(input);
                return l;
            } else {
                str = input.substring(0, index);
                if (!"".equals(str)) {
                    l.add(str);
                }
                input = input.substring(index + delim.length());
            }
        }
    }

    // 获取文本长度
    public static double getTextLength(String text) {
        double len = 0;
        for (int i = 0; i < text.length(); i++) {
            int temp = (int) text.charAt(i);
            if (temp > 0 && temp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return len;
    }
}
