package helper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ShuffleList {
	public static void shuffleList(List<Map<String, Object>> a) {
		int n = a.size();
		Random random = new Random();
		random.nextInt();
		for (int i = 0; i < n; i++) {
			int change = i + random.nextInt(n - i);
			((Map<String,Object>) a.get(i)).put("old_index", i);
			swap(a, i, change);
		}
	}

	private static void swap(List<Map<String, Object>> a, int i, int change) {
		Map<String, Object> helper = a.get(i);
		a.set(i, a.get(change));
		a.set(change, helper);
	}
}
