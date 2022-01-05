Running Instructions:

1.First, you have to use MySQL, if you do not have MySQL use this link : https://www.mysql.com/downloads/

2.You have to create DB named as "cafeteria" and give access to DB in application properties,
    for example my properties:
        spring.datasource.url=jdbc:mysql://localhost:3306/"cafeteria" !!!!
        spring.datasource.username=root !!!!
        spring.datasource.password=Webcam576 !!!!

3.You can post all entities.
    For the Employee and Product you have to use the format of those Entities:
           Product :
                    "name" : "cola",
                    "price" : "10"
           Employee :
                    "name" : "yaroslav",
                    "surname" : "borovyk",
                    "salary" : "1000"
   For Order posting you have to already have Employee and Product, also you will post or update Customer in the same time:
            Order:
                "customer_name" : "yaroslav"
                "customer_surname" : "borovyk",
                "employee_id" : "1",
                "products_id" : ["cola"]
    Posting Customer is not recommended. Customer has to be posted bby Order Posting Method.

4.Other API mappings ( put, get, delete ) allowed for all entities;

5.Be careful, I have SEQUENCE generating, so I have to look on id in DB before using some API mappings;

6.For the automated tests you have to create DB named "cafeteriatest".
    The tests covered whole business, data layer and REST API. I am not using CI via GitHub.

7.Some Methods are present but generally there is no more usage of them ( example -> getEmployeeOrder ).
    I left them to show that I could create more Methods for the business and API logic.

8.Also, main additional logic and additional operations are around Orders with the Products.
