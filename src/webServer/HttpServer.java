package webServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 服务器监听端口的线程类，在创建的同时开始执行run
 * 拥有一个线程池和一个serversocket属性
 * init方法初始化线程池大小，并绑定配置端口
 * @author xmubaga
 *
 */
public class HttpServer implements Runnable{
	final Logger logger = LoggerFactory.getLogger(RequestAnalyze.class);
    private ExecutorService pool;
    private ServerSocket serverSocket;
   /**
    * 初始化模块，绑定服务器监听的端口，指定线程池大小
    */
    private  void init(){
    	try {
			serverSocket = new ServerSocket(Config.PORT);
		} catch (IOException e) {
			logger.info("无法启动HTTP服务器:"+e.getLocalizedMessage());
		}
    	pool = Executors.newFixedThreadPool(64);
    }
    /**
     * 构造方法，先初始化，然后开始运行服务线程
     */
	public HttpServer(){
		init();
		new Thread(this).start();
		logger.info("HTTP服务器正在运行,端口:"+Config.PORT);
	}
	/**
	 * 该方法轮询端口是否有客服端连接进来，有链接进来则提交给线程池处理
	 */
	@Override
	public void run() {
		while(true){
			try {
				Socket client = serverSocket.accept();
				logger.info("有客服端连接进来了，它是："+client.toString());
				Handle handle = new Handle(client);
				pool.execute(handle);
				
			} catch (IOException e) {
				logger.info("客服端连接异常");
			}
		}
	}
	/**
	 * 结束服务线程
	 */
	protected void close(){
		//先检查线程池是否有任务，有任务则等任务完成之后停止
		Thread.currentThread().interrupt();
	}
}