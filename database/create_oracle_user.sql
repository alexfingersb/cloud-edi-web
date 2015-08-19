create user cloudedi
identified by cloudedi
default tablespace users
temporary tablespace temp
quota 0 on system
quota unlimited on users;

grant 
  connect, 
  resource, 
  create view, 
  create any materialized view, 
  alter any materialized view, 
  drop any materialized view, 
  query rewrite, 
  global query rewrite 
  to cloudedi;
