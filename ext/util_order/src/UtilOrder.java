import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

public class UtilOrder {
	private static final String FILE_ENCODING = "-Dfile.encoding=FILE_ENCODING";
	private static final String TARGETS_NAME = "-Dtargets.name=\"TARGETS_NAME\"";
	private static final String ANT = "ant.bat";

	private String _root_path;
	private ArrayList<String> _targets;
	private Map<String, Integer> _kv;

	private ProcessBuilder _pb;

	public UtilOrder(String root_path, String targets) {
		_root_path = root_path;

		if (null == targets || "".equals(targets)) {
			_targets = new ArrayList<String>();
		} else {
			_targets = new ArrayList<String>(Arrays.asList(targets.split(",")));
		}

		_kv = new HashMap<String, Integer>();

		_pb = new ProcessBuilder();
		_pb.directory(new File(_root_path));
		_pb.redirectErrorStream(true);
		_pb.environment().put("ANT_OPTS", FILE_ENCODING.replaceAll("FILE_ENCODING", System.getProperty("file.encoding")));
	}

	public void order() throws Exception {
		this.getTargetsFile();

		Map<Integer, List<String>> ks = new HashMap<Integer, List<String>>();
		Integer value;
		for (Entry<String, Integer> kv : _kv.entrySet()) {
			value = kv.getValue();
			if (!ks.containsKey(value)) {
				ks.put(value, new ArrayList<String>());
			}
			ks.get(value).add(kv.getKey());
		}

		Integer[] i_arr = ks.keySet().toArray(new Integer[0]);
		Arrays.sort(i_arr);
		Collections.reverse(Arrays.asList(i_arr));

		for (Integer integer : i_arr) {
//			System.out.println(StringUtils.join(ks.get(integer), ","));
			this.exec(StringUtils.join(ks.get(integer), ","));
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

	private void getTargetsFile() throws Exception {
		ArrayList<File> targets_file = new ArrayList<File>(Arrays.asList(new File(_root_path + "/src").listFiles()));
		for (File file : targets_file) {
			if (file.isDirectory()
					&& (0 == _targets.size() || _targets.contains(file.getName()))) {
				this.calc(file);
			}
		}
	}

	private void calc(File dir) throws Exception {
		String dir_name = dir.getName();
		this.mark(dir_name);
		
		String[] depend_arr;

		depend_arr = this.readContent(dir.getAbsolutePath() + "/.dependlib").split(",");
		for (String depend : depend_arr) {
			if (null != depend && !"".equals(depend)) {
				this.mark(depend);
				this.calc(new File(_root_path + "/src/" + depend));
			}
		}

		depend_arr = this.readContent(dir.getAbsolutePath() + "/.dependrsl").split(",");
		for (String depend : depend_arr) {
			if (null != depend && !"".equals(depend)) {
				this.mark(depend);
				this.calc(new File(_root_path + "/src/" + depend));
			}
		}
	}
	
	private void mark(String key){
		if (_kv.containsKey(key)) {
			_kv.put(key, _kv.get(key) + 1);
		} else {
			_kv.put(key, 1);
		}
	}

	private void exec(String targets_str) throws Exception {
		_pb.command().clear();
		_pb.command(ANT, "-f", "build_order.xml", "build_more", TARGETS_NAME.replaceAll("TARGETS_NAME", targets_str));
		Process ps = _pb.start();
		InputStream is = ps.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		while (null != (line = br.readLine())) {
			System.out.println(line);
		}
		br.close();
	}

	public static void main(String[] args) throws Exception {
//		new UtilOrder("E:/workspace_git/flex_ant_project_template", "embed,common,bag").order();
		new UtilOrder(args[0], 1 == args.length ? "" : args[1]).order();
	}
}
