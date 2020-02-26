# Music Repository API

This application is designed for users to perform CRUD opertaions on music records. 

## Main features:
	- Architecture is built on Inversion of Control principles by implementing Service Pattern
	- Supports multiple databases integration for scalability and portability purposes 

	(MongoDB and SQLite3 modules availaibe)
	- Custom Exceptions module supports the progrm workflow
	- All Service implementations are covered with integration tests (JUnit and Mockito)
	- Maven for managing dependencies
	- Docker Image with application inside availaible at DockerHub 

[Please take me to DockerHub!](https://hub.docker.com/)
	


## High Level Architecture

<p align="center">
   <img src ="readMeSource/High Level Architecture Music Repo.png" width="600">
</p>

<p align="center">
   <i>"High level overview of the architecture"</i>
</p>



## Modules Overview

This section will guide you module by module following the application flow. Fasten your seat bealts :)

### Entities

**Purpose:** Facilitates all layers of the application - serves as coherent form for efficient data transmition from user to DB and visa versa.

**Features:**

- Each class follows composition design
- This enforces cross class realtions between three of them
- Lazy initialization of entities when retrieved from DB

<p align="center">
   <img src ="readMeSource/Entities.png" width="600">
</p>


### Service Layer

**Purpose:** Receives requests from User, validates, applies business logic and passes request to Repositories Level.

**Features:**

	- Each Service requires instance of the corresponding Repository and neighbouring Service (i.e. SongService requires instance of AlbumService)
	- Data  fetching is based on Lazy Initialization of entities.
	- Validation logic is outsorced to RecordValidation class that does various checks utilizing all three Services
	- Performs Cascade deletion of data (i.e. If Artist is deleted -> validates if DB has Albums for this Artist -> if Yes deletes Albums and Songs)
	- Require entities to be passed into methods

### Repository Layer

**Purpose:**

**Features:**

	- Connections to databases are configured with .properties files
	- Using Prepared statements to prevent SQL injections
