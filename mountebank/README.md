# Mountebank

Official document: [mbtest.dev](https://www.mbtest.dev/)

Mountebank is the first open source tool to provide cross-platform, multi-protocol test doubles over the wire. Simply point your application under test to mountebank instead of the real dependency, and test like you would with traditional stubs and mocks.

## How it works

Mountebank employs a legion of imposters to act as on-demand test doubles. Your test communicates to mountebank over http using the api to set up stubs, record and replay proxies, and verify mock expectations. In the typical use case, each test will start an imposter during test setup and stop an imposter during test teardown, although you are also welcome to configure mountebank at startup using a config file.

Mountebank employs several types of imposters, each responding to a specific protocol. Typically, your test will tell the imposter which port to bind to, and the imposter will open the corresponding socket.

## Glossary

_imposter_

    A server representing a test double. An imposter is identified by a port and a protocol. mountebank is non-modal and can create as many imposters as your test requires.

_stub_

    A set of configuration used to generate a response for an imposter. An imposter can have 0 or more stubs, each of which are associated with different predicates.

_predicate_

    A condition that determines whether a given stub is responsible for responding. Each stub can have 0 or more predicates.

_response_

    The configuration that generates the response for a stub. Each stub can have 0 or more responses.

_response type_

    Defines the specific type of configuration used to generate a response. The simplest type is called is, and allows you to define the imposter's response directly. mountebank also supports a proxy response type, which allows record-replay behavior, and an inject response type, which allows you to script mountebank responses. Each response has exactly one response type.

_stub behavior_

    Adds additional post-processing to a response, for example by adding latency to the response or augmenting the response with more information. A response can have zero or more behaviors, which represent a pipeline of such transformations.

## API in This Mock Server

The mountebank mock server is running on port 8882.

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
