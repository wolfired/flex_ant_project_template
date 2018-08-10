import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.CharBuffer;
import java.util.UUID;


public class UtilASConfig {
	private static final String SRC_SRT = "\"../TARGET_NAME/src\",";
	
	private String _path;
	
	public UtilASConfig(String path) {
		_path = path;
	}
	
	public void done() throws Exception{
		CharBuffer buffer = CharBuffer.allocate(4096);
		
		String content;
		String[] array;
		
		content = this.readContent(_path + "/.dependlib");
		array = content.split(",");
		for (String str : array) {
			if(null != str && !"".equals(str.trim())){
				buffer.put(SRC_SRT.replaceAll("TARGET_NAME", str.trim()));
			}
		}
		
		content = this.readContent(_path + "/.dependrsl");
		array = content.split(",");
		for (String str : array) {
			if(null != str && !"".equals(str.trim())){
				buffer.put(SRC_SRT.replaceAll("TARGET_NAME", str.trim()));
			}	
		}
		
		String r_content = this.readContent(_path + "/asconfig.json");


		r_content = r_content.replaceAll("@@external_src@@", buffer.flip().toString());

		this.writeContent(_path + "/asconfig.json", r_content);
		
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
		new UtilASConfig(args[0]).done();
	}
}
