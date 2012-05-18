package helper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ShuffleList {
	public static List<Map<String, Object>> shuffleList(List<Map<String, Object>> a) {
		int n = a.size();
		Random random = new Random();
		random.nextInt();
		for (Integer i = 0; i < n; i++) {
			int change = i + random.nextInt(n - i);
			swap(a, i, change);
		}
		return a;
		
	}

	private static void swap(List<Map<String, Object>> a, int i, int change) {
		Map<String, Object> helper = a.get(i);
		a.set(i, a.get(change));
		a.set(change, helper);
	}
	
	public static Comparator<Map<String, Object>> comparatorOldId = new Comparator<Map<String, Object>> (){
	    public int compare(Map<String, Object> m1, Map<String, Object> m2) {
	   	    		return ((Integer) m1.get("old_index")).compareTo((Integer) m2.get("old_index"));
	    		
	    }

	};

	
	public static void SortListByOldId(List<Map<String, Object>> list){
		Collections.sort(list, comparatorOldId);
	}
}
