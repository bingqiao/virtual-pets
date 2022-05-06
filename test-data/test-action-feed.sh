#!/bin/bash

curl -X PUT -H "Content-Type: application/json" -d @pet.json http://localhost:8080/pets/1/action/FEED
