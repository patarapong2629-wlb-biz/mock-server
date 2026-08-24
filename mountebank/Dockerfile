FROM bbyars/mountebank:2.9.4
COPY ./imposters.json /imposters/imposters.json
CMD [ "start", "--configfile", "/imposters/imposters.json", "--allowInjection" ]