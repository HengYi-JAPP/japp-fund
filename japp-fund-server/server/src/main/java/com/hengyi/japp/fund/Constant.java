package com.hengyi.japp.fund;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by jzb on 16-10-20.
 */
public class Constant {
    public static final String TMP_PATH;
    public static final Properties CONFIG;
    public static final String BALANCE_SUM_PATH;
    public static final String CDHP = "_CDHP_";
    public static final String OA_DS = "java:/oaDS";
    public static final String FI_DS = "java:/fiDS";
    public static final String JWT_KEY = "test";

    static {
        try {
            File tmpDir = FileUtils.getFile(System.getProperty("java.io.tmpdir"), "japp-fund-client");
            FileUtils.forceMkdir(tmpDir);
            TMP_PATH = tmpDir.getPath();

            final String configPath = System.getProperty("FUND_SERVER_CONFIG", "/home/jzb/japp-fund-server/config.properties");
            CONFIG = new Properties();
            CONFIG.load(new FileInputStream(FileUtils.getFile(configPath)));

            BALANCE_SUM_PATH = CONFIG.getProperty("BALANCE_SUM_PATH", "/home/jzb/japp-fund-server/BalanceSum");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
