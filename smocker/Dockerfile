FROM thiht/smocker:0.18.5
COPY ./mocks.yaml /config/mocks.yaml
COPY ./docker-entrypoint.sh /docker-entrypoint.sh
ENTRYPOINT ["/bin/sh", "/docker-entrypoint.sh"]
