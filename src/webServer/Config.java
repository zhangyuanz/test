package webServer;
/**
 * 服务器的配置文件，包括服务端口及服务目录
 * @author xmubaga
 *
 */
public class Config {
	public static final int PORT = 80;
	public static final String ROOT = "d";
	public static final String[] STATIC_FILES = 
		{"jpg","png","txt","doc","docx","xls","pdf","ini","xml","html","zip","rar",
		"JPG","PNG","TXT","DOC","DOCX","XLS","PDF","INI","XML","HTML","ZIP","RAR"};
	/**
	 * 测试通过的文件类型：txt,xsl,ini,xml,zip
	 * isuess：存在乱码问题
	 */
	
}
