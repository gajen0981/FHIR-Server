
# FHIR-Server
=============
This is the HAPI based FHIR server with Blue Button 2.0 capabilities that is fine tuned for enterprise scalability with RDBMS on a JBoss Application Server. This technology stack is JavaEE with SpringMVC. 

Documentation is coming...

WHY
---
Healthcare Interoperability using HL7's FHIR standards via HTTPS transport (RESTful API)

WHAT
----
HAPI based FHIR server with Blue Button 2.0 capabilities. 

### Examples of RESTful Open API's
![alt tag](https://github.com/gajen0981/CHPL-OpenAPI/blob/master/docs/1.png)


DIRECTORY STRUCTURE
-------------------
      hapi-fhir-jpaserver-uhnfhirtest/             client application


REQUIREMENTS
------------
- Java 1.7.x
- JBoss EAP 6.3.x
- Any RDBMS (MySQL, Oracle, PostgreSQL etc...) 


INSTALLATION
------------
Check it out from GitHub and compile using Maven package dependency manager. 
```java
config/chpl.sql
```

Application can be access via the following URI
http://localhost/basic/web/


CONFIGURATION
-------------

### Database

Edit the file `config/db.java` with real database configurations, for example:

```java
return [
    'class' => 'yii\db\Connection',
    'dsn' => 'mysql:host=localhost;dbname=chpl',
    'username' => 'root',
    'password' => '1234',
    'charset' => 'utf8',
];
```

Based on a HAPI Server
----------------------
Big thanks to HAPI Server and folks from UHN especially James. 


