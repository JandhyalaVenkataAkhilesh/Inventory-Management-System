-- 1. Show all products in inventory
select * from products;

--2. Show only product names and categories
select product_name,product_category from products;

--3. Find Products where quantity is more than 10
select * from products where product_quantity>10;

--4. Find products where price is less than 5000
select * from products where product_price<5000;

--5. Show all electronics products.
select * from products where product_category = 'Electronics';

--6. show all products sorted by price.
SELECT * FROM products ORDER BY product_price ASC;

--7. show top 3 most expensive products.
SELECT * FROM products ORDER BY product_price DESC LIMIT 3;

--8. Find the total number of products(sum of quantity).
select sum(product_quantity) as Total_Products from products;

--9. Find avg price of products.
select avg(product_price) as Average_Price from products;

--10. Find the highest price products.
select max(product_price) as Highest_Price from products;