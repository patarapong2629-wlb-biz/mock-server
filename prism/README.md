# Prism

Official document: [meta.stoplight.io/docs/prism](https://meta.stoplight.io/docs/prism/)

Prism is an open source mock server built by Stoplight that mocks an API
directly from its OpenAPI document — there's no imposter/stub DSL to write;
the contract itself is the mock.

## How it works

Prism reads `openapi.yaml` at startup and, for every operation, returns the
first response example defined for its schema. If a request body doesn't
match the schema (wrong type, value outside an `enum`, missing required
field), Prism rejects it with a `422` **before** it ever reaches a handler —
this is request validation baked into the mock, not something you configure
per stub.

**This is a real capability gap vs. Mountebank/WireMock/Smocker:** Prism has
no per-request branching logic. All valid requests to an operation get the
same static example; there's no equivalent of "route `shipping_method_id: 1`
to one response and `99` to a 404 with a custom error body." The closest
Prism gets is picking a different named example via a `Prefer: example=<name>`
request header — a client opt-in, not something driven by the payload.

**Gotcha:** the base image defaults to `--multiprocess`, which uses Node's
`cluster` module and crashes (`Cannot read properties of undefined (reading
'isPrimary')`) when the `linux/amd64` image runs emulated on an arm64 host
(e.g. Apple Silicon without a native arm64 build). The Dockerfile disables it
with `--multiprocess=false`.

## Glossary

_document_

    The OpenAPI (or Postman) file describing every operation Prism mocks.

_example_

    A sample value attached to a schema/response. Prism returns it as-is for
    matching requests (static mode) unless `-d/--dynamic` is passed, in which
    case Prism ignores examples and fakes data from the schema's types
    instead.

_validation_

    Prism validates incoming requests (and can validate its own responses)
    against the OpenAPI schema, returning `422` for anything that doesn't
    conform.

## API in This Mock Server

The Prism mock server is running on port 8886.

### Payment Visa

API Endpoint: /payment/visa

Method: POST

Response Status: 200

Response Body:

```json
{
  "status": "success",
  "fee": 0,
  "available_balance": 2401.98,
  "authorized": "2019-08-24T14:15:22Z",
  "transaction_id": "TOY202002021525"
}
```

`authorized` is a static example value — unlike the other mock servers in
this repo, it does not update per request (see the capability gap above).

### Shipping

API Endpoint: /shipping

Method: POST

Response Status: 200

Response Body:

```json
{
  "tracking_number": "KR-123456789"
}
```

Every valid `shipping_method_id` (`1`, `2`, or `3`) returns this same static
example. Any other value fails OpenAPI validation and returns `422` with a
Prism-generated error body — not the `404`/`"Shipping method not found"`
response the other mock servers return for this case.
