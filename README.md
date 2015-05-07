
# FHIR-Server
=============
This is the HAPI based FHIR server with Blue Button 2.0 capabilities that is fine tuned for enterprise scalability with RDBMS on a JBoss Application Server. This technology stack is JavaEE with SpringMVC. 

Documentation is coming...

WHY
---
Healthcare Interoperability using HL7's FHIR standards over HTTPS transport (RESTful API).

WHAT
----
HAPI based FHIR server with Blue Button 2.0 capabilities. Able to download the files in FHIR JSON or FHIR XML for untethered PHR.

### Blue Button 2.0 capabilities
![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/rdbms.png)

DIRECTORY STRUCTURE
-------------------
      hapi-fhir-base/                              Base code
      hapi-fhir-jpaserver-uhnfhirtest/             client application
      hapi-fhir-structures-dstu*/                  HL7's FHIR specs in Draft Standard Trial Use (DSTU)
      hapi-tinder-plugin/                          HL7's FHIR specs raw schema

REQUIREMENTS
------------
- Java 1.7.x
- JBoss EAP 6.3.x
- Any RDBMS (MySQL, Oracle, PostgreSQL etc...) 

INSTALLATION
------------
Check it out from GitHub and compile using Maven package dependency manager. 

### Modify the Maven POM file to include RDBMS connector library
Include the MySQL (or any RDBMS) 
      hapi-fhir-jpaserver-uhnfhirtest->pom.xml
      
![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/rdbms.png)

### Database Configuration
DB Connection configuration with DB credentials and create a database name "fhir".
      hapi-fhir-jpaserver-uhnfhirtest -> src -> main -> webapp -> WEB-INF -> hapi-fhir-tester-config.xml
      
![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/dbConnectionConfig.png)

Configuration XML

```xml
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:mvc="http://www.springframework.org/schema/mvc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xmlns:tx="http://www.springframework.org/schema/tx" xmlns:context="http://www.springframework.org/schema/context" xmlns:security="http://www.springframework.org/schema/security"
	   xmlns:oauth="http://www.springframework.org/schema/security/oauth2"
	   xsi:schemaLocation="http://www.springframework.org/schema/security/oauth2 http://www.springframework.org/schema/security/spring-security-oauth2-2.0.xsd
http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc-3.2.xsd
http://www.springframework.org/schema/security http://www.springframework.org/schema/security/spring-security-3.1.xsd
http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.2.xsd
http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.2.xsd
http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.2.xsd">

	<!--JDBC Datasource from a RDBMS systems-->
	<bean id="myPersistenceDataSource" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close">
		<property name="driverClassName" value="com.mysql.jdbc.Driver"/>
		<property name="url" value="jdbc:mysql://localhost:3306/fhir"/>
		<property name="username" value="root"/>
		<property name="password" value="root"/>
		<property name="validationQuery" value="SELECT 1"/>
	</bean>

	<!--<bean depends-on="dbServer" id="myEntityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">-->
	<bean id="myEntityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
		<property name="dataSource" ref="myPersistenceDataSource" />
		<property name="persistenceXmlLocation" value="classpath:META-INF/fhirtest_persistence.xml" />
		<property name="persistenceUnitName" value="FHIR_UT" />
		<property name="jpaVendorAdapter">
			<bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
				<property name="showSql" value="false" />
				<property name="generateDdl" value="true" />
				<property name="databasePlatform" value="org.hibernate.dialect.MySQL5Dialect" />
			</bean>
		</property>
	</bean>

</beans>
```

### Modify SpringMVC persistence configuration
Include the MySQL (or any RDBMS) 
      hapi-fhir-jpaserver-uhnfhirtest -> src -> main -> resources -> META-INF -> fhirtest-persistence.xml
      
![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/persistence.png)

```xml
<property name="hibernate.dialect" value="org.hibernate.dialect.MySQL5Dialect" />
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


