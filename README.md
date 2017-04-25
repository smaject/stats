# Status of JavaEE 7 Application Servers
Stats exposes basic health / jvm / jndi and OS information for JEE 7 Application Servers.

Tested with GlassFish, Payara, WLP and Wildfly

## Installation
Just drop the WAR from [Releases](https://github.com/smaject/stats/releases/) into your autodeploy folder and access the application with `http://localhost:[YOUR_PORT]/stats`

All information is exposed via simple REST-Resources, so you could use it in your own application.

## Docker
If you are interested in using `stats` together with docker refer to our [Docker](https://github.com/smaject/docker) for examples.