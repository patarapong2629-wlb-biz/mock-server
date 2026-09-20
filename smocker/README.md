# Smocker

Official document: [smocker.dev](https://smocker.dev/)

Smocker is an open source, standalone mock server for HTTP APIs. Like
Mountebank and WireMock, you point your application under test at Smocker
instead of the real dependency, but Smocker also ships a built-in web UI for
inspecting matched/unmatched requests and swapping between mock sets.

## How it works

Unlike Mountebank/WireMock, the Smocker binary itself has no flag to load mock
files from disk at startup — mocks can only be registered through its admin
HTTP API. `docker-entrypoint.sh` starts Smocker in the background, waits for
its admin server to come up, then `POST`s `mocks.yaml` to `/mocks` before
handing control back to the Smocker process.

Each entry in `mocks.yaml` pairs a `request` matcher with either a static
`response` or a `dynamic_response` (a Go template, rendered with
[Sprig](http://masterminds.github.io/sprig/) helper functions, that can branch
on the incoming request).

**Match order gotcha:** Smocker evaluates mocks top to bottom, but a batch
`POST /mocks` registers them in *reverse* of the order they appear in the
file (last entry in the file becomes the first/highest-priority match). To
make the catch-all act as a default, it's declared **first** in
`mocks.yaml` so it ends up matched **last**. A path matcher also needs the
explicit `{matcher: ShouldMatch, value: "..."}` form for a regex — a plain
string is matched with `ShouldEqual` (exact match).

## Glossary

_mock_

    A YAML entry pairing a `request` matcher with a `response`.

_request matcher_

    Criteria (method, path, headers, body, etc.) Smocker uses to decide which
    mock should handle an incoming request.

_static response_

    A fixed status/headers/body returned when a mock matches. Supports a
    fixed `delay` before responding.

_dynamic response_

    A Go template (`go_template_yaml` engine) that renders the
    status/headers/body at request time, allowing conditional logic and
    Sprig helpers (e.g. `randInt`, `get`) against the already-parsed JSON
    request body at `.Request.Body`.

_history_

    The log of every request Smocker has received and which mock (if any)
    matched it, viewable from the admin UI.

## Admin UI

The Smocker admin UI is running on port 8885. Use it to inspect request
history or reset/reload mocks without rebuilding the container.

## API in This Mock Server

The Smocker mock server is running on port 8884.

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
