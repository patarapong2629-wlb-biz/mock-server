# WireMock

Official document: [wiremock.org](https://wiremock.org/)

WireMock is a tool for building mock APIs. It lets you stay productive when an
API you depend on doesn't exist or isn't complete, and it lets you test edge
cases and failure modes that the real API won't reliably produce.

## How it works

WireMock loads stub mappings from the `mappings/` directory at startup. Each
file describes a `request` matcher and a `response` to return when that
request is matched. Mappings are matched in ascending `priority` order (lower
number = higher priority), so a catch-all mapping is given a low priority to
act as the default response for anything else.

## Glossary

_mapping (stub)_

    A JSON file under `mappings/` pairing a request matcher with a response.

_request matcher_

    Criteria (method, URL, headers, body, etc.) WireMock uses to decide which
    mapping should handle an incoming request.

_response_

    The status, headers, and body WireMock returns when a mapping matches.
    Supports fixed delays (`fixedDelayMilliseconds`) and response templating
    (Handlebars) for dynamic values like timestamps.

_response templating_

    An extension (enabled globally here via `--global-response-templating`)
    that lets response bodies contain Handlebars expressions, e.g. `{{now}}`,
    which are evaluated per-request.

## API in This Mock Server

The mountebank mock server is running on port 8883.

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
