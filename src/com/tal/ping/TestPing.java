package com.tal.ping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPing {

	/**
	 * 进行ping值结果、加速、响应时间的执行方法
	 * 
	 * @param str
	 *            需要输入域名
	 * @param address
	 *            需要输入国内ping出的地址，以区分所ping的结果是国内还是国外的
	 * @return ping的结果
	 */
	public static String PingIP(String str, String address, String ip) {
		Runtime runtime = Runtime.getRuntime(); // 获取当前程序的运行进对象
		Process process = null; // 声明处理类对象
		String line = null; // 返回行信息
		InputStream is = null; // 输入流
		InputStreamReader isr = null;// 字节流
		BufferedReader br = null;
		String domainname = str;
		String result1 = "Ping succeeds,Has accelerated,Not timed out";
		String result2 = "Ping succeeds,Has accelerated,Time out";
		String result4 = "Ping failed";
		String result5 = "Ping succeeds,Not accelerated,Not timed out";
		String result6 = "Ping succeeds,Not accelerated,Time out";
		try {
			process = runtime.exec("ping " + domainname); // PING
			is = process.getInputStream(); // 实例化输入流
			isr = new InputStreamReader(is);// 把输入流转换成字节流
			br = new BufferedReader(isr);// 从字节中读取文本
			String line_result = "";
			while ((line = br.readLine()) != null) {
				line_result = line_result + line + '\n';// 获取到ping出的字符串
			}
			is.close();
			isr.close();
			br.close();

			/**
			 * 第一步：Ping域名，检测字符中是否含有"平均"字样 如果有，则代表ping成功，ping成功后，下一步测试是否加速
			 * 如果没有，则代表ping失败
			 */

			if (line_result.contains("平均")) {

				// 从Ping出的字符串中提取“正在 Ping XXXXXXXXXX”
				String regular_text = "正在 Ping " + "([0-9a-z\\.]+)";
				String search_text = extract(regular_text, line_result);
				// 从“正在 Ping XXXXXXXX中提取出XXXXXXX”
				String regular_url = "([0-9a-z\\.]+)";
				String search_url = extract(regular_url, search_text);
				
				// 方法一：检测是否有“XXXXXXXXXX”是否和输入的地址ping后的地址(国内的)，如果一致，则代表未加速
				// 方法二：检测解析的IP是否是国外IP，国外IP则代表成功
				if (!search_url.equals(address)) {
					// 不一致，初步证明是国外，但是还需要判断其ip，以再次确认其ip,则代表海外加速
					// 对比已知国内ip与ping出ip是否相等，如果相等，成功加速
					// 提取ip
					String regular_ip = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
					String search_ip = extract(regular_ip, line_result);
					if (!search_ip.equals(ip)) {
						// 从字符串中截取最长时间
						
						String regular_time = "最长 = " + "\\d+" + "ms";
						String search_time = extract(regular_time, line_result);
						// 从最长时间中截取具体时间
						String regular_time_number = "\\d?";
						String search_time_number = extract(regular_time_number, search_time);
						int time_result = Integer.parseInt(search_time_number);
						// 检测是否超时.如果大于50ms即为超时
						if (time_result < 50) {
							// 返回Ping成功，已加速，未超时
							
							return result1;
						} else {
							// 返回ping成功，已加速，超时
							return result2;
						}
					} else {
						// 如果一致，则代表国内未加速
						// 从字符串中截取最长时间
						String regular_time = "最长 = " + "\\d+" + "ms";
						String search_time = extract(regular_time, line_result);
						// 从最长时间中截取具体时间
						String regular_time_number = "\\d?";
						String search_time_number = extract(regular_time_number, search_time);
						int time_result = Integer.parseInt(search_time_number);
						// 检测是否超时.如果大于50ms即为超时
						if (time_result < 50) {
							// 返回Ping成功，未加速，未超时
							return result5;
						} else {
							// 返回ping成功，未加速，超时
							return result6;
						}
					}
				} else {
					// 如果一致，则代表国内未加速
					// 从字符串中截取最长时间
					String regular_time = "最长 = " + "\\d+" + "ms";
					String search_time = extract(regular_time, line_result);
					// 从最长时间中截取具体时间
					String regular_time_number = "\\d?";
					String search_time_number = extract(regular_time_number, search_time);
					int time_result = Integer.parseInt(search_time_number);
					// 检测是否超时.如果大于50ms即为超时
					if (time_result < 50) {
						// 返回Ping成功，未加速，未超时
						return result5;
					} else {
						// 返回ping成功，未加速，超时
						return result6;
					}
				}
			} else {
				// 返回Ping失败
				return result4;
			}
		} catch (IOException e) {
			System.out.println(e);
			runtime.exit(1);
			return result4;
		}
	}

	/**
	 * 进行三次ping的检测，如果3次都成功，才成功
	 * 
	 * @param ip
	 * @param address
	 * @return
	 */
	public static String PingTest(String domain, String address, String ip) {
		String str1 = "Ping succeeds,Has accelerated,Not timed out";
		String str2 = "Ping succeeds,Not accelerated,Not timed out";
		String str3 = "Ping failed,Three test results are different";
		//String[] str_data = new String[3];
		String result = TestPing.PingIP(domain, address, ip);
		System.out.println(domain+ ":"+result);
		//ping 一次
		if (result.equals(str1)) {
			return result;
		} else if(result.equals(str2)) {
			return result;
		}else{
			return str3;
		}
		
		//Ping3次
		/*
		if (result.equals(str1)) {
			str_data[0] = result;
			for (int i = 1; i < 3; i++) {
				String result_again = TestPing.PingIP(domain, address, ip);
				str_data[i] = result_again;
			}
			if (str_data[0].equals(str_data[1]) && str_data[0].equals(str_data[2])) {
				return result;
			} else {
				return str3;
			}
		} else if (result.equals(str2)) {
			str_data[0] = result;
			for (int i = 1; i < 3; i++) {
				String result_again = TestPing.PingIP(domain, address, ip);
				str_data[i] = result_again;
			}
			if (str_data[0].equals(str_data[1]) && str_data[0].equals(str_data[2])) {
				return result;
			} else {
				return str3;
			}
		} else {
			return result;
		}
		*/
	}

	/**
	 * 提取字符串的方法
	 * 
	 * @param regular
	 *            需要输入的正则表达式
	 * @param text
	 *            需要被提取的字符串
	 * @return 提取后的结果
	 */
	private static String extract(String regular, String text) {
		String extract_result = null;
		Pattern ps = Pattern.compile(regular);
		Matcher ms = ps.matcher(text);
		while (ms.find()) {
			if (!"".equals(ms.group())) {
				extract_result = ms.group();
			}
		}
		return extract_result;
	}

	public static String lastResult(String[] domain, String[] address, String[] ip) {
		String result = null;
		String last_result = "";
		int num = domain.length;
		for (int i = 0; i < num; i++) {
			result = TestPing.PingTest(domain[i], address[i], ip[i]);
			String last = domain[i] + " : " + result;
			last_result = last_result + last + "\n";
		}
		return last_result;
	}
}
