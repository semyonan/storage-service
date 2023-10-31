package org.example.ssTable;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.zip.Inflater;

public class SSTableReader {

    public static byte[] decompress(byte[] compressedData, int offset, int l) {
        ByteArrayOutputStream baos = null;
        Inflater iflr = new Inflater();
        iflr.setInput(compressedData, offset, l);
        baos = new ByteArrayOutputStream();
        byte[] tmp = new byte[4*1024];
        try{
            while(!iflr.finished()){
                int size = iflr.inflate(tmp);
                baos.write(tmp, 0, size);
            }
        } catch (Exception ex){
             
        } finally {
            try{
                if(baos != null) baos.close();
            } catch(Exception ex){}
        }
         
        return baos.toByteArray();
    }

    public static byte[] read(String path, LinkedHashMap<String, Integer> keyIndexes) {
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

                    buff.put(decompress(res, 0, curIndex));
                    curIndex = 0;
                }
            } 
        } catch(IOException ex){
             
            System.out.println(ex.getMessage());
        }
        
        return buff.array();
    }

    public static byte[] read(String pathString, int start, int end) {
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
                    buff.put(decompress(res, 0, curIndex));
                }
            } 
        } catch(IOException ex){
             
            System.out.println(ex.getMessage());
        }
        
        return buff.array();
    }
   
}
