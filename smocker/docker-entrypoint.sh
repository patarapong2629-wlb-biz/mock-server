#!/bin/sh
set -e

/opt/smocker &
SMOCKER_PID=$!

until wget -q -O /dev/null "http://localhost:8081/version"; do
  sleep 0.2
done

wget -q -O - \
  --header="Content-Type: application/x-yaml" \
  --post-file=/config/mocks.yaml \
  "http://localhost:8081/mocks"

wait "$SMOCKER_PID"
