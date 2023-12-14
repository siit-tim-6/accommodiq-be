package com.example.accommodiq.utilities;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HibernateUtil {
    private static final EntityManagerFactory emFactory;
    static {
        emFactory = Persistence.createEntityManagerFactory("accommodiq");
    }
    public static EntityManager getEntityManager(){
        return emFactory.createEntityManager();
    }
}
