## image-service
* Upload an image to the endpoint `/upload/image`
* Calculate the SHA256 hash of the image.
* Store the hash, image and additional properties in a DB. (Not recommended to store image in DB)
* Disallow uploading the same image twice based on the hash.
* Show image at `get/image/{name}`
* Show image details at `/get/image/info/{name}`

### To run
```
$ ./gradlew clean build -x test
$ ./gradlew bootRun
```

To run the tests:
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
| image | mediumblob   | NO   |     | NULL    |       |
| name  | varchar(255) | YES  |     | NULL    |       |
| size  | bigint       | YES  |     | NULL    |       |
| type  | varchar(255) | YES  |     | NULL    |       |
+-------+--------------+------+-----+---------+-------+
6 rows in set (0.01 sec)
```

### Issues
When testing using `@DataJpaTest` with `com.h2database:h2:2.1.210` dependency, the generation type
`@GeneratedValue(strategy = GenerationType.IDENTITY)` for the `Image` Id field causes:
```
Caused by: org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException: 
NULL not allowed for column "ID"; SQL statement:
insert into image (id, hash, image, name, size, type) values (null, ?, ?, ?, ?, ?) [23502-210]
```
When the generation type is changed to `@GeneratedValue(strategy = GenerationType.AUTO)` the tests pass.
This creates a second table called `hibernate_sequence` with a column for `next_val`.