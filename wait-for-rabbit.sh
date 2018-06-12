#!/bin/bash

until $(curl --output /dev/null --silent --head --fail http://192.168.99.100:15672); do
  echo 'Waiting for rabbitmq...'
  sleep 5
done

echo 'rabbitmq is now up'