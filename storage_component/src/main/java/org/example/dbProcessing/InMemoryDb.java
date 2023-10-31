package org.example.dbProcessing;

import lombok.Getter;
import org.example.merging.Merger;
import org.example.ssTable.SSTable;
import org.example.ssTable.SSTableWriter;
import org.example.ssTable.compression.Compressor;
import org.example.ssTable.compression.ZlibCompressor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Getter
public class InMemoryDb {

    private final Timer timer;
    private final int mergeInterval;
    private final int maxCapacity;
    private final int maxSegmentSize;
    private final String locationString;
    private TreeMap<String, String> tree = new TreeMap<String, String>();
    private List<SSTable> tablesList = new LinkedList<SSTable>();
    private final Merger merger;
    private final Compressor compressor;
    

    public InMemoryDb(@Value("${mergeInterval}") int mergeInterval,
                      @Value("${maxCapacity}") int maxCapacity,
                      @Value("${maxSegmentSize}") int maxSegmentSize,
                      @Value("${locationString}") String locationString) {
        this.mergeInterval = mergeInterval;
        this.maxCapacity = maxCapacity;
        this.maxSegmentSize = maxSegmentSize;
        this.locationString = locationString;
        this.tree = new TreeMap<String, String>();
        this.merger = new Merger();
        System.out.println(locationString);
        this.compressor = new ZlibCompressor();
        timer = mergePlanning();
    }

    public void set(String key, String value) {
        tree.put(key, value);
        System.out.println(tree.size());
        System.out.println(maxCapacity);

        if (tree.size() == maxCapacity) {
            createSStable();
        }
    }

    private void createSStable() {
        var filePath = locationString + "SSTable" + String.valueOf(tablesList.size() + ".txt");
        var indexes = SSTableWriter.write(filePath, new LinkedList<>(tree.entrySet()), maxSegmentSize, compressor);

        tablesList.add(new SSTable(filePath, indexes, compressor));
        tree.clear();
    }

    public String get(String key) {
        String value;

        if (tree.containsKey(key)) {
            value = tree.get(key);
        } else {
            value = getFromSegments(key);
        }
        
        return value;
    }

    private String getFromSegments(String key) {
        for (var i = tablesList.size() - 1; i >= 0; i--) {
            var table = tablesList.get(i);
            if (table.keyExists(key)) {
                var res = table.getValue(key);
                if (res != null) {
                    return res;
                }
            }
        }
        return null;
    }

    private Timer mergePlanning() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (tablesList.size() > 1) {
                    var mergedTablesList = merger.merge(new LinkedList<>(tablesList), maxCapacity, maxSegmentSize, locationString);
                    //удалили старые таблицы
                    for (var table : tablesList) {
                        table.deleteTable();
                    }
                    //присвоили новые
                    tablesList = mergedTablesList;
                }
            }
        };
        // Запускаем задачу с заданным интервалом
        timer.schedule(task, mergeInterval, mergeInterval);
        return timer;
    }
} 
