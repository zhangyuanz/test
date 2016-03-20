package webServer;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
/**
 * 该类是完成逻辑控制，业务流程处理，实现runable接口，专门执行逻辑任务
 * @author xmubaga
 *
 */
public class Handle implements Runnable {
	private Socket clientSocket;
    /**
     * 构造方法，初始化客服端socket对象
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
		System.out.println("开始处理客服端请求了...");
		RequestAnalyze request = new RequestAnalyze(clientSocket);
		Response response = new Response(clientSocket);
		// 非GET方法
		if (!request.getMethod().equals("GET")) {
			System.out.println("非get方法");
			response.outNotGet();
			// thow 异常？
			return;
		}
		
		String requesturl = request.getRequestURL();
		System.out.println("封装的url："+requesturl);
		// 非D盘
		String disk = requesturl.substring(1, 2);
		System.out.println("disk："+disk);
		if (!disk.equals(Config.ROOT)) {
			response.outNoPower();
			return;
		}else{
			String path = toPath(requesturl);
			System.out.println("访问路劲:"+path);
			File file = new File(path);
			// 只是一个目录
			if (file.isDirectory()) {
				response.outFileList(file);
				
			} else {
				// 文件不存在
				if (!file.exists()) {
					response.outNotExist();
				} else {
					// 非静态文件
					if (!isStaticFile(file.getName())) {
						response.outIllegalType();
					} else {
						//if(预览参数为ture) 输入预览信息outPreview
						//否则输入下载信息outFile
						try {
							response.outFile(file);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	/**
	 * 将一个URL转换为路径，并允许用户输入如果是空，即只输入主机ip，则访问默认跟目录，如果是根目录没有添加“/”自动添加
	 * <pre>
	 * localhost =  "D:/"
	 * localhost/d = "D:/"
	 * localhost/d/ = "D:/"
	 * </pre>
	 * 
	 * @param url是http请求经过封装的url
	 */
	private String toPath(String url){
		String gen = Config.ROOT+":/";
		String path = null;
		if(url == null){
			path = gen ;
		}
		if(url.startsWith('/'+Config.ROOT)){
			path = gen + url.substring(2, url.length());
		}
		if(url.startsWith('/'+Config.ROOT+'/')){
			path = gen + url.substring(3, url.length());
		}
		return path;
		
	}
	/**
	 * 判断文件是否为静态文件
	 * 
	 * @param str 是文件名
	 * @return 
	 */
	private boolean isStaticFile(String str) {
		if (str.endsWith(".txt") || str.endsWith(".docx")
				|| str.endsWith(".zip") || str.endsWith(".html")
				|| str.endsWith(".xls") || str.endsWith(".pdf")
				|| str.endsWith(".jpg") || str.endsWith(".png")
				|| str.endsWith(".rar")) {
			return true;
		} else {
			return false;
		}
	}
}