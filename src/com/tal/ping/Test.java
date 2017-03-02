package com.tal.ping;

/**
 * 可以执行的脚本
 * 
 * @author hwl 方法：利用数组存放ip和address的值，返回值为字符串，不是JSON
 *
 */

public class Test {

	public static void main(String[] args) {

		String[] domain = { "im.weclassroom.com", "i.weclassroom.com","org.weclassroom.com","admin.weclassroom.com","client.weclassroom.com","chat.weclassroom.com","www.weclassroom.com","icourse.weclassroom.com","s.weclassroom.com"};
		String[] address = { "im.weclassroom.com", "i.weclassroom.com.w.kunlunar.com","k8125qu3lnh88x89.alicloudlayer.com","k8125qu3lnh88x89.alicloudlayer.com","k8125qu3lnh88x89.alicloudlayer.com","k8125qu3lnh88x89.alicloudlayer.com","k8125qu3lnh88x89.alicloudlayer.com","icourse.weclassroom.com.w.kunlunpi.com","s.weclassroom.com"};
		String[] ip = {"101.201.33.23","121.29.8.237","218.11.0.214","218.11.0.214","218.11.0.214","218.11.0.214","218.11.0.214","125.39.199.14","182.92.25.242"};
		System.out.println(TestPing.lastResult(domain, address,ip));

	}

}
