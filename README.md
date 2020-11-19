# API_gateway

## Basic structure:
https://lucid.app/lucidchart/7bafe814-7ae0-4960-b480-45c0862a62a8/edit?page=0_0#?folder_id=home&browser=icon

## Authentication process:
https://lucid.app/lucidchart/a888a08a-0a60-4b4f-b617-22683891bbc3/edit?page=0_0#?folder_id=home&browser=icon

## Activite mock servers:
docker-compose up

3 mock servers will be started: 
 - a mock authentication service at localhost: 9000
 - a mock product api service at localhost: 8080
 - a mock user api service at localhost: 8081

These mock services are defined in the ./mock servers folder. The authentication service would check wether the request's api key(stored in header) is valid, based on a fixed list of valid api keys. The other 2 would verify the request's JWT(stored in header) and return a simple message. 

## Develope
After activiting the mock services, you can run the main application(a spring boot application) to have a test.

## Todo list
 - global error filter
 - rate limiting feature
 - put gateway app into a docker container