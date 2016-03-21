package webServer;

import java.io.File;
import java.net.Socket;

/**
 * 该类是完成逻辑控制，业务流程处理，实现runable接口，专门执行逻辑任务
 * 
 * @author xmubaga
 *
 */
public class Handle implements Runnable {
	private Socket clientSocket;

	/**
	 * 构造方法，初始化客服端socket对象
	 * 
	 * @param clientSocket
	 */
	public Handle(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		handleRequest();
	}

	private void handleRequest() {
		System.out.println("开始封装客服端请求了...");
		RequestAnalyze request = new RequestAnalyze(clientSocket);
		Response response = new Response(clientSocket);
		// 非GET请求
		if (request.getMethod() != null && !(request.getMethod().equals("GET"))) {
			System.out.println("非get方法");
			response.outNotGet();
			// thow 异常？
			return;
		}

		String requesturl = request.getRequestURL();
		System.out.println("封装的url：" + requesturl);
		if (requesturl == null) {
			// 输入空路径，默认显示d盘目录
			response.outFileList(new File(Config.ROOT + ":/"));
			return;
		}
		if (requesturl.equals("/favicon.ico")) {
			// 预留处理favicon.ico
			return;
		}
		// 非D盘
		String disk = requesturl.substring(1, 2);
		System.out.println("disk：" + disk);
		if (!disk.equals(Config.ROOT)) {
			System.out.println("访问非D盘");
			response.outNoPower();
			return;
		} else {
			String path = toPath(requesturl);
			System.out.println("访问路劲:" + path);
			File file = new File(path);
			// 只是一个目录
			if (file.isDirectory()) {
				System.out.println("访问的资源是一个目录");
				response.outFileList(file);

			} else {
				// 文件不存在
				if (!file.exists()) {
					System.out.println("访问的文件不存在");
					response.outNotExist();
				} else {
					// 非静态文件
					if (!contain(Config.STATIC_FILES, file.getName())) {
						System.out.println("访问的是非静态文件");
						response.outIllegalType();
					} else {
						System.out.println("访问的是静态文件");
						response.outFile(file);
					}
				}
			}
		}
	}

	/**
	 * 将一个URL转换为路径，并允许用户输入如果是空，即只输入主机ip，则访问默认跟目录，如果是根目录没有添加“/”自动添加
	 * 
	 * <pre>
	 * localhost =  "D:/"
	 * localhost/d = "D:/"此时应该通知浏览器重定向/d/
	 * localhost/d/ = "D:/"
	 * </pre>
	 * 
	 * @param url是http请求经过封装的url
	 */
	private String toPath(String url) {
		if (url.isEmpty() || url.equals("/")) {
			return Config.ROOT + ":/";
		}
		return Config.ROOT + ":/" + url.substring(3, url.length());
	}

	/**
	 * 判断文件是否为静态文件
	 * 
	 * @param str
	 *            是文件名
	 * @return
	 */
	private boolean contain(String[] fileLastNames, String str) {
		String lastName = str.substring(str.indexOf(".")+1, str.length());
		System.out.println("文件后缀名为："+lastName);
		for (String temp : fileLastNames) {
			if (temp.equals(lastName)){
				System.out.println("是静态文件");
				return true;
			}		
		}
		System.out.println("不是静态文件");
		return false;
	}
}