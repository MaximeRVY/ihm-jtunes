package helper;

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

}
