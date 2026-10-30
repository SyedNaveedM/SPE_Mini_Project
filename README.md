# SPE_Mini_Project
## Syed Naveed Mohammed - IMT2023119

# Calculator REST API - CI/CD Mini-Project

A Spring Boot REST API that performs basic arithmetic operations. The
project uses Maven for building and testing, Docker for
containerization, Docker Compose to run the API with an Nginx reverse
proxy, and Jenkins for automated CI/CD.

## Tech Stack

-   Java 21
-   Spring Boot
-   Maven
-   JUnit 5 and Spring MVC tests
-   Docker and Docker Compose
-   Nginx
-   Jenkins with GitHub webhook integration
-   Docker Hub

## Project Structure

``` text
SPE_Mini_Project/
├── src/
│   ├── main/
│   └── test/
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
├── .dockerignore
└── nginx/
    ├── Dockerfile
    └── default.conf
```

## Prerequisites

Install the following tools:

-   JDK 21
-   Apache Maven
-   Docker
-   Docker Compose

## Build the Application

From the project root, run:

``` bash
mvn clean package
```

The executable JAR is generated in the `target/` directory.

## Run the Tests

Run all unit and integration tests with:

``` bash
mvn test
```

Maven Surefire writes JUnit XML reports to:

``` text
target/surefire-reports/
```

## Run the API Locally

Start the Spring Boot application:

``` bash
mvn spring-boot:run
```

The application uses port `8081` in the containerized deployment. If
your local configuration uses a different port, use that port in the
URLs below.

Alternatively, run the packaged JAR:

``` bash
java -jar target/<application-jar>.jar
```

## API Endpoints

The API accepts two numeric query parameters, `a` and `b`, and returns a
JSON response.

Operation        Method and endpoint                 Example result for `a=10`, `b=5`
  ------ ----------- ------------
Addition         `GET /api/v1/add?a=10&b=5`                                      `15`
Subtraction      `GET /api/v1/subtract?a=10&b=5`                                  `5`
Multiplication   `GET /api/v1/multiply?a=10&b=5`                                 `50`
Division         `GET /api/v1/divide?a=10&b=5`                                    `2`
Health           `GET /actuator/health`                                          `UP`

### Example API Calls

When running locally on port `8081`:

``` bash
curl -i "http://localhost:8081/api/v1/add?a=10&b=5"
curl -i "http://localhost:8081/api/v1/subtract?a=10&b=5"
curl -i "http://localhost:8081/api/v1/multiply?a=10&b=5"
curl -i "http://localhost:8081/api/v1/divide?a=10&b=5"
curl -i "http://localhost:8081/actuator/health"
```

A successful arithmetic request returns HTTP `200` and a JSON response
similar to:

``` json
{
  "operation": "add",
  "a": 10,
  "b": 5,
  "result": 15
}
```

Division by zero returns HTTP `400` with a structured error response.
For example:

``` bash
curl -i "http://localhost:8081/api/v1/divide?a=10&b=0"
```

Example error body:

``` json
{
  "error": "Bad Request",
  "message": "Cannot divide by zero"
}
```

## Run with Docker Compose

The Compose deployment starts two services:

-   **calculator-api** - the Spring Boot application.
-   **nginx** - reverse proxy that forwards requests to the calculator
    API.

Build the images and start the services:

``` bash
docker compose up --build -d
```

Check the running containers:

``` bash
docker compose ps
```

View application logs:

``` bash
docker compose logs -f
```

The Nginx reverse proxy is published on port `80`. Use these URLs when
the Compose stack is running:

``` bash
curl -i "http://localhost/api/v1/add?a=10&b=5"
curl -i "http://localhost/api/v1/subtract?a=10&b=5"
curl -i "http://localhost/api/v1/multiply?a=10&b=5"
curl -i "http://localhost/api/v1/divide?a=10&b=5"
curl -i "http://localhost/actuator/health"
```

Stop the stack:

``` bash
docker compose down
```

## Docker Hub

The calculator API image is published to:

<https://hub.docker.com/r/naveed938/calculator-api>

The Jenkins pipeline publishes a build-number tag and the `latest` tag.

## CI/CD Pipeline

The declarative `Jenkinsfile` defines the automated workflow. A push to
the configured GitHub branch triggers Jenkins through a GitHub webhook.

Pipeline stages:

1.  **Checkout** - retrieves the latest source code.
2.  **Build** - packages the application using Maven.
3.  **Test** - runs the tests and publishes JUnit reports.
4.  **Docker Hub Login** - authenticates using credentials stored in
    Jenkins.
5.  **Build Docker Image** - builds and tags the calculator image.
6.  **Push to Docker Hub** - publishes the image tags.
7.  **Build Nginx Image** - builds the reverse-proxy image required by
    Compose.
8.  **Deploy with Docker Compose** - starts or updates the services.
9.  **Verify Deployment** - checks the deployed health endpoint.
10. **Post-build Notification** - sends an email for successful or
    failed builds.

Docker Hub credentials are configured in the Jenkins Credentials store
and are not committed to this repository.

## Author

**Syed Naveed Mohammed**

-   GitHub: [SyedNaveedM](https://github.com/SyedNaveedM)
-   Repository:
    [SPE_Mini_Project](https://github.com/SyedNaveedM/SPE_Mini_Project)
