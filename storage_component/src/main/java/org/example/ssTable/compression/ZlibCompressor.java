package org.example.ssTable.compression;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZlibCompressor implements Compressor {

    @Override
    public byte[] decompress(byte[] compressedData, int offset, int l) {
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

    @Override
    public byte[] compress(byte[] data, int offset, int l) {
        ByteArrayOutputStream baos = null;
        Deflater dfl = new Deflater();
        dfl.setLevel(Deflater.BEST_COMPRESSION);
        dfl.setInput(data, offset, l);
        dfl.finish();
        baos = new ByteArrayOutputStream();
        byte[] tmp = new byte[4*1024];
        try{
            while(!dfl.finished()){
                int size = dfl.deflate(tmp);
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
    
}
