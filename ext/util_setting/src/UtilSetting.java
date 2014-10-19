import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.CharBuffer;


public class UtilSetting {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String path = args[0];
		String prefix = args[1];
		
		CharBuffer lib_rsl_cb = CharBuffer.allocate(4096);
		CharBuffer setting_cb = CharBuffer.allocate(4096);
		
		String lib_str = "<libraryPathEntry kind=\"3\" linkType=\"1\" path=\"/@@lib_name@@/bin/@@lib_name@@.swc\" useDefaultLinkType=\"false\"/>";
		File lib_dir = new File(path + "/.lib");
		String[] lib_list = lib_dir.list();
		for (int i = 0; i < lib_list.length; ++i) {
			lib_rsl_cb.put(lib_str.replaceAll("@@lib_name@@", prefix + lib_list[i].split("\\.")[0]));
		}
		
		String rsl_str = "<libraryPathEntry kind=\"3\" linkType=\"2\" path=\"/@@lib_name@@/bin/@@lib_name@@.swc\" useDefaultLinkType=\"false\"/>";
		File rsl_dir = new File(path + "/.rsl");
		String[] rsl_list = rsl_dir.list();
		for (int i = 0; i < rsl_list.length; ++i) {
			lib_rsl_cb.put(rsl_str.replaceAll("@@lib_name@@", prefix + rsl_list[i].split("\\.")[0]));
		}
		
		BufferedReader br = new BufferedReader(new FileReader(path + "/.actionScriptProperties"));
		br.read(setting_cb);
		br.close();
		
		BufferedWriter wr = new BufferedWriter(new FileWriter(path + "/.actionScriptProperties"));
		wr.write(setting_cb.flip().toString().replaceAll("@@lib_rsl@@", lib_rsl_cb.flip().toString()));
		wr.close();
		
		lib_rsl_cb.clear();
		setting_cb.clear();
	}

}
