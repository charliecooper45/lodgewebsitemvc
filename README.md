# lodgewebsitemvc
This project was inspired by my parents buying a holiday lodge in Cornwall, UK. They asked me to create a simple <a href="https://github.com/charliecooper45/lodgewebsite">website</a> that gave users an overview of the lodge itself and the surrounding area. I decided to use this opportunity to enhance the project and help me learn the Spring framework. The enhancements to the static website are as follows:

1. User accounts (registration/login and password reset)
2. Reviews (add/delete and view reviews)
3. Account management (update fields such as password, name and email)
4. Settings (users can set and update their language preference)

## Stack
### Back end
As mentioned above the aim of this project was to expose myself to the Spring framework. With this in mind the back end stack is very Spring focused. Please find a list of technologies below:

1. Spring
    * MVC
    * Security
    * Data
    * AMQP
    * Rabbit
    * Mail
2. Logging
    * Logback
    * SL4J
3. Persistence
    * Hibernate
    * PostgreSQL
    * Flyway
    * HikariCP
4. Maven
    * Parent POM
    * Three modules (web, core and notification)
5. Velocity (for email templates)
6. JSON
    * Json Web Tokens (for email links), jjwt library
    * Jackson
7. Properties
    * jasypt
    * jasypt Spring Boot
8. Testing
    * JUnit
    * Spring Test
    * Mockito
    * JsonPath
    * Spring Rabbit test

### Front end
The front end is served via the Thymeleaf templating engine. JQuery is used extensively to make asynchronous calls to the Java backend. Towards the end of the project I made the decision to update to Bower as dependency management was becoming difficult to maintain.

1. Bootstrap
    * Font awesome
    * Vertical tabs
2. JavaScript
    * AJAX
    * JQuery
    * Modernizr
3. Thymeleaf
    * Standard Layout Dialect
    * Spring Data Dialect
    * Spring Security Dialect
4. HTML
    * Local Storage
5. Bower

### Environment
1. Three environments (controlled via Spring profiles)
    * dev, test, prod

## Deployment
During the project I setup a home server running Ubuntu Linux. The application runs on Apache Tomcat and is served through Apache. The build process is currently not automated (this is a future improvement) but TeamCity is used for continuous integration.
