package com.ebacon.app.parsers;

import java.io.InputStream;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDataParser implements IDataParser {
    private final ObjectMapper mapper;
    private final JsonNode rootNode;
    
    public JsonDataParser(InputStream file) throws Exception {
        mapper = new ObjectMapper();
        mapper.enable(JsonParser.Feature.ALLOW_COMMENTS);
        rootNode = mapper.readTree(file);
    }

    @Override
    public <T> T[] parseArray(String key, Class<T[]> clazz) throws Exception {
        JsonNode node = rootNode.get(key);
        if (node == null) throw new IllegalArgumentException("Key not found: " + key);
        return mapper.treeToValue(node, clazz);
    }

    @Override
    public <T> T parseObject(String key, Class<T> clazz) throws Exception {
        JsonNode node = rootNode.get(key);
        if (node == null) throw new IllegalArgumentException("Key not found: " + key);
        return mapper.treeToValue(node, clazz);
    }
}
