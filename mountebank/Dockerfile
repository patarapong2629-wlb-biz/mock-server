FROM bbyars/mountebank:2.9.4
COPY ./imposters.json /imposters/imposters.json
COPY ./response.js /imposters/response.js
CMD [ "start", "--configfile", "/imposters/imposters.json", "--allowInjection" ]