# Node-RED

Official document: [nodered.org](https://nodered.org/)

Node-RED is a flow-based, low-code programming tool built on Node.js. It
isn't purpose-built as a mock server (it's a general automation/IoT
platform), but its `http in` / `function` / `http response` nodes give it
full programmable request/response logic — closer to Mountebank's `inject`
or MockServer's JS templates than to a static-stub tool like WireMock.

## How it works

Node-RED loads `flows.json` from its data directory (`/data`) at startup and
immediately deploys it. Each mock endpoint here is three wired nodes:

- `http in` — declares the method + path Node-RED should listen on
- `function` — a plain JavaScript function that inspects `msg.payload` (the
  parsed request body) and sets `msg.statusCode` / `msg.headers` /
  `msg.payload` for the response
- `http response` — sends whatever the function node produced

Unlike every other mock server in this repo, the editor UI and the deployed
mock endpoints share the same port (1880 → 8889) — there's no separate
admin port. Visiting `http://localhost:8889/` opens the Node-RED flow editor
itself, where you can inspect or modify `Mock APIs` tab live (changes made
there aren't persisted back into this repo's `flows.json` unless you export
them again, since the container has no volume mounted to `/data`).

Requests to a path with no matching `http in` node get Node-RED's default
Express 404, so no catch-all node is needed.

## Glossary

_flow_

    A set of wired nodes forming a pipeline. `flows.json` is Node-RED's
    on-disk serialization of every tab/flow in the editor.

_http in_

    A node that opens an HTTP route (method + path) as the entry point of a
    flow, equivalent to a Mountebank `stub`'s `predicates` or a WireMock
    mapping's `request` matcher.

_function_

    A node holding arbitrary JavaScript, run once per message. This is where
    conditional logic, random values, and dynamic timestamps live.

_msg_

    The object passed between nodes. For an HTTP-triggered flow, `msg.payload`
    starts as the parsed request body; setting `msg.statusCode`, `msg.headers`,
    and `msg.payload` before the `http response` node controls the reply.

## API in This Mock Server

The Node-RED mock server (and its flow editor) is running on port 8889.

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
