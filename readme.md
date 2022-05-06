# VPETS

### Build and run
JDK 17 is required to build this project. Building locally is however not required to run the app. See 
next section [Run via Docker](#run-via-docker).

On macOS
```
./gradlew build
```
On Windows
```
gradlew.bat build
```
Then
```
java -DDB_HOST=localhost -jar build/libs/vpets-0.0.1-SNAPSHOT.jar
```
### Run via Docker
There are two docker-compose configs. The default `docker-compose.yaml` runs this project in a docker container  
as well as a mongoDB container.
```
docker-compose up --build
```
This will start the app on `http://localhost:8080` and mongoDB on `mongodb://localhost:27017`.

The login credentials for mongodb:
* username: vpetuser
* password: letmein

The other docker-compose config `docker-compose-db.yaml` runs only a mongoDB instance which is useful when  
the app is launched from IDE or command line.
```
docker-compose -f docker-compose-db.yaml up
```
### Test data
There are shell scripts in folder `test-data` that contain `curl` requests for setting up test data and making  
restful requests relevant to the requirements of this project.
* `test-setup.sh.sh`, run this to set up `metadata`, a single `player` and two `pets`;
* `test-action-feed.sh`, run this to feed `pet` of id `1`;
* `test-action-feed.sh`, run this to feed `pet` of id `1`;
* `test-action-stroke.sh`, run this to stroke `ped` of id `1`;
* `test-get-player-pets.sh`, run this to get all pets for `player` of id `1`;

To reset mongoDB, shutdown docker container by:
```
docker-compose down
```
Then remove the contents of folder `mongodb/data/`.

Then restart docker containers via docker-compose.

### Design
* Metadata driven and NoSQL DB
  - Player collection. A `user` defines user id and name
  - Pet collection. A `pet` defines its id, player, type, name and property values  
  and a timestamp of lastActionPlayed
  - Metadata collection. A `metadata` defines pet type which in turn defines pet  
  properties and actions targeting properties 
* HTTP API implemented in Spring WebFlux for Reactive and Non-blocking paradigm
* Embedded mongoDB used for repository testing; Mock objects for endpoint and  
service testing

### Requirements
An HTTP API that will be called by a game client.
#### Players with zero or more animals
`Player` entity has a `foreign key` relationship with `Pet` entity. The number of  
pets owned by a player is determined by how many pet entries present. A player can  
have zero or more animals.
To get all pets of a user:
```
curl -X GET -H "Content-Type: application/json" http://localhost:8080/players/{PLAYER_ID}/pets
```
#### Stroking animal makes them happy
A stroke `action` is defined in the test medatadata:
```
{
  "code": "STROKE",
  "target": "HAPPINESS",
  "effect": 20
}
```
* Field `code` uniquely defines an action.
* Field `target` identifies which pet property this action is applicable to.
* Field `effect` defines the delta applied to target property. This field is currently integer (+/-) only  
but can be expanded to support other data types such as boolean or enum.

In the example stroke action above, it will increase property `HAPPINESS` by 20.

To stroke a pet:
```
curl -X PUT -H "Content-Type: application/json" -d @pet.json http://localhost:8080/pets/{PET_ID}/action/STROKE
```
#### Feeding animal makes them less hungry
A feed `action` is defined in the test medatadata:
```
{
  "code": "FEED",
  "target": "HUNGER",
  "effect": -10
}
```
In the example feed action above, it will decrease property `HUNGER` by 10.

To feed a pet:
```
curl -X PUT -H "Content-Type: application/json" -d @pet.json http://localhost:8080/pets/{PET_ID}/action/FEED
```
#### Animals start with "neutral" happiness and hunger
When a new pet is created, it's properties are set to the half of their range.
To add a new pet:
```
curl -X POST -H "Content-Type: application/json" -d @pet-dog.json http://localhost:8080/pets
```
Where pet-dog.json contains:
```
{
  "id": "2",
  "playerId": "1",
  "type": {
    "code": "DOG"
  },
  "name": "wowo"
}
```
Notice that no property values (HAPPINESS or HUNGER) are provided. They are ignored if provided.

The newly created `pet` entity will have properties:
```
{
  "HAPPINESS": "50",
  "HUNGER": "50"
}
```
The values are half of their property ranges as defined in metadata:
```
{
  "HAPPINESS": {
      "code": "HAPPINESS",
...
      "max": 100,
      "min": 0,
...
  },
  "HUNGER": {
      "code": "HUNGER",
...
      "max": 100,
      "min": 0,
...
  }
}
```

#### Happiness & hunger change over time
If a pet type has non-zero (+/-) `rate` defined in metadata, then it's a time-based property and its  
value is adjusted by time. Pet rate is also pet type specific. The `rateUnit` field is used to define  
time unit.
```
{
  "HAPPINESS": {
    "code": "HAPPINESS",
...
    "rate": -2,
    "rateUnit": "minute"
  },
  "HUNGER": {
    "code": "HUNGER",
...
    "rate": 5,
    "rateUnit": "minute"
  }
}
```
A `lastActionPlayed` timestamp for `Pet` entity is set every time an action is applied to any of the  
time-based properties. Those time-based properties are calculated for `GET` requests to be adjusted  
based on `rate`, `rateUnit` and `lastActionPlayed`.

To get a pet (notice property values change over time):
```
curl -X GET -H "Content-Type: application/json" http://localhost:8080/pets/{PET_ID}
```

#### Different types of animal
Pet types are defined by metadata so can be managed without code change.

To get metadata:
```
curl -X GET -H "Content-Type: application/json" http://localhost:8080/metadata
```

#### Hunger & happiness change rate vary
Time-based properties have different change type defined on property level.

#### Prototype which will be expanded upon in the future
The design of this prototype has future extension in mind.
* Metadata driven so that code change is minimized when new pet types/actions are added.
* API endpoints are grouped largely based on data. This allows breaking up this app into multiple  
microservices if needed.

### Further improvement
* Test coverage. Tests are written only for selected classes for this prototype but cover core logic  
of time-based properties, service, endpoint and repository.
* The way that time-based property change is implemented means that game downtime will have an impact  
on players who could not have played but would still have their pets getting unhappier and hungrier.  
One way to handle this is to have a registry for maintenance events so that downtime can be taken into  
account when pets properties are retrieved (GET).
* Metadata should be versioned. So are pet data. This is not handled currently.



