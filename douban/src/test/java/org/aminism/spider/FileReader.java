package org.aminism.spider;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;

/**
 * @author ygxie
 * @date 2018/8/21.
 */
public class FileReader {
    private String getFile(String name) {
        String path = getClass().getClassLoader().getResource(name).toString();
        path = path.replace("\\", "/");
        if (path.contains(":")) {
            path = path.replace("file:/", "");
        }
        String input = null;
        try {
            input = FileUtils.readFileToString(new File(path), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    public Document getDoc(String file) {
        String html = getFile(file);
        return Jsoup.parse(html);
    }
}
