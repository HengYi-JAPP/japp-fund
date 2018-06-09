package com.hengyi.japp.fund.client;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Constant {
    public static final String TMP_PATH;

    static {
        try {
            File tmpDir = FileUtils.getFile(System.getProperty("java.io.tmpdir"), "japp-fund-client");
            FileUtils.forceMkdir(tmpDir);
            TMP_PATH = tmpDir.getPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
