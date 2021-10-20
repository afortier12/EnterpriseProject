Name: Adam Fortier
ID: 195827350
Email: fort7350@mylaurier.ca
WorkID: cp630-final-project
Statement: I claim that the enclosed submission is my individual work.

To help the instructors to understand and evaluate your project, please provide a brief short description, comments and self-evaluation according to the 10 items in the project specification. 

Project marking grid: Each of the following grading item weighs 3%: 1% for baseline, 1% for completion, 1% for additional/new features/tools/techniques being used.  

1. [30/30/] Write a project proposal (2-3 pages)

Project proposal written.  Pandoc with css used to generate html file: proposal.html

2. [30/30/] Design data format, collect data, create dataset for the application.

Data is supplied in csv format.  There are 18 files with recorded data and one training data set that has the result for each of the 18 files.  Results include tool worn, passed visual inspection and machine cycle complete.  These are merged into one dataset.  Data is saved to the database using JPA.

New: saving dataset to database.  Used NumericToNominal filter.  Websockets are used to update the display with the stages of the import process.  A react screen updates the display.

3. [30/30/] Develop and implement data application algorithms for the proposed application problem.

Used support vector machine, k nearest neighbours and decision tree algorithms.  

New:  principal component analysis (PCA) and attribute selection through ranking

4. [30/30/] do data computing to generate models, representing models in portable format and stored in file or database. More credit is given if distributed approach is used data mining of big dataset

Models are built and saved to the database.  PCA is also included for prediction analysis.  This is done in a spring boot RestContoller that runs the classifyInstance function on the selected model.  Stages of model generation are shown on the screen via websockets and javascript used on jsp pages.

New: ajax call to spring boot.  Websocket jsp generation

5. [30/30/] Create deployable service components using application models using Java based enterprise computing technologies, to create client program to do remote call of the data mining services.

Servlets are used to call for model creation and evaluation using JAX-RS webservices.  Predictions are retrieved via ajax calls to a spring boot rest controller.

6. [20/30/] Deploy service components onto enterprise application servers.

Deployment is done with wildfly domain (new) and spring boot. 

7. [30/30/] Create web services (SOAP, RESTful) to use the data service components.

Model creation and evaluation using JAX-RS webservices.  Predictions are retrieved via ajax calls to a spring boot rest controller.

8. [30/30/] Create web user interface/mobile applications to use the application/web services.

Used react, jsp, javascript and html for client pages

9. [10/30/] Test your services, log your services, and document your term project.

Logging was added to all beans and comments ans required

10. [30/30/] Demonstrate your term project in final project presentation, slides, or short video.

Supplied video shows a demo of the project

Total: [/30/]