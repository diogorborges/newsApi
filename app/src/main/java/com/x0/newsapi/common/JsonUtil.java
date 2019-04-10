package com.x0.newsapi.common;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class JsonUtil {

    public static String loadJsonFromResource(String resourceName) throws IOException {
        InputStream inputStream = JsonUtil.class.getClassLoader().getResourceAsStream(resourceName);
        return IOUtils.toString(inputStream, "UTF-8");
    }
}