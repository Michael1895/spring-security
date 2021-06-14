#!/bin/bash
while ! nc -z ${DB_HOST:-db} ${DB_PORT:-5432}; do sleep 5; done
