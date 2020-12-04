package com.micarol.stock.web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.micarol.stock.util.Loggers;

@Controller
public class WeiboController {

	private final static String appSecret = "0478b1f6f3f6ac789e1c9aee0e817b6a";

	@RequestMapping(value = "/fans/service/r/{key}", method = RequestMethod.GET)
	// @ResponseBody
	public void validate(@PathVariable("key") String key, String signature, String timestamp, String nonce,
			String echostr, HttpServletResponse response) throws Exception {
		Loggers.RUNNING_LOG.info("GET, /fans/service/r/{}", key);
		boolean isValid = ValidateSHA(signature, nonce, timestamp, appSecret);
		if (isValid) {// FSK_mbUid
			Loggers.RUNNING_LOG.info("echstr:{}", echostr);
			response.getWriter().append(echostr);
		}
		response.getWriter().append("");
	}

	public static boolean ValidateSHA(String signature, String nonce, String timestamp, String appSecret) {
		if (signature == null || nonce == null || timestamp == null) {
			return false;
		}
		String sign = sha1(getSignContent(nonce, timestamp, appSecret));
		if (!signature.equals(sign)) {
			return false;
		}
		return true;
	}

	private static String getSignContent(String... params) {
		List<String> list = new ArrayList(params.length);
		for (String temp : params) {
			if (StringUtils.isNotBlank(temp)) {
				list.add(temp);
			}
		}
		Collections.sort(list);
		StringBuilder strBuilder = new StringBuilder();
		for (String element : list) {
			strBuilder.append(element);
		}
		return strBuilder.toString();
	}

	private static String sha1(String strSrc) {
		MessageDigest md = null;
		String strDes = null;

		byte[] bt = strSrc.getBytes();
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return strDes;
	}

	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;

		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));

			if (tmp.length() == 1) {
				des += "0";
			}

			des += tmp;
		}

		return des;
	}
}
