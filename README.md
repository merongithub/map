Reference Service
================
Provider of reference-related services.

API Documentation
-----------------
The API documentation is generated by [Swagger UI](http://swagger.io/swagger-ui/) and can be found at:

* http://{host}:{port}/swagger-ui.html
* http://{host}:{port}/v2/api-docs

Configuration
-------------
The data source configuration, as well as service settings, can be found in the external config directory in `application.properties`.  These properties can be overridden per environment as they live outside of the service.

Running Application Locally
---------------------------

*Note: After building the application, there will be a `reference-service.jar` file and a `reference-service-exec.jar` file.  The former is created to allow this project to be used as a dependency in other projects. The latter is the executable Sprint Boot file.*

##### Method 1

Build and run application from the command line using the Spring Boot Maven Plugin:

```
mvn spring-boot:run
```

##### Method 2

1. Build the application from the command line with Maven: `mvn clean install`
2. Start the service: `java -jar target/reference-service-exec.jar`

##### Method 3

Use the `Run` command in your favorite IDE.
    
##### View API Documentation

Once the service is running, visit the Swagger documentation at `http://localhost:5013/swagger-ui.html`

*Note: By default, the service will start on port 8080.  To change this, modify the following property in the `application.properties` file:*

```
server.port=8080
```

### Example of Geometry Object 

{
  "name": "Victoria",
  "type": "Cafe",
  "geometry":{ "type": "Point", "coordinates": [-125.00, -90.00] }
}



### Swagger End point 
http://localhost:5009/swagger-ui.html#/