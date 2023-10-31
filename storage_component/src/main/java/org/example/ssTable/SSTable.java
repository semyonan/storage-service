package org.example.ssTable;

import lombok.Getter;

import java.io.File;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.LinkedHashMap;

@Getter
public class SSTable {
    private String filePath;
    private LinkedHashMap<String, Integer> keyIndexes;

    public SSTable(String filePath, LinkedHashMap<String, Integer> keyIndexes) {
        this.filePath = filePath;
        this.keyIndexes = keyIndexes;
    }

    public boolean keyExists(String key) {
        var set = keyIndexes.keySet();
        var i = set.iterator();
        String prevKey = "";
        
        if (i.hasNext()) {
            prevKey = i.next();
            if (prevKey == key) {
                return true;
            }
        }

        while (i.hasNext()) {
            var cur = i.next();
            if ((key.compareTo(prevKey) > 0 && key.compareTo(cur) < 0) || (key.equals(prevKey)) || (key.equals(cur))) {
                return true;
            }
            prevKey = cur;
        }
        return false;
    }

    private AbstractMap.SimpleEntry<Integer, Integer> getKeyIndexes(String key) {
        var set = keyIndexes.keySet();
        var i = set.iterator();
        String prevKey = "";
        
        if (i.hasNext()) {
            prevKey = i.next();
            if (!i.hasNext()) {
                return  new AbstractMap.SimpleEntry<>(keyIndexes.get(prevKey), keyIndexes.get(prevKey));
            }
        }

        while (i.hasNext()) {
            var cur = i.next();
            if ((key.compareTo(prevKey) > 0 && key.compareTo(cur) < 0) || (key.equals(prevKey)) || (key.equals(cur))) {
                return new AbstractMap.SimpleEntry<>(keyIndexes.get(prevKey), keyIndexes.get(cur));
            }
            prevKey = cur;
        }
        return null;
    }

    public String getValue(String key) {
        var indexes = getKeyIndexes(key);
        var pairs = SSTableParser.parse(SSTableReader.read(filePath, indexes.getKey(), indexes.getValue()));
        int index = Collections.binarySearch(pairs.stream()
                .map(pair -> pair.getKey()).toList(), key);

        if (index < 0) {
            return null;
        }
        return pairs.get(index).getValue();
    }

    public void deleteTable() {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
