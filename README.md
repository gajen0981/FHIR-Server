
# FHIR-Server
=============
This is the HAPI based FHIR server with Blue Button capabilities that is fine tuned for enterprise scalability in mind for any RDBMS and JBoss Application Server. This technology stack is developed using industry standards technology including JavaEE with SpringMVC frameworks. 

Documentation is still in progress and it's in DRAFT format.

WHY
---
Healthcare Interoperability using HL7's open FHIR standards over HTTPS transport (RESTful API).


WHAT
----
HAPI based FHIR server with Blue Button capabilities. Able to download the files in FHIR JSON or FHIR XML for untethered PHR (Business 2 Consumer) or provider to provider health inetroperability (Business 2 Business).  Imagine this Blue Button functionality is implemented in every patient portal to free the data to their patients and have them empower and take control of their health that can improve health outcome and reduce healthcare cost. 

### Blue Button apabilities including patients able to download everything as a FHIR Bundle
Download the healthcare data as FHIR JSON or FHIR XML format. 

![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/BB2Download.png)

### Blue Button capabilities
Untethered PHR, patient can download/access/share the data with providers & researchers. 

![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/BB2DownloadedFiles.png)

### Use case around Patient centered Personal Healh Record (PHR) that can enagage and empower patient that can improve health outcome and reduce healthcare cost. 

- Patient Generated Heatlh Data (PGHD): Apple Health, FitBit or any health wearables 
- Repository of EHR, Genomics, PGHD or mHealth data. 
- Patient controlled API for inbound and outbound transactions.  
- Donate the data for research & science including Precision Medicine Initiates, ResearchKit, GitHub and other platforms.

End point app is in early stages of developments and it maily focused around patient mediated, engagement & empowerment for better heatlh outcomes. 

Login using OpenID Connect & OAuth 2.0 and Single Sign On (SSO)
![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/PHR_1_Login.png)

Personal Health Record to engage and empower patient health and improve health outcomes. 
![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/PHR_2_Dashboard.png)

myHealth API - Patient inbound & outbound API 
![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/PHR_3_API.png)

myHealth Repo - Clinical File Systems (CFS)
Collect clinical, PGHD, Genomics data.
![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/PHR_4_Repo.png)

myHealth Repo - Share & Donate your data
![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/PHR_5_Donate.png)

myHealth Wearables - Connect to health wearables devices
![alt tag](https://github.com/gajen0981/FHIR-Server/blob/master/screenshots/PHR_6_HealthWearables.png)


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
