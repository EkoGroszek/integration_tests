--this script initiates db for integration tests 
insert into user (account_status, email, first_name, last_name) values ('CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (account_status, email, first_name,last_name) values ( 'NEW', 'kamil@domain.com', 'Kamil','Zarski')
insert into user (account_status, email, first_name, last_name) values ('CONFIRMED', 'rober@domain.com', 'Robert', 'Witczak')
insert into user (account_status, email, first_name,last_name) values ( 'REMOVED', 'wiktor@domain.com', 'Wiktor','Jachimczak')
insert into blog_post values (null, 'stub for removed User', 1)
insert into blog_post values (null, 'stub for removed User', 2)
insert into blog_post values (null, 'stub for removed User', 3)
insert into blog_post values (null, 'stub for removed User', 4)


