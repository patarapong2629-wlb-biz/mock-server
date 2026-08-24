function response(request) {
  const body = JSON.parse(request.body);
  const mapper = {
    1: "KR",
    2: "TH",
    3: "LM",
  };

  const num = Math.floor(Math.random() * 1000000000);

  if (!mapper[body.shipping_method_id]) {
    return {
      statusCode: 404,
      headers: {
        "Content-Type": "application/json; charset=utf-8",
      },
      body: {
        error: "Shipping method not found",
      },
    };
  }

  return {
    statusCode: 200,
    headers: {
      "Content-Type": "application/json; charset=utf-8",
    },
    body: {
      tracking_number: `${mapper[body.shipping_method_id]}-${num}`,
    },
  };
}
