package com.example.project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class Utils {
    /**
     * Converts a map into a treeMap and reverse the order of the key-value pairs.
     * @param answers
     * @return
     */
    public static TreeMap<String, Boolean> convertMapToTreeMap(Map<String, Boolean> answers ) {
        Iterator<Map.Entry<String, Boolean>> iterator = answers.entrySet().iterator();
        TreeMap<String, Boolean> treeMap = new TreeMap<>();

        while (iterator.hasNext()) {
            Map.Entry<String, Boolean> entry = iterator.next();
            treeMap.put(entry.getKey(), entry.getValue());
        }

        return treeMap;
    }

    /**
     * Subtracts the number of answers the question had from the rest of answerIds (offset).
     * @param answerIdList
     * @param offset
     */
    public static void updateOffset(ArrayList<Integer> answerIdList, Integer offset) {
        for (int i = 0; i < answerIdList.size(); i++) {
            answerIdList.set(i, answerIdList.get(i) - offset);
        }
    }
}
