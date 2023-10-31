package org.example.ssTable.compression;

public interface Compressor {
    public byte[] decompress(byte[] compressedData, int offset, int l);
    public byte[] compress(byte[] data, int offset, int l);
} 