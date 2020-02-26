# Music Repository API

This application is designed for users to perform CRUD opertaions on music records. 

## Main features:
	- Architecture is built on Inversion of Control principles by implementing Service Pattern
	- Supports multiple databases integration for scalability purpose(currently MongoDB and SQLite3 modules availaibe)
	- Custom Exceptions module supports the progrm workflow
	- All Service implementations are covered with integration tests (JUnit and Mockito)
	- Maven for managing dependencies
	- Docker Image with application inside: [_Take me to docker hub!_](https://hub.docker.com/)
	- Connections to databases are configured with .properties files
	- Using Prepared statements to prevent SQL injections
	- 

## High Level Architecture

<p align="center">
   <img src ="readMeSource/High Level Architecture Music Repo.png" width="700">
</p>

Receives requests from User, validates,
 applies business logic and passes
 request to Repositories Level

Receives requests from Service Layer, 
retrieves data from DB, maps it to entities
and returns back to Service Layer


