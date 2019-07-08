## DB Å×ÀÌºí

### Board

	```sql 
	CREATE TABLE b_board (
  	num int(6) unsigned NOT NULL AUTO_INCREMENT,
   	title varchar(50) NOT NULL,
   	count int(5) unsigned default 0,
   	date datetime NOT NULL DEFAULT current_timestamp,
   	contents varchar(250),
   	id varchar(20) not null default '',
   	del_chk varchar(1) not null default 'N',
   	PRIMARY KEY (num),
   	foreign key (id) references b_users(id)
   	) AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
	```

### Users

	```sql
	CREATE TABLE b_users (
    	id varchar(20) not null default '',
    	name varchar(10) not null default '',
    	pwd varchar(20) not null default '',
    	PRIMARY KEY (id)
  	) DEFAULT CHARSET=utf8;
	```