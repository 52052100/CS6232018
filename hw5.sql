/*1. What are the #prods whose name begins with a ’p’ and are less than $300.00?*/
	SELECT prod_id FROM Product WHERE prod_id LIKE 'p%' AND price < 300.00;

/* 2. Names of the products stocked in ”d2”.*/
/*(a) without in/not in */
	SELECT pname FROM Product, Stock  WHERE Product.prod_id = Stock.prod_id AND dep_id = 'd2';

/*(b) with in/not in */
	SELECT pname FROM Product WHERE prod_id IN(SELECT prod_id FROM Stock WHERE dep_id='d2');

/*3. #prod and names of the products that are out of stock. */
/*(a) without in/not in */
	SELECT Product.prod_id, Product.pname FROM Product , Stock  WHERE Product.prod_id = Stock.prod_id AND quantity <= 0;

/*(b) with in/not in */
	SELECT prod_id,pname FROM Product WHERE prod_id IN(SELECT prod_id FROM Stock WHERE quantity<=0);

/*4. Addresses of the depots where the product ”p1” is stocked. */
/*(a) without exists/not exists and without in/not in */
	SELECT addr FROM Depot, Stock  WHERE Stock.prod_id = 'p1' AND Depot.dep_id = Stock.dep_id GROUP BY addr;

/*(b) with in/not in */
	SELECT addr FROM Depot WHERE dep_id IN(SELECT dep_id FROM Stock WHERE prod_id='p1')GROUP BY addr;

/*(c) with exists/not exists */
	SELECT  addr FROM Depot WHERE EXISTS (SELECT * FROM Stock WHERE prod_id = 'p1')GROUP BY addr;

/*5. #prods whose price is between $250.00 and $400.00. */
/*(a) using intersect. */
	(SELECT prod_id FROM Product WHERE price >=250.00) INTERSECT ( SELECT prod_id FROM Product WHERE price <= 400.00);

/*(b) without intersect. */
	SELECT prod_id FROM Product WHERE price >=250.00 AND price <=400.00;

/*6. How many products are out of stock? */
	SELECT COUNT(*) FROM Stock WHERE quantity<=0;

/*7. Average of the prices of the products stocked in the ”d2” depot. */
	SELECT AVG(price) FROM Product, Stock  WHERE Product.prod_id = Stock.prod_id AND dep_id = 'd2';

/*8. #deps of the depot(s) with the largest capacity (volume). */
 	SELECT dep_id FROM Depot WHERE volume IN(SELECT MAX(volume) FROM Depot);

/*9. Sum of the stocked quantity of each product. */
	SELECT prod_id,SUM(quantity) FROM Stock GROUP BY prod_id;

/*10. Products names stocked in at least 3 depots. */
/*(a) using count */
	SELECT Product.pname FROM Product WHERE prod_id IN(SELECT prod_id  FROM Stock  GROUP BY prod_id HAVing COUNT(dep_id)>='3');
/*(b) without using count */
	SELECT Product.pname FROM Product,Stock s1,Stock s2,Stock s3  
	where product.prod_id=s1.prod_id AND product.prod_id=s2.prod_id AND product.prod_id=s3.prod_id 
	AND S1.dep_id <> S2.dep_id AND S1.dep_id <> S3.dep_id AND S2.dep_id <> S3.dep_id GROUP BY Product.pname;
/*11. #prod stocked in all depots. */
/*(a) using count */
	SELECT prod_id FROM Stock GROUP BY prod_id HAVING COUNT(Stock.dep_id) IN (SELECT COUNT(Depot.dep_id) FROM Depot);

/*(b) using exists/not exists */
  SELECT Stock.prod_id FROM Stock GROUP BY Stock.prod_id HAVING EXISTS((SELECT COUNT(Depot.dep_id)  
	FROM Depot HAVING COUNT(Stock.dep_id) = COUNT(Depot.dep_id)));











