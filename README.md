
# FHIR-Server
=============
This is the HAPI based FHIR server with Blue Button 2.0 capabilities that is tuned for enterprise scalability with RDBMS with JBoss Application Server. This technology stack on JavaEE with SpringMVC. 

Documentation is still in progress and it's in DRAFT format.

WHY
---
Healthcare Interoperability using HL7's FHIR standards over HTTPS transport (RESTful API).


WHAT
----
HAPI based FHIR server with Blue Button 2.0 capabilities. Able to download the files in FHIR JSON or FHIR XML for untethered PHR. Imagine this Blue Button functionality is implemented in every patient portal to free the data to patient and have them empower and take control of their health. Also provides dynamic API access to patient health info for 3rd party applications. 

### Blue Button 2.0 capabilities => Download everything for a patient as a FHIR Bundle
Download the healthcare data as FHIR JSON or FHIR XML format. 

![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/BB2Download.png)

### Blue Button 2.0 capabilities
Untethered PHR and patient download and share the data with heatlhcare providers. 

![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/BB2DownloadedFiles.png)

### Example Personal Healh Record Use Case
Untethered Personal Health Repository - Patient imports FHIR files from multiple health systems.
Rich Health Meaningful Info - With visually appealing mobile app, patients able to take control of their health and empower it. 
Share and Proxy Access - Share the data with other heatlhcare providers and give and authorize 3rd party applications to get access to their data. 
Donate for Research & Science - Donate the data for research & science (or Precision Medicine Initiates).

End point app is in the early stages of developments. 
![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/PHR.png)

DIRECTORY STRUCTURE
-------------------
      hapi-fhir-base/                              Base code
      hapi-fhir-jpaserver-uhnfhirtest/             client application
      hapi-fhir-structures-dstu*/                  HL7's FHIR specs in Draft Standard Trial Use (DSTU)
      hapi-tinder-plugin/                          HL7's FHIR specs raw schema


REQUIREMENTS
------------
- Java 1.7.x
- SpringMVC Framework 4.1.x
- JBoss EAP 6.3.x
- Any RDBMS (MySQL, Oracle, PostgreSQL etc...) 


CONFIGURATION
-------------
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
To choose the database type and version dialect.

      hapi-fhir-jpaserver-uhnfhirtest -> src -> main -> resources -> META-INF -> fhirtest-persistence.xml
      
![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/persistence.png)

Configuration XML
```xml
<property name="hibernate.dialect" value="org.hibernate.dialect.MySQL5Dialect" />
```

### Add JBoss application server system properties for the local server base URL
Important if you want to connect the FHIR server to its local DB 

      JBoss EAP -> standalone -> configuration -> standalone.xml
      
![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/jbossConfig.png)

Configuration XML
```xml
<system-properties> 
        <!-- Local FHIR Server Base URL -->
        <property name="fhir.baseurl.dstu1" value="http://localhost:8080/baseDstu1"/>
        <property name="fhir.baseurl.dstu2" value="http://localhost:8080/baseDstu2"/>
        <property name="fhir.baseurl" value="http://localhost:8080"/>
    </system-properties>
```


Build & Deploy
--------------
Build the application using the following order of packages and then deploy the war. Always use clean and install Maven lifecycle. 

1. HAPI-FHIR (root)
2. HAPI FHIR TestPage Overlay
3. FHIR Server - BlueButton 2.0 - Gaj

![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/mavenLifecycle.png)
	
	http://localhost:8080/fhir/


Thanks
------
Thanks to HAPI Team and UHN folks especially James Agnew


Contact
--------
Gajen Sunthara => gajen.sunthara@post.harvard.edu
