package org.example.ssTable;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;

import org.example.ssTable.compression.Compressor;

public class SSTableReader {

    public static byte[] read(String path, LinkedHashMap<String, Integer> keyIndexes, Compressor compressor) {
        var res = new byte[1024];
        ByteBuffer buff = ByteBuffer.wrap(new byte[32*1024]);
        var set = keyIndexes.keySet();
            
        var i = set.iterator();
        int curIndex = 0;
        int length = 0;
        i.next();
        var index = keyIndexes.get(i.next());
        try (FileInputStream fin=new FileInputStream(path))
        {
            byte c;
            while((c=(byte)fin.read())!=-1){
                res[curIndex] = c;
                curIndex++;
                length++;

                if (length == index) {
                    if (i.hasNext()) {
                        index = keyIndexes.get(i.next());
                    }

                    buff.put(compressor.decompress(res, 0, curIndex));
                    curIndex = 0;
                }
            } 
        } catch(IOException ex){
             
            System.out.println(ex.getMessage());
        }
        
        return buff.array();
    }

    public static byte[] read(String pathString, int start, int end, Compressor compressor) {
        var res = new byte[1024];
        ByteBuffer buff = ByteBuffer.wrap(new byte[32*1024]);

        try (FileInputStream fin=new FileInputStream(pathString))
        {
            fin.skip(start);
            byte c;
            int curIndex = 0;
            int length = start;
            while((c=(byte)fin.read())!=-1){
                res[curIndex] = c;
                curIndex++;
                length++;

                if (length == end) {
                    buff.put(compressor.decompress(res, 0, curIndex));
                }
            } 
        } catch(IOException ex){
             
            System.out.println(ex.getMessage());
        }
        
        return buff.array();
    }
   
}
