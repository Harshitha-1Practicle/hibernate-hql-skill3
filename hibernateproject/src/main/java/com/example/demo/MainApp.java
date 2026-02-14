package com.example.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.example.entity.Product;
import com.example.util.HibernateUtil;

import java.util.List;

public class MainApp {

    public static void main(String[] args){

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session session = factory.openSession();

        // ===============================
        // 1️⃣ INSERT SAMPLE DATA
        // ===============================
        Transaction tx = session.beginTransaction();

        session.save(new Product("Laptop", 85000.0, 10, "Electronics"));
        session.save(new Product("Mouse", 500.0, 50, "Electronics"));
        session.save(new Product("Keyboard", 1500.0, 30, "Electronics"));
        session.save(new Product("Chair", 3000.0, 15, "Furniture"));
        session.save(new Product("Table", 7000.0, 5, "Furniture"));
        session.save(new Product("Pen", 20.0, 200, "Stationery"));
        session.save(new Product("Notebook", 100.0, 100, "Stationery"));

        tx.commit();
        System.out.println("Data Inserted Successfully\n");

        // ===============================
        // 2️⃣ FETCH ALL PRODUCTS
        // ===============================
        System.out.println("All Products:");
        List<Product> all = session.createQuery("FROM Product", Product.class).list();
        all.forEach(p -> System.out.println(p.getId() + " " + p.getName() + " " + p.getPrice()));

        // ===============================
        // 3️⃣ SORTING (High to Low Price)
        // ===============================
        System.out.println("\nSorted By Price DESC:");
        List<Product> sorted = session.createQuery(
                "FROM Product ORDER BY price DESC", Product.class).list();
        sorted.forEach(p -> System.out.println(p.getName() + " " + p.getPrice()));

        // ===============================
        // 4️⃣ FILTERING (Price > 1000)
        // ===============================
        System.out.println("\nProducts Price > 1000:");
        List<Product> filtered = session.createQuery(
                "FROM Product WHERE price > 1000", Product.class).list();
        filtered.forEach(p -> System.out.println(p.getName() + " " + p.getPrice()));

        // ===============================
        // 5️⃣ PAGINATION (First 3 Records)
        // ===============================
        System.out.println("\nPagination (First 3 Records):");
        List<Product> page = session.createQuery(
                "FROM Product", Product.class)
                .setFirstResult(0)
                .setMaxResults(3)
                .list();
        page.forEach(p -> System.out.println(p.getName()));

        // ===============================
        // 6️⃣ COUNT TOTAL PRODUCTS
        // ===============================
        Long count = session.createQuery(
                "SELECT COUNT(p) FROM Product p", Long.class)
                .uniqueResult();
        System.out.println("\nTotal Products: " + count);

        // ===============================
        // 7️⃣ MIN & MAX PRICE
        // ===============================
        Double minPrice = session.createQuery(
                "SELECT MIN(p.price) FROM Product p", Double.class)
                .uniqueResult();

        Double maxPrice = session.createQuery(
                "SELECT MAX(p.price) FROM Product p", Double.class)
                .uniqueResult();

        System.out.println("\nMinimum Price: " + minPrice);
        System.out.println("Maximum Price: " + maxPrice);

        // ===============================
        // 8️⃣ GROUP BY CATEGORY
        // ===============================
        System.out.println("\nGroup By Description (Category Count):");

        List<Object[]> group = session.createQuery(
                "SELECT p.description, COUNT(p) FROM Product p GROUP BY p.description",
                Object[].class).list();

        for(Object[] row : group){
            System.out.println("Category: " + row[0] + " Count: " + row[1]);
        }

        session.close();
        factory.close();
    }
}



