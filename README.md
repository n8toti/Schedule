# Schedule
The user is prompted to log in. A report is created of all succesful and attempted logins labeled login_activity.txt. If the users system is in French they will be prompted in French-- otherwise English.
In this application I am pulling data from a database that is shared between 3 geographically different countries. The database data houses customers and
appointments, contacts and users countries and regions. I use the information from the database to develop a working application to schedule
update, delete, and add customers and appointments all in a functional and self explanitory GUI for the end user. A customer is not allowed to overlap their appointments. A customer can not be deleted if they have appointments scheduled. A report is created of a contacts schedule. Appointments can be viewed by week or by month. A report for total appointments by type and month is created and displayed in a table.

@Author Nathanial Toti

@Contact ntoti@wgu.edu

@Application Version: 1.0

@Date 2022-06-24

@IDE IntelliJ IDEA 2022.1.3 (Community Edition) JDK 18_linux-x64_bin SDK 18.0.1_linux-x64

The Main class has the main method to run the program.

I created a report that narrows down the earliest appointment with all of the appointments accompanying
information.

MySQL connector driver Version # mysql-connector-java-8.0.25
