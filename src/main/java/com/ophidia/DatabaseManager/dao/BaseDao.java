package com.ophidia.DatabaseManager.dao;

import com.ophidia.DatabaseManager.ServiceFacade;
import com.ophidia.DatabaseManager.constans.Constans;
import com.ophidia.DatabaseManager.dto.ConnectionParameterDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

public class BaseDao implements IBaseDao {

    private static final String PERSISTENCE_UNIT_NAME = "myPersistenceUnit";


    private EntityManagerFactory FACTORY = null;

    private static final Logger logger = LogManager.getLogger(BaseDao.class);;

    @Override
    public void initialize() {
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", Constans.DATABASE_URL);
        properties.put("javax.persistence.jdbc.user", Constans.DATABASE_USER);
        properties.put("javax.persistence.jdbc.password", Constans.DATABASE_PASSWORD);
        properties.put("javax.persistence.jdbc.driver", Constans.DATABASE_DRIVER);
        FACTORY = Persistence.createEntityManagerFactory(Constans.PERSISTENCE_UNIT_NAME, properties);
        ServiceFacade.getInstance().setFACTORY(FACTORY);
    }

    public BaseDao() {
        logger.info("BaseDao initialized");
    }

    @Override
    public EntityManager openConnection() {
        if (ServiceFacade.getInstance().getFACTORY() == null) {
            throw new RuntimeException("The EntityManagerFactory is null.  This must be passed in to the constructor or set using the setEntityManagerFactory() method.");
        }

        EntityManager em = ServiceFacade.getInstance().getFACTORY().createEntityManager();
        if (em != null) {
            return em;
        }
        return null;
    }



    @Override
    public void closeConnection(EntityManager em) {
        try {
            if (em != null && em.isOpen()) {
                rollbackTransaction(em);
                em.close();
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }

    }

    @Override
    public EntityManager openTransactionalConnection() {
        EntityManager em = openConnection();
        if (em != null && em.isOpen()) {
            em.getTransaction().begin();
            return em;
        }
        return null;
    }
    @Override
    public void startTransaction(EntityManager em) {
        if (em != null && em.isOpen() && !em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    @Override
    public void connect(ConnectionParameterDto connectionParamterDto) {
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", connectionParamterDto.getUrl());
        properties.put("javax.persistence.jdbc.user", connectionParamterDto.getUsername());
        properties.put("javax.persistence.jdbc.password", connectionParamterDto.getPassword());
        properties.put("javax.persistence.jdbc.driver", Constans.DATABASE_DRIVER);
        FACTORY = Persistence.createEntityManagerFactory(Constans.PERSISTENCE_UNIT_NAME, properties);
        ServiceFacade.getInstance().setFACTORY(FACTORY);
    }

    @Override
    public void commitTransaction(EntityManager em) {
        if (em != null && em.isOpen() && em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }

    }
    @Override
    public void rollbackTransaction(EntityManager em) {
        try {
            if (em != null && em.isOpen() && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    @Override
    public String hibernateClobConverterToString(Object object) throws Exception {
        /*
         * DB2 have some issue Solution : db2set DB2_RESTRICT_DDF=true
         * http://www.ibm.com/developerworks/forums/thread.jspa?threadID=268395
         */
        if (object != null) {
            java.sql.Clob clob;
            clob = (java.sql.Clob) object;
            if ((int) clob.length() > 0) {
                return clob.getSubString(1, (int) clob.length());
            }
        }
        return "";
    }

}
