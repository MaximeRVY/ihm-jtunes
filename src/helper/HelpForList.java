package helper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class HelpForList {
	
	private HelpForList(){}
	public static final HelpForList instance = new HelpForList();
	
	
	public Integer indexById(List<Map<String, Object>> list, Integer id){
		int i = 0;
		for(Map<String, Object> l : list){
			if( ((Integer) l.get("id")).equals(id) )
				return i;
			i += 1;
		}
		
		return -1;
	}
	
	
	public Integer indexByName(List<Map<String, Object>> list, String name){
		int i = 0;
		for(Map<String, Object> l : list){
			if( ((String) l.get("name")).equals(name) )
				return i;
			i += 1;
		}
		
		return -1;
	}
	
	public Comparator<Map<String, Object>> comparatorOldId = new Comparator<Map<String, Object>> (){
	    public int compare(Map<String, Object> m1, Map<String, Object> m2) {
	        return ((Integer) m1.get("old_index")).compareTo((Integer) m2.get("old_index"));
	    }

	};

	
	public void SortListByOldId(List<Map<String, Object>> list){
		Collections.sort(list, comparatorOldId);
	}

}
