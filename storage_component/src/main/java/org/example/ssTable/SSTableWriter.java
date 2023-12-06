package org.example.ssTable;

import org.example.ssTable.compression.Compressor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

public class SSTableWriter {
    public static LinkedHashMap<String, Integer> write(String pathString,  LinkedList<Map.Entry<String, String>> data, int maxSegmentSize, Compressor compressor) {
        LinkedHashMap<String, Integer> keyIndexes = new LinkedHashMap<String, Integer>();

        try (FileOutputStream fos=new FileOutputStream(pathString))
        {
            ListIterator<Map.Entry<String, String>> pair = data.listIterator();
            
            int counter = 1;
            int length = 0;

            ByteBuffer buff = ByteBuffer.wrap(new byte[32*1024]);;
            Map.Entry<String, String> entry = null;

            while(pair.hasNext()) {
                
                entry = pair.next();

                Integer keyLength = entry.getKey().length();
                buff.putInt(keyLength);
                buff.put(entry.getKey().getBytes());
                
                Integer valueLength = entry.getValue().length();
                buff.putInt(valueLength);
                buff.put(entry.getValue().getBytes());

                if (counter % maxSegmentSize == 0 || counter == 1) {
                    if (counter != 1) {
                        var compressed = compressor.compress(buff.array(), 0, buff.position());
                        length += compressed.length;
                        fos.write(compressed);
                        fos.flush(); 
                        
                        buff.clear();
                    }
                    keyIndexes.put(entry.getKey(), length);
                }
                counter++;
            }

            if (counter % maxSegmentSize != 0) {
                var compressed = compressor.compress(buff.array(), 0, buff.position());
                length += compressed.length;
                fos.write(compressed);
                fos.flush();        
            
                buff.clear();

                keyIndexes.put(entry.getKey(), length);
            }
            fos.close();
        } catch (IOException ex){
            System.out.println(ex.getMessage());
        } 

        return keyIndexes;
    }



}
