# Library - SQL scripts
This folder contains the scripts to create the user associated to the library database (user: library, default password library) and the schema of the database. It also contains script to configure the Tomcat realm.

## Installation
1. Connect as root to mysql, execute script librarydb-user.sql, then exit.
2. Connect as library (password: library) to mysql, execute script librarydb-scheme.sql, then exit.