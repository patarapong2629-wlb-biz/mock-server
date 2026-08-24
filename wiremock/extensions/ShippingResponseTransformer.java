package com.wlb.mockserver.wiremock;

import com.github.tomakehurst.wiremock.extension.ResponseTransformerV2;
import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShippingResponseTransformer implements ResponseTransformerV2 {

  private static final Pattern SHIPPING_METHOD_ID =
      Pattern.compile("\"shipping_method_id\"\\s*:\\s*\"?(\\d+)\"?");

  private static final Map<String, String> CARRIER_BY_METHOD_ID =
      Map.of("1", "KR", "2", "TH", "3", "LM");

  @Override
  public Response transform(Response response, ServeEvent serveEvent) {
    String requestBody = serveEvent.getRequest().getBodyAsString();
    Matcher matcher = SHIPPING_METHOD_ID.matcher(requestBody);
    String methodId = matcher.find() ? matcher.group(1) : null;
    String carrier = CARRIER_BY_METHOD_ID.getOrDefault(methodId, "undefined");
    long trackingSuffix = ThreadLocalRandom.current().nextLong(1_000_000_000L);

    String body = String.format("{\"tracking_number\":\"%s-%d\"}", carrier, trackingSuffix);

    return Response.Builder.like(response)
        .but()
        .status(200)
        .headers(new HttpHeaders(new HttpHeader("Content-Type", "application/json; charset=utf-8")))
        .body(body)
        .build();
  }

  @Override
  public String getName() {
    return "shipping-response";
  }

  @Override
  public boolean applyGlobally() {
    return false;
  }
}
