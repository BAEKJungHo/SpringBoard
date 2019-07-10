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
	atch_file_id char(25) not null default '0',
   	del_chk varchar(1) not null default 'N',
   	PRIMARY KEY (num),
   	foreign key (id) references b_users(id),
	foreign key (atch_file_id) references b_filemaster(atch_file_id)
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

### FileMaster 

	```sql 
    	create table b_filemaster (
   	atch_file_id char(25) not null default '0', 
	create_dt datetime not null default current_timestamp,
	use_at char(1) not null default 'Y',
   	primary key (atch_file_id)
    	) DEFAULT CHARSET=utf8;
	```

### FileDetail
	
	```sql 
     	create table b_filedetail (
   	atch_file_id char(25) not null default '0',
	file_sn int(10) not null AUTO_INCREMENT, 
   	ori_name varchar(150) not null, 
   	save_name varchar(150) not null, 
   	save_path varchar(2000) not null, 
   	file_size int(10) not null,
   	primary key (atch_file_id, file_sn),
   	UNIQUE KEY(file_sn),
   	foreign key (atch_file_id) references b_filemaster(atch_file_id)
    	) AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
	```