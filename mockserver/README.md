# MockServer

Official document: [mock-server.com](https://www.mock-server.com/)

MockServer is an open source, standalone mock server for HTTP APIs, closest
in spirit to WireMock: expectations are JSON, loaded from a file at startup,
and dynamic responses are written as scripts rather than a stub DSL.

## How it works

MockServer loads expectations from the file at `MOCKSERVER_INITIALIZATION_JSON_PATH`
when the container starts. Each expectation pairs an `httpRequest` matcher
with either a static `httpResponse` or an `httpResponseTemplate` — a script
(JavaScript here, Velocity and Mustache are also supported) that computes the
status/headers/body per request. Requests with no matching expectation get a
default `404`, so no catch-all entry is needed.

**Gotcha:** the base `mockserver/mockserver` image does **not** bundle a
JavaScript engine — a JS `httpResponseTemplate` fails at request time with
`JavaScript response templates require the GraalJS engine, which is not on
the classpath`. This Dockerfile uses the `-graaljs` image variant instead.

**Gotcha:** an `httpResponseTemplate` script is the *body* of an implicit
`function handle(request) { ... }` — write bare statements ending in
`return {...}`, not a full `function handle(request) { ... }` declaration
(wrapping it again nests a second, uncalled function and the response comes
back empty). `request.body` is the raw JSON **string**, not a parsed
object — call `JSON.parse(request.body)` yourself.

## Glossary

_expectation_

    A JSON entry pairing an `httpRequest` matcher with a response action.

_httpRequest_

    Criteria (method, path, headers, body, etc.) MockServer uses to decide
    which expectation should handle an incoming request.

_httpResponse_

    A fixed status/headers/body, optionally with a `delay`.

_httpResponseTemplate_

    A script (`templateType: JAVASCRIPT`, `VELOCITY`, or `MUSTACHE`) that
    computes the response per request, enabling conditional logic equivalent
    to Mountebank's `inject` or WireMock's Java extensions.

## API in This Mock Server

The MockServer mock server is running on port 8887. Its own admin/dashboard
UI (for inspecting matched requests, retrieving/clearing expectations, etc.)
is on the same port, under `/mockserver/*`.

### Payment Visa

API Endpoint: /payment/visa

Method: POST

Response Status: 200

Response Body:

```json
{
  "status": "success",
  "fee": 0.0,
  "available_balance": 2401.98,
  "authorized": "${CURRENT_UTC_DATE_TIME}",
  "transaction_id": "TOY202002021525"
}
```

### Shipping

API Endpoint: /shipping

Method: POST

Response Status: 200

Response Body:

```json
{
  "tracking_number": "${SHIPPING_METHOD}-${RANDOM-NUMBER}"
}
```

Returns 404 with `{"error": "Shipping method not found"}` when
`shipping_method_id` isn't `1`, `2`, or `3`.
