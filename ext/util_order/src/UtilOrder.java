import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class UtilOrder {
//	private static final String FILE_ENCODING = "-Dfile.encoding=FILE_ENCODING";
//	private static final String TARGETS_NAME = "-Dtargets.name=\"TARGETS_NAME\"";

	private String _root_path;
	private Set<String> _targets;
	private Map<String, Order> _order_map;
	
	private List<Set<String>> _phases;
	private Set<String> _dones;

//	private ProcessBuilder _pb;

	public UtilOrder(String root_path, String targets) {
		_root_path = root_path;

		if (null == targets || "".equals(targets)) {
			_targets = new HashSet<String>();
		} else {
			_targets = new HashSet<String>(Arrays.asList(targets.split(",")));
		}

		_order_map = new HashMap<String, Order>();
		_phases = new ArrayList<Set<String>>();
		_dones = new HashSet<String>();

//		_pb = new ProcessBuilder();
//		_pb.directory(new File(_root_path));
//		_pb.redirectErrorStream(true);
//		_pb.environment().put("ANT_OPTS", FILE_ENCODING.replaceAll("FILE_ENCODING", System.getProperty("file.encoding")));
	}

	public int order() throws Exception {
		this.calc_depend();
		
		
		int exit_code = 0;
		ArrayList<String> as = new ArrayList<String>();
		if(this.solve_depend()){
			Iterator<Set<String>> iter = _phases.iterator();
			while(0 == exit_code && iter.hasNext()){
//				System.out.println(StringUtils.join(iter.next().toArray(new String[0]), ","));
//				exit_code = this.exec(StringUtils.join(iter.next().toArray(new String[0]), ","));
				as.add(StringUtils.join(iter.next().toArray(new String[0]), ","));
			}
			
			System.out.println(StringUtils.join(as.toArray(new String[0]), ";"));
		}
		
		return exit_code;
	}

	private String readContent(String fileName) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		CharBuffer cb = CharBuffer.allocate(4096);
		br.read(cb);
		br.close();

		String result = cb.flip().toString();
		cb.clear();
		
		result.replaceAll("\\s", "");
		return result;
	}

	private void calc_depend() throws Exception {
		ArrayList<File> targets_file = new ArrayList<File>(Arrays.asList(new File(_root_path + "/src").listFiles()));
		for (File file : targets_file) {
			if (file.isDirectory() && (0 == _targets.size() || _targets.contains(file.getName()))) {
				this.calc(file);
			}
		}
	}

	private void calc(File dir) throws Exception {
		String key = dir.getName();
		
		if(_order_map.containsKey(key)){
			return;
		}
		
		Order key_order = new Order(key);
		_order_map.put(key, key_order);
		
		String[] depend_arr;

		depend_arr = this.readContent(dir.getAbsolutePath() + "/.dependlib").split(",");
		for (String depend : depend_arr) {
			depend = depend.trim();
			if (null != depend && !"".equals(depend)) {
				key_order.depends.add(depend);
				
				this.calc(new File(_root_path + "/src/" + depend));
			}
		}

		depend_arr = this.readContent(dir.getAbsolutePath() + "/.dependrsl").split(",");
		for (String depend : depend_arr) {
			depend = depend.trim();
			if (null != depend && !"".equals(depend)) {
				key_order.depends.add(depend);
				
				this.calc(new File(_root_path + "/src/" + depend));
			}
		}
	}
	
	private boolean solve_depend(){
		Set<String> phase;
		
		Collection<Order> orders = _order_map.values();
		
		int max_loop = 16;
		while(0 < --max_loop){
			phase = new HashSet<String>();
			for (Order order : orders) {
				if(!order.done && order.isDone(_dones)){
					order.done = true;
					phase.add(order.key);
				}
			}
			_phases.add(phase);
			_dones.addAll(phase);
			
			if(_dones.size() == orders.size()){
				break;
			}
		}
		
		if(0 == max_loop){
			System.out.println("Loop depend ?");
			return false;
		}
		
		return true;
	}
	
//	private int exec(String targets_str) throws Exception {
//		_pb.command().clear();
//		_pb.command(this.ant(), "-f", "build_order.xml", "build_more", TARGETS_NAME.replaceAll("TARGETS_NAME", targets_str));
//		Process ps = _pb.start();
//		InputStream is = ps.getInputStream();
//		InputStreamReader isr = new InputStreamReader(is);
//		BufferedReader br = new BufferedReader(isr);
//		String line;
//		while (null != (line = br.readLine())) {
//			System.out.println(line);
//		}
//		br.close();
//
//		return ps.exitValue();
//	}

//	private String ant(){
//		String os_name = System.getProperty("os.name");
//		if(null == os_name || "".equals(os_name) || os_name.startsWith("Window") || os_name.startsWith("window")){
//			return "ant.bat";
//		}
//		
//		return "ant";
//	}

	public static void main(String[] args) throws Exception {
//		System.exit(new UtilOrder("E:/workspace_git/flex_ant_project_template", "").order());
		System.exit(new UtilOrder(args[0], 1 == args.length ? "" : args[1]).order());
	}
}
