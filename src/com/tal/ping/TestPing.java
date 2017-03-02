package com.tal.ping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPing {

	/**
	 * ����pingֵ��������١���Ӧʱ���ִ�з���
	 * 
	 * @param str
	 *            ��Ҫ��������
	 * @param address
	 *            ��Ҫ�������ping���ĵ�ַ����������ping�Ľ���ǹ��ڻ��ǹ����
	 * @return ping�Ľ��
	 */
	public static String PingIP(String str, String address, String ip) {
		Runtime runtime = Runtime.getRuntime(); // ��ȡ��ǰ��������н�����
		Process process = null; // �������������
		String line = null; // ��������Ϣ
		InputStream is = null; // ������
		InputStreamReader isr = null;// �ֽ���
		BufferedReader br = null;
		String domainname = str;
		String result1 = "Ping succeeds,Has accelerated,Not timed out";
		String result2 = "Ping succeeds,Has accelerated,Time out";
		String result4 = "Ping failed";
		String result5 = "Ping succeeds,Not accelerated,Not timed out";
		String result6 = "Ping succeeds,Not accelerated,Time out";
		try {
			process = runtime.exec("ping " + domainname); // PING
			is = process.getInputStream(); // ʵ����������
			isr = new InputStreamReader(is);// ��������ת�����ֽ���
			br = new BufferedReader(isr);// ���ֽ��ж�ȡ�ı�
			String line_result = "";
			while ((line = br.readLine()) != null) {
				line_result = line_result + line + '\n';// ��ȡ��ping�����ַ���
			}
			is.close();
			isr.close();
			br.close();

			/**
			 * ��һ����Ping����������ַ����Ƿ���"ƽ��"���� ����У������ping�ɹ���ping�ɹ�����һ�������Ƿ����
			 * ���û�У������pingʧ��
			 */

			if (line_result.contains("ƽ��")) {

				// ��Ping�����ַ�������ȡ������ Ping XXXXXXXXXX��
				String regular_text = "���� Ping " + "([0-9a-z\\.]+)";
				String search_text = extract(regular_text, line_result);
				// �ӡ����� Ping XXXXXXXX����ȡ��XXXXXXX��
				String regular_url = "([0-9a-z\\.]+)";
				String search_url = extract(regular_url, search_text);
				
				// ����һ������Ƿ��С�XXXXXXXXXX���Ƿ������ĵ�ַping��ĵ�ַ(���ڵ�)�����һ�£������δ����
				// ����������������IP�Ƿ��ǹ���IP������IP�����ɹ�
				if (!search_url.equals(address)) {
					// ��һ�£�����֤���ǹ��⣬���ǻ���Ҫ�ж���ip�����ٴ�ȷ����ip,����������
					// �Ա���֪����ip��ping��ip�Ƿ���ȣ������ȣ��ɹ�����
					// ��ȡip
					String regular_ip = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
					String search_ip = extract(regular_ip, line_result);
					if (!search_ip.equals(ip)) {
						// ���ַ����н�ȡ�ʱ��
						
						String regular_time = "� = " + "\\d+" + "ms";
						String search_time = extract(regular_time, line_result);
						// ���ʱ���н�ȡ����ʱ��
						String regular_time_number = "\\d?";
						String search_time_number = extract(regular_time_number, search_time);
						int time_result = Integer.parseInt(search_time_number);
						// ����Ƿ�ʱ.�������50ms��Ϊ��ʱ
						if (time_result < 50) {
							// ����Ping�ɹ����Ѽ��٣�δ��ʱ
							
							return result1;
						} else {
							// ����ping�ɹ����Ѽ��٣���ʱ
							return result2;
						}
					} else {
						// ���һ�£���������δ����
						// ���ַ����н�ȡ�ʱ��
						String regular_time = "� = " + "\\d+" + "ms";
						String search_time = extract(regular_time, line_result);
						// ���ʱ���н�ȡ����ʱ��
						String regular_time_number = "\\d?";
						String search_time_number = extract(regular_time_number, search_time);
						int time_result = Integer.parseInt(search_time_number);
						// ����Ƿ�ʱ.�������50ms��Ϊ��ʱ
						if (time_result < 50) {
							// ����Ping�ɹ���δ���٣�δ��ʱ
							return result5;
						} else {
							// ����ping�ɹ���δ���٣���ʱ
							return result6;
						}
					}
				} else {
					// ���һ�£���������δ����
					// ���ַ����н�ȡ�ʱ��
					String regular_time = "� = " + "\\d+" + "ms";
					String search_time = extract(regular_time, line_result);
					// ���ʱ���н�ȡ����ʱ��
					String regular_time_number = "\\d?";
					String search_time_number = extract(regular_time_number, search_time);
					int time_result = Integer.parseInt(search_time_number);
					// ����Ƿ�ʱ.�������50ms��Ϊ��ʱ
					if (time_result < 50) {
						// ����Ping�ɹ���δ���٣�δ��ʱ
						return result5;
					} else {
						// ����ping�ɹ���δ���٣���ʱ
						return result6;
					}
				}
			} else {
				// ����Pingʧ��
				return result4;
			}
		} catch (IOException e) {
			System.out.println(e);
			runtime.exit(1);
			return result4;
		}
	}

	/**
	 * ��������ping�ļ�⣬���3�ζ��ɹ����ųɹ�
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
		//ping һ��
		if (result.equals(str1)) {
			return result;
		} else if(result.equals(str2)) {
			return result;
		}else{
			return str3;
		}
		
		//Ping3��
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
	 * ��ȡ�ַ����ķ���
	 * 
	 * @param regular
	 *            ��Ҫ�����������ʽ
	 * @param text
	 *            ��Ҫ����ȡ���ַ���
	 * @return ��ȡ��Ľ��
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
