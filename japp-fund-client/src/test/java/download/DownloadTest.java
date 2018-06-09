package download;

import com.google.common.collect.ImmutableMap;
import com.hengyi.japp.fund.client.Api;
import com.sun.security.auth.UserPrincipal;
import okhttp3.Request;
import okhttp3.Response;
import org.jzb.J;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;

import static com.hengyi.japp.fund.client.Api.OK_CLIENT;
import static com.hengyi.japp.fund.client.Constant.TMP_PATH;

public class DownloadTest {
    static Principal principal = new UserPrincipal("eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNqqViouTVKyUnIyTM3PKCozNk_KzAj2DcgqrQpMKQlRqgUAAAD__w.UiE9K9H8UmS0wXwEGQbnL3li6401t6mQvq1GavuE-Y_4U22DIR7bmptsmNyBIgAsRKwCrFMSFyWr2o7zRr2C2Q");

    public static void main(String[] args) throws Exception {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
        builder.put("year", "2017");
        builder.put("month", "9");
        builder.put("divideYear", "2017");
        builder.put("divideMonth", "10");
        builder.put("divideDay", "18");

        String token = principal.getName();
        Request request = new Request.Builder()
                .addHeader("authorization", "Bearer " + token)
                .url(Api.queryUrl(Api.Urls.EXPORTS, builder.build()))
                .build();
        try (Response response = OK_CLIENT.newCall(request).execute();
             InputStream is = response.body().byteStream()) {
            if (response.isSuccessful()) {
                Path path = Paths.get(TMP_PATH, J.uuid58() + ".xlsx");
                Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }
}
