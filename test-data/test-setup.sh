#!/bin/bash

curl -X POST -H "Content-Type: application/json" -d @metadata.json http://localhost:8080/metadata

curl -X POST -H "Content-Type: application/json" -d @player.json http://localhost:8080/players

curl -X POST -H "Content-Type: application/json" -d @pet-dog.json http://localhost:8080/pets

curl -X POST -H "Content-Type: application/json" -d @pet-cat.json http://localhost:8080/pets

