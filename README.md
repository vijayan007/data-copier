# Data Copier
Copies data from one DB to another DB
## How to use?
- Load this project in to any of your favorite ide
- Locate `src/main/resources/input-file.txt` and provide source and destination database connections details as shown below without adding any empty line
- Sample `input-file.txt`
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
- Run `java com.github.vijayan007.DataCopier -Djava.util.logging.config.file=src/main/resources/logging.properties src/main/resources/input-file.txt`
