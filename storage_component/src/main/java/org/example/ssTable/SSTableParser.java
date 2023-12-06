package org.example.ssTable;
import org.example.entities.Pair;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SSTableParser {
    public static TreeMap<String, String> parseV1(byte[] bytes) {
        var map = new TreeMap<String, String>();
        ByteBuffer buff = ByteBuffer.wrap(bytes);
        String key = null;

        while(buff.position() < buff.capacity()) {
            int valueLength = buff.getInt();
            if (valueLength == 0) {
                break;
            }
            
            String value = "";
            for(int n = 0; n < valueLength; n++) {
                value += (char)buff.get();
            }

            if (key == null) {
                key = value;
            } else {
                map.put(key, value);
                key = null;
            }
        }

        return map;
    }

    public static List<Map.Entry<String, String>> parse(byte[] bytes) {
        List<Map.Entry<String, String>> pairs = new LinkedList<>();
        ByteBuffer buff = ByteBuffer.wrap(bytes);
        String key = null;

        while(buff.position() < buff.capacity()) {
            int valueLength = buff.getInt();
            if (valueLength == 0) {
                break;
            }

            String value = "";
            for(int n = 0; n < valueLength; n++) {
                value += (char)buff.get();
            }

            if (key == null) {
                key = value;
            } else {
                pairs.add(new Pair<>(key, value));
                key = null;
            }
        }

        return pairs;
    }
}
