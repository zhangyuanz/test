package webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * 该类用于启动或停止服务器
 * @author xmubaga
 *
 */
public class Controller {
	/**
	 * 控制器自启动main方法
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String command = null;
		HttpServer httpServer = null;
		try {
			while (true) {
				command = reader.readLine();
				if (command.equals("start")) {
					if (httpServer == null) {
						httpServer = new HttpServer();
					} else {
						System.out.println("服务器已经启动，请勿重复此操作！");
					}
				}
				if (command.equals("exit")) {
					if (httpServer != null) {
						// 释放资源
						httpServer.close();
					}
					// 等待已有任务结束，退出
					break;
				}
			}
			System.out.println("服务器停止了...");
		} catch (IOException e) {
			//thow new IOException("控制台输入异常");
			//日子记录异常
		}finally{
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
