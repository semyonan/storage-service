package org.example.repositories;

import lombok.Getter;
import org.example.entities.Pair;
import org.example.merging.Merger;
import org.example.ssTable.SSTable;
import org.example.ssTable.SSTableWriter;
import org.example.ssTable.compression.Compressor;
import org.example.ssTable.compression.ZlibCompressor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.*;

@Getter
public class PairRepositoryLsmImpl implements PairRepository {

    private final Timer timer;
    @Value("${mergeInterval}")
    private int mergeInterval;
    @Value("${maxCapacity}")
    private int maxCapacity;
    @Value("${maxSegmentSize}")
    private int maxSegmentSize;
    @Value("${locationString}")
    private String locationString;
    private TreeMap<String, String> tree = new TreeMap<String, String>();
    private List<SSTable> tablesList = new LinkedList<SSTable>();
    private final Merger merger;
    private final Compressor compressor;


    public PairRepositoryLsmImpl() {
        this.tree = new TreeMap<String, String>();
        this.merger = new Merger();
        this.compressor = new ZlibCompressor();
        timer = mergePlanning();
    }



    public void save(Pair<String, String> pair) {
        tree.put(pair.getKey(), pair.getValue());
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

    public Pair<String, String> getReferenceById(String id) {
        String value;

        if (tree.containsKey(id)) {
            value = tree.get(id);
        } else {
            value = getFromSegments(id);
        }

        return new Pair<String, String>(id, value);
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
