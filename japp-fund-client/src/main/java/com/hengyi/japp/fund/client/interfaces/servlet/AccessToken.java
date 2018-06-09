package com.hengyi.japp.fund.client.interfaces.servlet;

import com.google.common.collect.ImmutableMap;
import com.hengyi.japp.fund.client.Api;
import okhttp3.Request;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;
import org.jzb.J;

public class AccessToken {
    private final String casId;
    private final String oaId;
    private String token;

    AccessToken(Assertion assertion) {
        AttributePrincipal attributePrincipal = assertion.getPrincipal();
        casId = attributePrincipal.getName();
        oaId = (String) attributePrincipal.getAttributes().get("oauser");
    }

    String token() throws Exception {
        if (J.nonBlank(token)) {
            return token;
        }
        return _token();
    }

    private String _token() throws Exception {
        ImmutableMap<String, Object> map = ImmutableMap.of("appId", "japp-fund-client", "appSecret", "123456", "cas", casId, "oaId", oaId);
        Request request = new Request.Builder()
                .url(Api.queryUrl(Api.Urls.AUTH + "/jwtToken", map))
                .build();
        token = Api.reqToString(request);
        return token;
    }

}
