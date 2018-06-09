package yaml;

import com.fasterxml.jackson.databind.JsonNode;
import com.hengyi.japp.fund.interfaces.xlsx.export.balanceSum.BalanceSumType;
import com.hengyi.japp.fund.interfaces.xlsx.export.balanceSum.BalanceSumTypeConfigWatchTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.hengyi.japp.fund.Constant.CONFIG;

public class YamlTest {

    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    static {
        System.setProperty("FUND_SERVER_CONFIG", "/home/jzb/git/com.hengyi/japp-fund-server/server/src/test/resources/yaml/config.properties");
    }

    public static void main(String[] args) throws IOException {
        Path dir = Paths.get(CONFIG.getProperty("BALANCE_SUM_PATH"));
        new BalanceSumTypeConfigWatchTask(dir, "BalanceSumType.yml")
                .watch(node -> {
                    node.fields().forEachRemaining(entry -> {
                        final String key = entry.getKey();
                        final JsonNode value = entry.getValue();
                        System.out.println(key);
                        System.out.println(value);
                    });
                    test(BalanceSumType.SS);
                    test(BalanceSumType.SS_XS);
                });
        reader.readLine();
    }

    private static void test(BalanceSumType type) {
        System.out.println(type.getDisplayName());
        for (String s : type.getCorporationIds()) {
            System.out.println(s);
        }
    }

}
