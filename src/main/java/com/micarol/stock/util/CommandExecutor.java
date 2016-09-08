package com.micarol.stock.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统Shell调用工具类
 * 
 * @author cjou
 * @since 2013-03-18
 * 
 */
public class CommandExecutor {
	
	protected final static Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
	
	public static String execute(String command) {
		StringBuilder sb = new StringBuilder();
		String[] commands = new String[] { "/bin/sh", "-c", command };
		try {
			Process proc = new ProcessBuilder(commands).start();
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					proc.getErrorStream()));

			String s = null;
			while ((s = stdInput.readLine()) != null) {
				sb.append(s);
				sb.append("\n");
			}

			while ((s = stdError.readLine()) != null) {
				sb.append(s);
				sb.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Execute Command: command={}, result={}", command, sb.toString());
		return sb.toString();
	}
	
	public static void main(String[] avgs) {
		File dir = new File("/Users/micarol/logs/keyword_log_1");
		if(dir.isDirectory()){
			File[] files = dir.listFiles();
			for(int i=0;i<files.length;i++){
				File file = files[i];
				System.out.println(file.getPath());
				String  result = CommandExecutor.execute("rm "+file.getPath());
			}
		}	
	}

}
