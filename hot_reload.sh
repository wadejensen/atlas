#!/bin/bash

if [ $# -ne 2 ]; then
  echo <<<USAGE

Usage: nodemon --exec "./hot_reload.sh <image_name> <port>"

USAGE
fi

image_name=$1
port=$2

container_id=$(docker ps    \
    --quiet    \
    --filter="ancestor=${image_name}"    \
    --format="{{.ID}}")

echo "Shutting down container id: ${container_id}"
docker stop -t 1 "${container_id}"

docker build -t "${image_name}" .
docker run -d -p "${port}:${port}" "${image_name}"
