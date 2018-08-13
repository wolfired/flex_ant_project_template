import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;

public class UtilASConfig {
	private static final String SRC_SRT = "\"../TARGET_NAME/src\",";

	private String _root_path;
	private String _project_path;

	private ArrayList<String> _array;

	public UtilASConfig(String root_path, String project_path) {
		_root_path = root_path;
		_project_path = project_path;
		_array = new ArrayList<String>();
	}

	public void done() throws Exception {
		this.collectAll(_project_path);

		String r_content = this.readContent(_project_path + "/asconfig.json");

		r_content = r_content.replaceAll("@@external_src@@",
				StringUtils.join(_array.toArray(), ""));

		this.writeContent(_project_path + "/asconfig.json", r_content);
	}

	private void collectAll(String path) throws Exception {
		this.collect(path + "/.dependlib");
		this.collect(path + "/.dependrsl");
	}

	private void collect(String file) throws Exception {
		String content = this.readContent(file);
		String[] array = content.split(",");
		for (String str : array) {
			str = str.trim();

			if (null == str || "".equals(str)) {
				continue;
			}

			this.collectAll(_root_path + "/" + str);

			str = "\"../" + str + "/src\",";

			if (_array.contains(str)) {
				continue;
			}

			_array.add(str);
		}
	}

	private String readContent(String fileName) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		CharBuffer cb = CharBuffer.allocate(4096);
		br.read(cb);
		br.close();

		String result = cb.flip().toString();
		cb.clear();
		return result;
	}

	private void writeContent(String fileName, String content) throws Exception {
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
		bw.write(content);
		bw.close();
	}

	public static void main(String[] args) throws Exception {
		new UtilASConfig(args[0], args[1]).done();
	}
}
