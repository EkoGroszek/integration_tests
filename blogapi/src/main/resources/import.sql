--this script initiates db for h2 db (used in test profile)
insert into user (account_status, email, first_name, last_name) values ('CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (account_status, email, first_name) values ('NEW', 'brian@domain.com', 'Brian')
insert into user (account_status, email, first_name, last_name) values ('CONFIRMED', 'adam@domain.com', 'Adam', 'Mada')
insert into user (account_status, email) values ('REMOVED', 'zofia@domain.com')

insert into blog_post values (null, 'Test post by confirmed user', 1)
insert into blog_post values (null, 'Test post by confirmed user without likes', 3)
insert into blog_post values (null, 'Removed user post', 4)