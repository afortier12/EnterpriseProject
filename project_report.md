CP-630 Final Project Report

Author: Adam Fortier
Date: Dec 22, 2020

# Final Project Tool life monitoring

## Data

Dataset was obtained from https://www.kaggle.com/saeedsh91/cnc-tool-wear.  The dataset is the collected from 18 experiments with a training dataset that includes the results of each experiment.

## Design

The following applications were developed:

- ec-project-admin-mvn: maven projeect for setting up the user table and creating a super user

- ec-project-setup:  This purpose of this application is to setup the tables in the mysql database.  It also is where the experiment and training csv files are loaded.  These files are combined so that all 18 experiments are in one dataset.  The results from the training data are then merged into the dataset.  These in include: machining completed, tool worn and visual inspection passed.  This app also includes the servlets and web pages for interacting with the beans and POJOs.

- ec-project:	This application contains the POJOs, repositories and enterprise beans for the data, model and users.

- ec-project-web:	This application uses servlets and JAX-RS to communicate with the beans in the ec-project application.  This also includes the web pages for client interaction.  Users, models and evaluations can be created here.

- ec-project-spring-boot:	This application uses Spring Boot, providing a RestController for communicating with the ec-project-web application.  The controller interacts with the Spring beans that provide a prediction based on the models created.

## Setup 

### Enterprise application

Copy the domain, slave-1 and slave-2 folders provided to the wildfly installation directory.  Open command prompt and run domain.bat found in the bin folder of the wildfly installation directory.  

![](.\images\setup_domain_start.png)

![](.\images\setup_add_deployment.png)

![](.\images\setup_domain_servers.png)


### Spring boot application


Open a command prompt and select the directory of the supplied jar file.  Type java -jar target/ec-project-spring-boot.jar  to run the file

![](.\images\spring.png)


### MySQL

Open a command prompt and select mysql installation bin directory.  Type mysqld --console to start mysql

![](.\images\mysql.png)

## Application Usage

### Create user table and super user

Open a command prompt and select the directory of the supplied jar file.  Type java -jar target/ec-projectadmin-mvn.jar  to run the file

![](.\images\admin.png)

![](.\images\admin_table.png)

### create tables and import csv data

Enter http://localhost:8080/ejb-project-web/ in the browser.  Login using super user account:  username-super, password-cp630super

![](.\images\login.png)

Press SETUP in top left corner

![](.\images\index.png)

Press Select next to 'create tables' to add the new tables to the database
 
![](.\images\new_tables.png)

Upload the 19 csv files and press Upload

![](.\images\data_import.png)

Enter http://localhost:8080/ejb-project-web/ in the browser to return to the index.

### create models, evaluate models and add users

![](.\images\data_import.png)

Press select beside create model. Select model from dropdown and press Select.

![](.\images\create_model.png)

Status of model creation is shown on the screen

![](.\images\create_status.png)

Tool model is saved to database

![](.\images\create_db1.png)


Tool training data is saved to the database

![](.\images\create_db2.png)


Press select beside evaluate model. Select model from dropdown and press Select.

![](.\images\eval_model.png)


Results are displayed on completion

![](.\images\eval_results.png)

Press select beside users

![](.\images\users.png)

Click add user

![](.\images\user_add.png)

Enter username and password and role (1 for admin, 2 for operator)

![](.\images\user_add_success.png)


##  Operator screen

Logout and enter operator credentials

![](.\images\operator.png)

Choose a machine and press select.  A new page will display a simulation of realtime tool life management.  Datasets are imported in batches to detect a tool worn

![](.\images\realtime.png)


