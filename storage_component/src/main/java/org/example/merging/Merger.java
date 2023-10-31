package org.example.merging;

import org.example.ssTable.SSTable;
import org.example.ssTable.SSTableParser;
import org.example.ssTable.SSTableReader;
import org.example.ssTable.SSTableWriter;
import org.example.ssTable.compression.Compressor;

import java.util.*;

public class Merger {

    private void saveNewSSTable(String location, int maxSegmentSize, LinkedList<Map.Entry<String, String>> mergedTwoSStable, LinkedList<SSTable> newTableList, Compressor compressor) {
        var filePath = location + "SSTable" + String.valueOf(UUID.randomUUID().toString() + ".txt");
        var indexes = SSTableWriter.write(filePath, mergedTwoSStable, maxSegmentSize, compressor);

        newTableList.add(new SSTable(filePath, indexes, compressor));
        mergedTwoSStable.clear();
    }

    private void mergeTwoTables(int maxSegmentSize, String locationString,
                                int maxCapacity, LinkedList<Map.Entry<String, String>> mergedTwoSStable,
                               LinkedList<SSTable> newTableList,
                               ListIterator<Map.Entry<String, String>> iterator1,
                               ListIterator<Map.Entry<String, String>> iterator2, Compressor compressor) {

        while (iterator1.hasNext() && iterator2.hasNext()) {
            Map.Entry<String, String> pair1 = iterator1.next();
            Map.Entry<String, String> pair2 = iterator2.next();

            int comparison = pair1.getKey().compareTo(pair2.getKey());
            if (comparison < 0) {
                mergedTwoSStable.add(pair1);
                iterator2.previous(); // Возвращаемся назад для проверки следующей пары
            } else if (comparison == 0) {
                mergedTwoSStable.add(pair1);
            } else {
                mergedTwoSStable.add(pair2);
                iterator1.previous(); // Возвращаемся назад для проверки следующей пары
            }
            if (mergedTwoSStable.size() == maxCapacity) {
                saveNewSSTable(locationString, maxSegmentSize, mergedTwoSStable, newTableList, compressor);
            }
        }
        while (iterator1.hasNext()) {
            mergedTwoSStable.add(iterator1.next());
            if (mergedTwoSStable.size() == maxCapacity) {
                saveNewSSTable(locationString, maxSegmentSize, mergedTwoSStable, newTableList, compressor);
            }
        }
        while (iterator2.hasNext()) {
            mergedTwoSStable.add(iterator2.next());
            if (mergedTwoSStable.size() == maxCapacity) {
                saveNewSSTable(locationString, maxSegmentSize, mergedTwoSStable, newTableList, compressor);
            }
        }

        if (mergedTwoSStable.size() > 0) {
            saveNewSSTable(locationString, maxSegmentSize, mergedTwoSStable, newTableList, compressor);

        }

    }

    //берем 2 ssTable с конца и начинаем мерджить их в новую
    public LinkedList<SSTable> merge(LinkedList<SSTable> tableList,
                                     int maxCapacity,int maxSegmentSize,
                                     String locationString) {
        var newTableList = new LinkedList<SSTable>();
        ListIterator<SSTable> ssTables = tableList
                .listIterator(tableList.size());
        List<Map.Entry<String, String>> pairs1 = null;
        Compressor compressor = null;

        if (ssTables.hasPrevious()) {
            var ssTable1 = ssTables.previous();
            compressor = ssTable1.getCompressor();
            pairs1 = SSTableParser.parse(SSTableReader
                    .read(ssTable1.getFilePath(), ssTable1.getKeyIndexes(), compressor));
        }

        LinkedList<Map.Entry<String, String>> mergedTwoSStable = new LinkedList<>();

        while(ssTables.hasPrevious()) {
            var ssTable2 = ssTables.previous();
            var pairs2 = SSTableParser.parse(SSTableReader
                    .read(ssTable2.getFilePath(), ssTable2.getKeyIndexes(), compressor));

                ListIterator<Map.Entry<String, String>> iterator1 = pairs1.listIterator();
                ListIterator<Map.Entry<String, String>> iterator2 = pairs2.listIterator();
                mergeTwoTables(maxSegmentSize, locationString, maxCapacity, mergedTwoSStable, newTableList, iterator1, iterator2, compressor);
                pairs1 = mergedTwoSStable;
            }
        return newTableList;
        }
}
