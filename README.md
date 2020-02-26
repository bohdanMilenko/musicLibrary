# Music Repository API

This application is designed for users to perform CRUD opertaions on music records. 

## Main features:
	- Architecture is built on Inversion of Control principles by implementing Service Pattern
	- Supports multiple databases integration for scalability purpose(MongoDB and SQLite3 modules availaibe)
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

Purpose: 

### Service Layer

Purpose: Receives requests from User, validates, applies business logic and passes request to Repositories Level

Features:

### Repository Layer

Purpose: 

Features:

	- Connections to databases are configured with .properties files
	- Using Prepared statements to prevent SQL injections
