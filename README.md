# Open-source Rest Quick-start (EN)

[Readme FR](#user-content-open-source-rest-quick-start-fr)

Hi ! Here is an open source project for quick-starting a REST API using J2EE.
Fell free to do whatever you want with this code, it's free :) .
The project has been designed and tested on `Wildfly 19`, `MariaDB 10.2` (a MySQL fork) and `JAVA 13` but I tried to use as much as possible the generic J2EE functions, so it should not be too hard to adapt.
You can, of course, ask me anything or contribute to the project to enhance it.

This project provides the following functionalities :
 - Configuration properties and application stage
 - My logging best practices
 - Database persistence and Transaction management
 - DTO mapping
 - Generic REST DB querying system using queryparams
 - Localized Strings in DB
 - API security (JWT + security checks using pathparams)
 - Automatic API Documentation generation
 - Image upload and resizing
 - Exception handling (with JSON body response)
 - Notifications using SSE (Server-Sent-Events)
 - Mail system (you must configure your SMTP server)
 - Arquillian Tests with real database

## Used frameworks and their documentation
 - **J2EE / CDI**: [https://docs.oracle.com/javaee/7/index.html](https://docs.oracle.com/javaee/7/index.html)
 - **Hibernate**: [https://hibernate.org/orm/documentation/5.4/](https://hibernate.org/orm/documentation/5.4/)
 - **QueryDSL**: [http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/](http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/)
 - **Deltaspike**: [https://deltaspike.apache.org/documentation/](https://deltaspike.apache.org/documentation/)
 - **Resteasy**: [https://docs.jboss.org/resteasy/docs/3.11.2.Final/userguide/html_single/index.html](https://docs.jboss.org/resteasy/docs/3.11.2.Final/userguide/html_single/index.html)
 - **Jackson**: [https://github.com/FasterXML/jackson-docs](https://github.com/FasterXML/jackson-docs)
 - **Enunciate**: [https://github.com/stoicflame/enunciate/wiki](https://github.com/stoicflame/enunciate/wiki)
 - **Arquillian**: [https://arquillian.org/guides/](https://arquillian.org/guides/)

## Launching the app

### 1 - Install Wildfly
Install Wildfly 19 on your system: http://wildfly.org/downloads/.

### 2 - Create Database

The MariaDB version must be at least 10.2 if you want to user localized strings !!!

Create a Mysql database called `yourdbname` with UTF8-general-ci collation

Add a user `user` with `password` as password and all rights for this database.

### 3 - Add the datasource to Wildfly
Add the following datasource and driver to your wildfly `standalone.xml` configuration (replace `yourdbname`, `user` and `password`).

```xml
<datasources>
    <datasource jndi-name="java:/jboss/datasources/restquickstart" pool-name="MySQLPool">
        <connection-url>jdbc:mysql://localhost:3306/yourdbname</connection-url>
        <driver>com.mysql</driver>
        <pool>
            <max-pool-size>30</max-pool-size>
        </pool>
        <security>
            <user-name>user</user-name>
            <password>password</password>
        </security>
    </datasource>
    <drivers>
        <driver name="com.mysql" module="com.mysql"/>
    </drivers>
</datasources>
```

### 4 - Add the MySQL driver to Wildfly
 - Download the JAVA MySQL driver here : https://dev.mysql.com/downloads/connector/j/
 - Copy the jar to your Wildfly server in `/{wildfly_path}/modules/system/layers/base/com/mysql/main`
 - Add a `module.xml` file with following content (replace `name_of_the_jar_file`)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.0" name="com.mysql">
   <resources>
      <resource-root path="name_of_the_jar_file" />
   </resources>
   <dependencies>
      <module name="javax.api" />
      <module name="javax.transaction.api" />
   </dependencies>
</module>
```

### 5 - Add the mail configuration
Add the following configuration to your `standalone.xml` (change "smtp.domain.com", "username" and "password" values).

For local testing, you can use local server, even if it does not exists. The tests use a mock server mail.

```xml
<socket-binding-group>
    <outbound-socket-binding name="restQuickstartSMTP">
        <remote-destination host="smtp.domain.com" port="465"/>
    </outbound-socket-binding>
</socket-binding-group>
```

```xml
<subsystem xmlns="urn:jboss:domain:mail:3.0">
    <mail-session name="restQuickstart" jndi-name="java:jboss/mail/restQuickstart">
        <smtp-server outbound-socket-binding-ref="restQuickstartSMTP" ssl="true" username="username" password="password"/>
    </mail-session>
</subsystem>
```

### 6.a - Launch the app (eclipse)
 - Import the project as a existing pom in Eclipse
 - Add the server to your runtimes (Servers view -> Left click -> Add -> Server)
 - Add the app to the server (left click on newly created server -> Add and Remove)
 - Launch the server
 - The API is deployed. Root path is http://localhost:8080/rest-quickstart/api
 
### 6.b - Launch the app (no IDE)
 - Install maven
 - Compile the app : `mvn clean compile package`
 - Copy the war file created from the `target` folder to your wildfly `deployments` folder
 - Launch wildfly with the `/wildfly_path/bin/standlone.sh` script
 - The API is deployed. Root path is http://localhost:8080/rest-quickstart/api

### 6.c - Launch tests (eclipse)
 - Add the `$JBOSS_HOME=path_to_wildfly` as environment variable.
 - Add a `restquickstarttest` datasource to Wildfly (see [Chapter 3](#user-content-3---add-the-datasource-to-wildfly)) 
 - Run the TestSuite.java file as a JUnit Test.
 
 
# Open-source Rest Quick-start (FR)
Salut ! Je mets à disposition un projet open-source pour démarrer une nouvelle API REST avec J2EE.
Vous pouvez l'utiliser, en faire ce que vous voulez, c'est gratuit :).
Le projet a été créé et testé en utilisant `Wildfly 19`, `MariaDB 10.2` (un projet parallèle à MySQL) et `JAVA 13` mais j'ai essayé d'utiliser au maximum les librairies génériques de J2EE, donc ça ne devrait pas être trop dur de l'adapter.
Vous pouvez, bien sûr, me demander des précisions ou contribuer au projet pour l'améliorer.

Ce projet rempli les fonctionnalités suivantes :
 - Fichier de propriété pour la configuration paramétrable en fonction de l'environnement
 - Mes meilleurs pratiques pour le logging
 - La persistence en base de données et la gestion des transactions
 - Le mapping de DTO
 - Un système générique de requêtes pour la base données utilisant les queryparams
 - Un système de string localisées en DB
 - La sécurité de l'API (JWT + des vérifications de sécurité qui utilisent les pathparams)
 - La génération automatique de la documentation de l'API
 - Upload d'image et redimensionnement
 - Gestion des exceptions (avec corps de réponse JSON)
 - Notifications en utilisant SSE (Server-Sent-Events)
 - Système de mail (vous devez configurer votre serveur SMTP)
 - Des tests Arquillian avec une vraie base de données

## Frameworks utilisés et leur documentation
 - **J2EE / CDI** : [https://docs.oracle.com/javaee/7/index.html](https://docs.oracle.com/javaee/7/index.html)
 - **Hibernate** : [https://hibernate.org/orm/documentation/5.4/](https://hibernate.org/orm/documentation/5.4/)
 - **QueryDSL** : [http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/](http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/)
 - **Deltaspike** : [https://deltaspike.apache.org/documentation/](https://deltaspike.apache.org/documentation/)
 - **Resteasy** : [https://docs.jboss.org/resteasy/docs/3.11.2.Final/userguide/html_single/index.html](https://docs.jboss.org/resteasy/docs/3.11.2.Final/userguide/html_single/index.html)
 - **Jackson** : [https://github.com/FasterXML/jackson-docs](https://github.com/FasterXML/jackson-docs)
 - **Enunciate** : [https://github.com/stoicflame/enunciate/wiki](https://github.com/stoicflame/enunciate/wiki)
 - **Arquillian** : [https://arquillian.org/guides/](https://arquillian.org/guides/)

## Lancer l'application

### 1 - Installer Wildfly
Installer Wildfly 19 sur votre système : http://wildfly.org/downloads/.

### 2 - Créer la base de données

La version de MariaDB doit être au moins 10.2 si vous voulez utiliser les strings localisées !!!

Créer une base de données MySQL appellée `yourdbname` avec l'interclassement UTF8-general-ci

Ajouter un utilisateur `user` avec le mot de passe `password` et la totalité des droits sur la base de données.

### 3 - Ajouter la datasource à Wildfly
Ajouter la datasource suivante dans votre configuration `standalone.xml` (remplacer `yourdbname`, `user` et `password`).

```xml
<datasources>
    <datasource jndi-name="java:/jboss/datasources/restquickstart" pool-name="MySQLPool">
        <connection-url>jdbc:mysql://localhost:3306/yourdbname</connection-url>
        <driver>com.mysql</driver>
        <pool>
            <max-pool-size>30</max-pool-size>
        </pool>
        <security>
            <user-name>user</user-name>
            <password>password</password>
        </security>
    </datasource>
    <drivers>
        <driver name="com.mysql" module="com.mysql"/>
    </drivers>
</datasources>
```

### 4 - Ajouter le driver MySQL à Wildfly
 - Télécharger le driver JAVA MySQL ici : https://dev.mysql.com/downloads/connector/j/
 - Copier le JAR dans votre serveur Wildfly dans le dossier `/{wildfly_path}/modules/system/layers/base/com/mysql/main`
 - Ajouter  un fichier `module.xml` avec le contenu suivant (remplacer `name_of_the_jar_file`)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.0" name="com.mysql">
   <resources>
      <resource-root path="name_of_the_jar_file" />
   </resources>
   <dependencies>
      <module name="javax.api" />
      <module name="javax.transaction.api" />
   </dependencies>
</module>
```

### 5 - Ajouter la configuration mail
Ajouter la configuration suivante à votre `standalone.xml` (changer `smtp.domain.com`, `username` et `password`).

Pour tester en local, vous pouvez utiliser localhost, meme s'il n'existe pas. Les tests utilisent un serveur mail mock.

```xml
<socket-binding-group>
    <outbound-socket-binding name="restQuickstartSMTP">
        <remote-destination host="smtp.domain.com" port="465"/>
    </outbound-socket-binding>
</socket-binding-group>
```

```xml
<subsystem xmlns="urn:jboss:domain:mail:3.0">
    <mail-session name="restQuickstart" jndi-name="java:jboss/mail/restQuickstart">
        <smtp-server outbound-socket-binding-ref="restQuickstartSMTP" ssl="true" username="username" password="password"/>
    </mail-session>
</subsystem>
```

### 6.a - Lancer l'application (eclipse)
 - Importer le pom dans eclipse
 - Ajouter le serveur à eclipse (Vue serveurs -> Click droit -> Ajouter -> Serveur)
 - Ajouter l'application au serveur (Clic droit sur le serveur nouvellement créé -> Ajouter et supprimer)
 - Lancer le serveur
 - L'API est déployée. Le chemin racine est http://localhost:8080/rest-quickstart/api
 
### 6.b - Launch the app (no IDE)
 - Installer maven
 - Compiler l'application : `mvn clean compile package`
 - Copier le war créé depuis le dossier`target` vers votre dossier `deployments` de Wildfly
 - Lancer wildfly avec le script `/wildfly_path/bin/standlone.sh`
 - L'API est déployée. Le chemin racine est http://localhost:8080/rest-quickstart/api

### 6.c - Launch tests (eclipse)
 - Ajouter la variable d'environnement `$JBOSS_HOME=path_to_wildfly` 
 - Ajouter une datasource `restquickstarttest` à Wildfly (voir le [Chapitre 3](#user-content-3---ajouter-la-datasource-à-wildfly)) 
 - Lancer le fichier TestSuite.java comme Test Junit

 

