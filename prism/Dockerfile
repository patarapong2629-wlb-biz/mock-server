FROM stoplight/prism:5.15.10
COPY ./openapi.yaml /specs/openapi.yaml
CMD ["mock", "-h", "0.0.0.0", "-p", "4010", "--multiprocess=false", "/specs/openapi.yaml"]
