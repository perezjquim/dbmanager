# DBManager

SQLite databases helper class for development purposes

## Install Instructions

Build.gradle (root):
```gradle
allprojects {
	repositories {
		(...)
		mavenCentral()
		maven{
		    url  'https://oss.sonatype.org/content/repositories/snapshots/'
		    name 'OSS-Sonatype'
		}
		maven { url "https://jitpack.io" }
		(...)
	}
}
```

Build.gradle (app):
```gradle
dependencies
{
    (...)
    implementation 'com.github.perezjquim:dbmanager:master-SNAPSHOT'
    (...)
}
```

## Examples of use

Initialization:
```java
{
	(...)

	DatabaseManager dbManager = new DatabaseManager("ExampleDB");

	(...)
}
```

Select operation:
```java
{
	(...)

	String[] from = { "Table1" };
	HashMap<String,String> where = new HashMap<>();
	where.put("id","2");
	where.put("name","dummy");
	where.put("balance","10");	
	Cursor selectExample = dbManager.select(from,where);

	(...)
}
```	

Insert operation:
```java
{
	(...)

	String into = "Table1";
	HashMap<String,String> data = new HashMap<>();
	data.put("id","3");
	data.put("name","dummy2");
	data.put("balance","22");		
	dbManager.insert(into,data);

	(...)
}
```		

Update operation:
```java
{
	(...)

	String table = "Table1";
	HashMap<String,String> newData = new HashMap<>();
	newData.put("balance","33");			
	HashMap<String,String> filter = new HashMap<>();
	newData.put("id","3");			
	dbManager.update(table,newData,filter);

	(...)
}
```			

Delete operation:
```java
{
	(...)

	String table = "Table1";
	HashMap<String,String> filter = new HashMap<>();
	filter.put("id","3");			
	dbManager.delete(table,filter);

	(...)
}
```	

Create a new table:
```java
{
	(...)

	String table = "Table2";
	Column[] columns = { new Column("Test","VARCHAR(45)",true,true); } // The last two parameters of each column refer to if it can handle null values and if it is the primary key, respectively
	dbManager.createTable(table,columns);

	(...)
}
```		

Clear an existing table:
```java
{
	(...)

	dbManager.clearTable("Table1");

	(...)
}
```

Custom query:
```java
{
	(...)

	dbManager.query("SELECT 'somecustomquery'");

	(...)
}
```	

Custom set of queries done in a transaction:
```java
{
	(...)

	dbManager.queryInTransaction("INSERT INTO 'Table1' ('id') VALUES('1')","INSERT INTO 'Table2' ('id','name') VALUES('1','dummywtv')");

	(...)
}
```		