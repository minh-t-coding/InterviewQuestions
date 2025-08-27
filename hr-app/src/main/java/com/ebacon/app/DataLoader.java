package com.ebacon.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.ebacon.app.parsers.IDataParser;
import com.ebacon.app.parsers.JsonDataParser;

public class DataLoader {
    private final IDataParser parser;

    public DataLoader(String filePath) throws Exception {
        InputStream in = getResourceAsStreamOrFile(filePath);
        if (in == null) throw new IllegalArgumentException("File not found: " + filePath);

        // Simple extension-based selection
        if (filePath.endsWith(".json") || filePath.endsWith(".jsonc")) {
            parser = new JsonDataParser(in);
        } else {
            throw new UnsupportedOperationException("Unsupported file type: " + filePath);
        }
    }

    private InputStream getResourceAsStreamOrFile(String path) throws IOException {
        // Try classpath first
        InputStream in = DataLoader.class.getResourceAsStream(path.startsWith("/") ? path : "/" + path);
        if (in != null) return in;

        // Fallback to filesystem
        File file = new File(path);
        if (file.exists()) return new FileInputStream(file);

        return null;
    }

    public <T> T[] loadArray(String key, Class<T[]> clazz) throws Exception {
        return parser.parseArray(key, clazz);
    }

    public <T> T loadObject(String key, Class<T> clazz) throws Exception {
        return parser.parseObject(key, clazz);
    }
}
