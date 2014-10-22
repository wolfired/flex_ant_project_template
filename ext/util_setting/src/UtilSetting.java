import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.CharBuffer;


public class UtilSetting {
	private static final String LIB_SRT = "<libraryPathEntry kind=\"3\" linkType=\"1\" path=\"/TARGET_NAME/bin/TARGET_NAME.swc\" useDefaultLinkType=\"false\"/>";
	private static final String RSL_STR = "<libraryPathEntry kind=\"3\" linkType=\"2\" path=\"/TARGET_NAME/bin/TARGET_NAME.swc\" useDefaultLinkType=\"false\"/>";
	
	private String _path;
	private String _prefix;
	
	public UtilSetting(String path, String prefix) {
		_path = path;
		_prefix = prefix;
	}
	
	public void done() throws Exception{
		CharBuffer buffer = CharBuffer.allocate(4096);
		
		String content;
		String[] array;
		
		content = this.readContent(_path + "/.dependlib");
		array = content.split(",");
		for (String str : array) {
			if(null != str && !"".equals(str.trim())){
				buffer.put(LIB_SRT.replaceAll("TARGET_NAME", _prefix + str.trim()));
			}
		}
		
		content = this.readContent(_path + "/.dependrsl");
		array = content.split(",");
		for (String str : array) {
			if(null != str && !"".equals(str.trim())){
				buffer.put(RSL_STR.replaceAll("TARGET_NAME", _prefix + str.trim()));
			}	
		}
		
		String src_content = this.readContent(_path + "/.actionScriptProperties");
		String dst_content = src_content.replaceAll("@@lib_rsl@@", buffer.flip().toString());
		this.writeContent(_path + "/.actionScriptProperties", dst_content);
		
		buffer.clear();
	}
	

	private String readContent(String fileName) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		CharBuffer cb = CharBuffer.allocate(4096);
		br.read(cb);
		br.close();
		
		String result = cb.flip().toString();
		cb.clear();
		return result;
	}
	
	private void writeContent(String fileName, String content) throws Exception{
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
		bw.write(content);
		bw.close();
	}
	
	public static void main(String[] args) throws Exception {
		new UtilSetting(args[0], args[1]).done();
	}
}
