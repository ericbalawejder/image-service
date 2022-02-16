## image-service
-Calculate the SHA256 sum of an image.

-Store the hash, image and additional properties in a DB.

-Disallow uploading the same image twice.

### Issues
When testing using `@DataJpaTest` with `com.h2database:h2:2.1.210`, the generation type
`@GeneratedValue(strategy = GenerationType.IDENTITY)` for `Image` `Id` field throws
```
Caused by: org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException: 
NULL not allowed for column "ID"; SQL statement:
insert into image (id, hash, image, name, size, type) values (null, ?, ?, ?, ?, ?) [23502-210]
```
Must change generation type to `@GeneratedValue(strategy = GenerationType.AUTO)` for tests to pass.
This creates a second table called `hibernate_sequence` with a column for `next_val`.