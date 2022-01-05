1. My client is Spring Shell client which has 4 consoles:
        Employee, Order, Customer, Product

2. Main user of the client would be Employee, so only employee has access to the 
   create-order operation ( due some bugs list of products can't be accepted, only one product;-( ).

3.The main idea is to use console client to create entities in DB and manipulate them. Look at the
    Dtos and Shell Method to follow the naive format of the input parameters