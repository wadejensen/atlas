#!/usr/bin/env bash

# Script to hot reload a Node.js server running inside a
# Docker image when the source code is re-compiled.
# Stops any pre-existing container with the same image name,
# and launches a freshly built image.

if [ $# -ne 2 ]; then
  echo <<<USAGE

Usage: nodemon --exec "./hot_reload.sh <image_name> <port>"

USAGE
fi

function main() {
  local image_name=$1
  local port=$2

  container_id="$(docker ps    \
      --quiet    \
      --filter="ancestor=${image_name}"    \
      --format="{{.ID}}")"

  echo "Shutting down container id: ${container_id}"
  docker stop -t 1 "${container_id}"
  docker build -t "${image_name}" .
  docker run -p "${port}:${port}" "${image_name}" &
}

main "$@"
