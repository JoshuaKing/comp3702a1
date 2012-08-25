package search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class MapQueue extends TreeMap<Integer, List<Node>> {
	List<Node> put(Integer i, Node n) {
		List<Node> l = super.get(i);
		if (l == null) {
			l = new ArrayList<Node>();
			super.put(i, l);
		}
		l.add(n);
		return l;
	}
	
	Node getNext() {
		Map.Entry<Integer, List<Node>> e = super.firstEntry();
		List<Node> l = e.getValue();
		Node n = l.remove(0);
		if (l.size() == 0)
			super.remove(e.getKey());
		
		return n;
	}
}
