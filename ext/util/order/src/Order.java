import java.util.HashSet;
import java.util.Set;

public class Order {
	public boolean done;
	public String key;
	public Set<String> depends;
	public Order(String key) {
		done = false;
		this.key = key;
		depends = new HashSet<String>();
	}
	
	public boolean isDone(Set<String> total){
		if(depends.isEmpty()){
			return true;
		}
		
		for (String depend : depends) {
			if(!total.contains(depend)){
				return false;
			}
		}
		
		return true;
	}
}
