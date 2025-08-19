/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tests;

/**
 *
 * @author ingri
 */

import domain.Cliente;
import domain.Producto;
import infrastructure.security.CipherService;
import infrastructure.security.HMACUtil;
import application.service.ProductoService;
import infrastructure.repository.ProductoRepositorioCSV;
import application.exception.PrecioInvalidoException;
import application.exception.StockNegativoException;
import application.exception.ProductoDuplicadoException;
import java.math.BigDecimal;
import java.util.List;

public class MiniTestRunner {

    public static void assertEquals(Object expected, Object actual, String message) {
        if (!expected.equals(actual)) {
            System.err.println("FAIL: " + message + " | Expected: " + expected + ", Actual: " + actual);
        } else {
            System.out.println("PASS: " + message);
        }
    }

    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            System.err.println("FAIL: " + message);
        } else {
            System.out.println("PASS: " + message);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Iniciando Pruebas ===");

        // 1. Prueba de cifrado/descifrado
        try {
            CipherService cipher = new CipherService();
            String original = "1-2345-6789";
            byte[] encrypted = cipher.encrypt(original);
            String decrypted = cipher.decrypt(encrypted);
            assertEquals(original, decrypted, "Cifrado/Descifrado de cédula");
        } catch (Exception e) {
            System.err.println("FAIL: Cifrado/Descifrado de cédula - " + e.getMessage());
        }

        // 2. Prueba de HMAC
        try {
            String data = "Datos de prueba para HMAC";
            String hmac = HMACUtil.generateHMAC(data);
            assertTrue(hmac != null && !hmac.isEmpty(), "Generación de HMAC");
        } catch (Exception e) {
            System.err.println("FAIL: Generación de HMAC - " + e.getMessage());
        }

        // 3. Prueba de creación de producto
        try {
            ProductoRepositorioCSV repoProd = new ProductoRepositorioCSV("data/productos_test.csv");
            ProductoService prodService = new ProductoService(repoProd);
            Producto p = new Producto("T001", "Producto Test", "Test", new BigDecimal("100.00"), 5, 10);
            prodService.crearProducto(p);
            List<Producto> productos = prodService.listarTodos();
            assertTrue(productos.stream().anyMatch(prod -> "T001".equals(prod.getCodigo())), "Creación de producto");
        } catch (Exception e) {
            System.err.println("FAIL: Creación de producto - " + e.getMessage());
        }

        // 4. Prueba de validación de precio negativo
        try {
            ProductoRepositorioCSV repoProd = new ProductoRepositorioCSV("data/productos_test.csv");
            ProductoService prodService = new ProductoService(repoProd);
            Producto p = new Producto("T002", "Producto Test Neg", "Test", new BigDecimal("-100.00"), 5, 10);
            prodService.crearProducto(p);
            System.err.println("FAIL: Validación de precio negativo - No se lanzó excepción");
        } catch (PrecioInvalidoException e) {
            System.out.println("PASS: Validación de precio negativo");
        } catch (Exception e) {
            System.err.println("FAIL: Validación de precio negativo - " + e.getMessage());
        }

        // 5. Prueba de validación de stock negativo
        try {
            ProductoRepositorioCSV repoProd = new ProductoRepositorioCSV("data/productos_test.csv");
            ProductoService prodService = new ProductoService(repoProd);
            Producto p = new Producto("T003", "Producto Test Stock", "Test", new BigDecimal("100.00"), -5, 10);
            prodService.crearProducto(p);
            System.err.println("FAIL: Validación de stock negativo - No se lanzó excepción");
        } catch (StockNegativoException e) {
            System.out.println("PASS: Validación de stock negativo");
        } catch (Exception e) {
            System.err.println("FAIL: Validación de stock negativo - " + e.getMessage());
        }

        // 6. Prueba de detección de duplicados
        try {
            ProductoRepositorioCSV repoProd = new ProductoRepositorioCSV("data/productos_test.csv");
            ProductoService prodService = new ProductoService(repoProd);
            Producto p1 = new Producto("T004", "Producto Test Dup 1", "Test", new BigDecimal("100.00"), 5, 10);
            Producto p2 = new Producto("T004", "Producto Test Dup 2", "Test", new BigDecimal("200.00"), 5, 10);
            prodService.crearProducto(p1);
            prodService.crearProducto(p2); // Debe lanzar excepción
            System.err.println("FAIL: Detección de duplicados - No se lanzó excepción");
        } catch (ProductoDuplicadoException e) {
            System.out.println("PASS: Detección de duplicados");
        } catch (Exception e) {
            System.err.println("FAIL: Detección de duplicados - " + e.getMessage());
        }

        System.out.println("=== Pruebas Finalizadas ===");
    }
}
