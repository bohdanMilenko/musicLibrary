# Music Library

Welcome to the app that give you an opportunity to perform various queries to the database with information about artists, their songs and albums

## DB Structure

### Artists:
	- id - Integer, key
	- name - String, the name of the artist

### Songs:
	- id - Integer, key
	- track - Integer, the position of the track in the album
	- title - String, the title of the song
	- album - Integer, the id of the album this song belongs to

### Albums:
	- id - Integer, key
	- name - String, the name of the album
	- artist - Integer, the id of the artist this album belongs to

## Main features:
	- Insert new songs ( if song with such artist and album already exists, nothing is commited)
	- In case of SQLException .rollback is performed
	- Path to database is extracted from .properties file. Default path is hardcoded in case of file is not availaible
	- Using Prepared statements to prevent SQL injections
	- Only one connection is active per query. Each new query creates a new connection and closes when it is performed