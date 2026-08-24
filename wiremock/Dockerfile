FROM wiremock/wiremock:3.13.2 AS wiremock-base

FROM eclipse-temurin:17-jdk AS build
COPY --from=wiremock-base /var/wiremock/lib/wiremock-standalone.jar /opt/wiremock-standalone.jar
COPY ./extensions /extensions
WORKDIR /build
RUN javac -cp /opt/wiremock-standalone.jar -d classes /extensions/ShippingResponseTransformer.java \
    && jar --create --file shipping-response-transformer.jar -C classes .

FROM wiremock-base
COPY ./mappings /home/wiremock/mappings
COPY --from=build /build/shipping-response-transformer.jar /var/wiremock/extensions/shipping-response-transformer.jar
CMD ["--global-response-templating", "--verbose", "--extensions", "com.wlb.mockserver.wiremock.ShippingResponseTransformer"]
