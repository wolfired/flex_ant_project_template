import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.CharBuffer;


public class UtilSetting {
	private static final String LIB_SRT = "<libraryPathEntry kind=\"3\" linkType=\"1\" path=\"/@@target_name@@/bin/@@target_name@@.swc\" useDefaultLinkType=\"false\"/>";
	private static final String RSL_STR = "<libraryPathEntry kind=\"3\" linkType=\"2\" path=\"/@@target_name@@/bin/@@target_name@@.swc\" useDefaultLinkType=\"false\"/>";
	
	public String readContent(String fileName) throws Exception{
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		CharBuffer cb = CharBuffer.allocate(4096);
		br.read(cb);
		br.close();
		
		String result = cb.flip().toString();
		cb.clear();
		return result;
	}
	
	public void writeContent(String fileName, String content) throws Exception{
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
		bw.write(content);
		bw.close();
	}
	

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		UtilSetting us = new UtilSetting();
		
		String path = args[0];
		String prefix = args[1];
		
		CharBuffer buffer = CharBuffer.allocate(4096);
		
		String content;
		String[] array;
		
		content = us.readContent(path + "/.dependlib");
		array = content.split(",");
		for (String str : array) {
			if(null != str && !"".equals(str.trim())){
				buffer.put(LIB_SRT.replaceAll("@@target_name@@", prefix + str.trim()));
			}
		}
		
		content = us.readContent(path + "/.dependrsl");
		array = content.split(",");
		for (String str : array) {
			if(null != str && !"".equals(str.trim())){
				buffer.put(RSL_STR.replaceAll("@@target_name@@", prefix + str.trim()));
			}	
		}
		
		String src_content = us.readContent(path + "/.actionScriptProperties");
		String dst_content = src_content.replaceAll("@@lib_rsl@@", buffer.flip().toString());
		us.writeContent(path + "/.actionScriptProperties", dst_content);
		
		buffer.clear();
	}

}
