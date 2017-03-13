# StudentManagement
Assignment for Korea Software HRD center
## Library use
PostgreSQL JDBC - [Link Here](https://jdbc.postgresql.org/)

JFoenix - [Link Here](http://www.jfoenix.com/)

ControlFX - [Link Here](http://fxexperience.com/controlsfx/)

## Relational Database
```
├── tbl_user
│   ├── user_id
│   ├── user_email (unique)
│   ├── user_password
│ 
├── tbl_subject
│   ├── sub_id 
│   ├── sub_name (unique)
├── tbl_student
    ├── std_id
    ├── std_full_name
    ├── std_gender 
    ├── std_sub_id (int) (REFERENCES tbl_subject(sub_id))
    ├── std_email
    ├── std_password
    ├── isPaid

```
