# Data Copier
- Copies data from one DB to another DB
- Download this repo in to your local machine
## Steps for MySQL DB
- `cd run`
- edit `mysql.txt` and provide source and destination database connections details and each sqls in a line as shown below 
- **Note: don't add any empty space**
### Sample input file
```sql
com.mysql.cj.jdbc.Driver
jdbc:mysql://localhost:3306/source
root
mysql
com.mysql.cj.jdbc.Driver
jdbc:mysql://localhost:3306/dest
root
mysql
SELECT * FROM table1
```
### Command to Run
`java -cp mysql-connector-java-6.0.5.jar:data-copier-1.0.1.jar -Djava.util.logging.config.file=logging.properties com.github.vijayan007.DataCopier mysql.txt`

### Sample Output
```
Feb 10, 2017 6:53:16 PM com.github.vijayan007.DataCopier main INFO: DataCopier {source=Database {jdbcUrl=jdbc:mysql://localhost:3306/source, jdbcDriverClassName=com.mysql.cj.jdbc.Driver, username=root, password=******}, destination=Database {jdbcUrl=jdbc:mysql://localhost:3306/dest, jdbcDriverClassName=com.mysql.cj.jdbc.Driver, username=root, password=******}, sqls=[SELECT * FROM table1]}
Feb 10, 2017 6:53:16 PM com.github.vijayan007.DataCopier main INFO: Destination SQL:insert into table1 (id, name) values (?, ?)
Feb 10, 2017 6:53:16 PM com.github.vijayan007.DataCopier main INFO: Closing connections
Feb 10, 2017 6:53:16 PM com.github.vijayan007.DataCopier main INFO: Closed connections successfully
```
## Steps for Oracle DB
- `cd run`
- edit `oracle.txt` and provide source and destination database connections details and each sqls in a line as shown below 
- **Note: don't add any empty space**
### Sample input file
```sql
oracle.jdbc.OracleDriver
jdbc:oracle:thin:@host1:1521/DB1
user
password
oracle.jdbc.OracleDriver
jdbc:oracle:thin:@host2:1521/DB2
user
password
SELECT * FROM table1
```
### Command to Run
`java -cp mysql-connector-java-6.0.5.jar:data-copier-1.0.1.jar -Djava.util.logging.config.file=logging.properties com.github.vijayan007.DataCopier mysql.txt`

### Sample Output
```
Feb 10, 2017 6:49:52 PM com.github.vijayan007.DataCopier main INFO: DataCopier {source=Database {jdbcUrl=jdbc:oracle:thin:@host1:1521/DB1, jdbcDriverClassName=oracle.jdbc.OracleDriver, username=user, password=******}, destination=Database {jdbcUrl=jdbc:oracle:thin:@host2:1521/DB2, jdbcDriverClassName=oracle.jdbc.OracleDriver, username=user, password=******}, sqls=[SELECT * FROM table1]}
Feb 10, 2017 6:49:53 PM com.github.vijayan007.Database openConnection SEVERE: unable open connection becasue of IO Error: Unknown host specified 
Feb 10, 2017 6:49:54 PM com.github.vijayan007.Database openConnection SEVERE: unable open connection becasue of IO Error: Unknown host specified 
Feb 10, 2017 6:49:54 PM com.github.vijayan007.DataCopier main INFO: Closing connections
Feb 10, 2017 6:49:54 PM com.github.vijayan007.DataCopier main INFO: Closed connections successfully
```
