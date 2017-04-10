# CeļoKopā.lv App #

### Installation dependencies ###

The following dependencies are necessary: 

 - Java 8
 - Node 0.12 or higher
 - bower
 - maven 3

### Installing frontend dependencies ###

After cloning the repository, the following command installs the Javascript dependencies:

    bower install

### Building and starting the server ###

To build the backend and start the server, run the following command on the root folder of the repository:

    mvn clean install tomcat7:run-war -Dspring.profiles.active=test

The spring test profile will activate an in-memory database. After the server starts, the application is accessible at the following URL:

    http://localhost:8080/

To see a user with existing data, login with the following credentials:

    username: test123 / password: Password2

### How to run the project in HTTPS-only mode ###

The application can be started in HTTPS only mode by using the flag httpsOnly=true.

    mvn clean install tomcat7:run-war -Dspring.profiles.active=test -DhttpsOnly=true

The project can be accessed via this URL:

    https://localhost:8443/
