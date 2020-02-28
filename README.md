# Music Repository API

This application is designed for users to perform CRUD opertaions on music records. You can do CRUD operations on Artists, Albums and Songs.

## Main features:
- Architecture is built on Inversion of Control principles by implementing Service Pattern
- Supports multiple databases integration for scalability and portability purposes 
(MongoDB and SQLite3 modules availaibe)
- Custom Exceptions module supports the program workflow
- All Service implementations are covered with integration tests (JUnit and Mockito)
- Maven for managing dependencies
- Docker Image with application inside availaible at DockerHub 

*[Take me to DockerHub!](https://hub.docker.com/)*
	

## High Level Architecture

<p align="center">
   <img src ="readMeSource/High Level Architecture Music Repo.png" width="600">
</p>

<p align="center">
   <i>"High level overview of the architecture"</i>
</p>



## Modules Overview

This section will guide you module by module following the application flow. Fasten your seat belts :)

### Entities

**Purpose:** Facilitates all layers of the application - serves as a coherent form of efficient data transmission from user to DB and visa versa.

**Features:**

- Each class follows composition design
- This enforces cross-class relations between three of them
- Lazy initialization of entities when retrieved from DB

<p align="center">
   <img src ="readMeSource/Entities.png" width="600">
</p>


### Service Layer

**Purpose:** Receives requests from User, validates, applies business logic and passes the request to Repositories Level.

**Features:**

- Each Service requires an instance of the corresponding Repository and neighboring Service (i.e. SongService requires instance of AlbumService)
- Data fetching is based on Lazy Initialization of entities.
- Validation logic is outsourced to RecordValidation class that does various checks utilizing all three Services
- Performs Cascade deletion of data (i.e. If Artist is deleted -> validates if DB has Albums for this Artist -> if Yes deletes Albums and Songs)
- Require entities to be passed into methods

<p align="center">
   <img src ="readMeSource/ServiceLayerRecent.png" width="600">
</p>


### Repository Layer

**Purpose:** Serves as a tool for Service layer to write/read/delete to DBs.

**Features:**

- Each DB module has Util class that helps them to establish and manage pool of connections
- Connections to databases are configured with .properties files
- Repositories for each DB implement interfaces, to ensure each new module added would have identical structure and functionality
- Using Prepared statements to prevent SQL injections
