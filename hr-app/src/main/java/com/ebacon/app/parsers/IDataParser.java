package com.ebacon.app.parsers;

public interface IDataParser {
        <T> T[] parseArray(String key, Class<T[]> clazz) throws Exception;
        <T> T parseObject(String key, Class<T> clazz) throws Exception;
}
