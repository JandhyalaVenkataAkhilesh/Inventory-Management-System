package com.example.model;

import com.infosys.inventory.model.Product;
import org.junit.Assert;
import org.junit.Test;

public class ProductValidationTest {

    @Test
    public void testValidProduct(){
        Product p = new Product(1,"keyboard",10,1200.50,"Electronics");
        Assert.assertEquals(1,p.getProductId());
        Assert.assertEquals("keyboard",p.getProductName());
        Assert.assertEquals(10,p.getQuantity());
        Assert.assertEquals(1200.50,p.getPrice(),0.0);
        Assert.assertEquals("Electronics",p.getCategory());
    }

    @Test
    public void testInvalidProductId(){
        Exception ex = Assert.assertThrows(IllegalArgumentException.class,
                () -> new Product(-1,"keyboard",5,300,"Electronics"));
        Assert.assertEquals("❌ Product Id should be positive number", ex.getMessage());
    }

    @Test
    public void testInvalidProductName(){
        Exception ex = Assert.assertThrows(IllegalArgumentException.class,
                () -> new Product(1,"",5,300,"Electronics"));
        Assert.assertEquals("❌ Product Name should no empty", ex.getMessage());
    }

    @Test
    public void testInvalidProductQuantity(){
        Exception ex = Assert.assertThrows(IllegalArgumentException.class,
                () -> new Product(1,"keyboard",-5,300,"Electronics"));
        Assert.assertEquals("❌ Quantity should be positive number", ex.getMessage());
    }

    @Test
    public void testInvalidProductPrice(){
        Exception ex = Assert.assertThrows(IllegalArgumentException.class,
                () -> new Product(1,"keyboard",5,-300,"Electronics"));
        Assert.assertEquals("❌ price should be positive number", ex.getMessage());
    }

    @Test
    public void testInvalidProductCategory(){
        Exception ex = Assert.assertThrows(IllegalArgumentException.class,
                () -> new Product(1,"keyboard",5,300,""));
        Assert.assertEquals("❌ Product Category should no empty", ex.getMessage());
    }

}
