[![CircleCI](https://circleci.com/gh/ericbalawejder/image-service.svg?style=svg)](https://circleci.com/gh/ericbalawejder/image-service)

## image-service
* Upload an image to the endpoint `/upload/image`
* Calculate the SHA256 hash of the image.
* Store the hash, image and additional properties in a DB. (Not recommended to store image in DB)
* Disallow uploading the same image twice based on the hash.
* Show image at `get/image/{name}`
* Show image details at `/get/image/info/{name}`
* Delete image at `/delete/image/{name}`

### To run
```
$ docker-compose up
```
#### Local development
To run locally, the datasource in the `application.properties` needs to point to your local mysql database.
`spring.datasource.url=jdbc:mysql://localhost:3306/image-service?createDatabaseIfNotExist=...`

```
$ ./gradlew clean assemble
$ ./gradlew bootRun
```

To run the tests:
```
$ ./gradlew clean build
```
or
```
$ ./gradlew test
```

### Postman
Post request:
![post](/assets/image-service-post.png)

Get request for image:
![get-image](/assets/image-service-get.png)

Get request for image info:
![get-image-info](/assets/image-service-get-info.png)

### Database schema
Spring Data JPA sets up the table defined by the entity class `Image`. The database connection is configured
in the `application.properties` file.
```mysql
mysql> desc image;
+-------+--------------+------+-----+---------+-------+
| Field | Type         | Null | Key | Default | Extra |
+-------+--------------+------+-----+---------+-------+
| id    | bigint       | NO   | PRI | NULL    |       |
| hash  | varchar(44)  | NO   | UNI | NULL    |       |
| photo | mediumblob   | NO   |     | NULL    |       |
| name  | varchar(255) | YES  |     | NULL    |       |
| size  | bigint       | YES  |     | NULL    |       |
| type  | varchar(255) | YES  |     | NULL    |       |
+-------+--------------+------+-----+---------+-------+
6 rows in set (0.01 sec)
```

### Issues
When testing using `@DataJpaTest` with `com.h2database:h2:2.1.210` in an `@ActiveProfiles("test")`, 
the generation type `@GeneratedValue(strategy = GenerationType.IDENTITY)` for the `Image` Id field causes:
```
Caused by: org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException: 
NULL not allowed for column "ID"; SQL statement:
insert into image (id, hash, image, name, size, type) values (null, ?, ?, ?, ?, ?) [23502-210]
```
`@ActiveProfiles("test")` is configured with `application-test.properties` in `src/test/resources`.
When the generation type is changed to `@GeneratedValue(strategy = GenerationType.AUTO)` the tests pass.
It appears the H2Dialect does not support the `IDENTITY` strategy even though it says it does?
The application works with either strategy but the test will only work with `AUTO`.