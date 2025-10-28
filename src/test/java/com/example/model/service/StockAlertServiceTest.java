package com.example.model.service;

import com.infosys.inventory.dao.ProductDao;
import com.infosys.inventory.model.Product;
import com.infosys.inventory.service.EmailService;
import com.infosys.inventory.service.StockAlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

class StockAlertServiceTest {

    private StockAlertService stockAlertService;
    private ProductDao mockProductDao;

    @BeforeEach
    void setUp() {
        stockAlertService = new StockAlertService();
        mockProductDao = mock(ProductDao.class);
        stockAlertService.dao = mockProductDao;
    }

    @Test
    void testCheckAlertService_WithLowStock_ShouldSendEmail() {
        Product lowStockProduct = new Product();
        lowStockProduct.setProductName("Test Product");
        lowStockProduct.setQuantity(10);
        lowStockProduct.setThreshold(20);

        ArrayList<Product> mockList = new ArrayList<>();
        mockList.add(lowStockProduct);

        when(mockProductDao.getAllProducts()).thenReturn(mockList);

        try (MockedStatic<EmailService> emailMock = Mockito.mockStatic(EmailService.class)) {
            stockAlertService.checkAlertService("test@example.com", "John");

            emailMock.verify(() -> EmailService.sendAlert(
                    eq("test@example.com"),
                    eq("Low Stock Alert Summary"),
                    contains("⚠️ Low Stock Alert for the following products"),
                    eq("John")
            ), times(1));
        }
    }


    @Test
    void testCheckAlertService_WithSufficientStock_ShouldNotSendEmail() {
        // Arrange
        Product sufficientStockProduct = new Product();
        sufficientStockProduct.setProductName("Good Product");
        sufficientStockProduct.setQuantity(100);
        sufficientStockProduct.setThreshold(50);

        ArrayList<Product> mockList = new ArrayList<>();
        mockList.add(sufficientStockProduct);

        when(mockProductDao.getAllProducts()).thenReturn(mockList);

        try (MockedStatic<EmailService> emailMock = Mockito.mockStatic(EmailService.class)) {
            stockAlertService.checkAlertService("test@example.com", "John");
            emailMock.verifyNoInteractions();
        }
    }

    @Test
    void testCheckAlertService_WhenExceptionThrown_ShouldHandleGracefully() {

        when(mockProductDao.getAllProducts()).thenThrow(new RuntimeException("DB error"));
        stockAlertService.checkAlertService("test@example.com", "John");
    }
}