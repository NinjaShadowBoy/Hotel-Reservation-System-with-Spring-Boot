#!/bin/bash
# Start stripe listener in background
stripe listen --forward-to localhost:8080/api/stripe/webhook &
