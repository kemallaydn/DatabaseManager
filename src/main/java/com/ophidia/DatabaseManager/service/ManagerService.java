package com.ophidia.DatabaseManager.service;

import com.ophidia.DatabaseManager.ServiceFacade;
import com.ophidia.DatabaseManager.dao.BaseDao;
import com.ophidia.DatabaseManager.dao.IBaseDao;
import com.ophidia.DatabaseManager.dto.ConnectionParameterDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerService extends BaseDao {


    private IBaseDao baseDao = null;

    public ManagerService() {
        baseDao = ServiceFacade.getInstance().getBaseDao();
    }

    public void connect(ConnectionParameterDto connectionParamterDto) {
        baseDao.connect(connectionParamterDto);
    }

    public ResponseEntity<?> allDbTables(String tableName) {
        EntityManager   em = null;
        try {
            em=openConnection();
            String sqlQuery = "SELECT * FROM \"" + tableName+"\"";
            Query query = em.createNativeQuery(sqlQuery);

            List<Object> resultList = query.getResultList();
            return ResponseEntity.status(200).body(resultList);
        } catch (Exception ex) {
            return ResponseEntity.status(400).body("Table columns could not be fetched");
        }
    }

    public ResponseEntity<?> getDynamicTables(String tableName) {
        EntityManager   em = null;
        try {
            em=openTransactionalConnection();
            Query query = em.createNativeQuery("SELECT column_name FROM information_schema.columns WHERE table_name = ?");
            query.setParameter(1, tableName);
            List<String> columns = query.getResultList();
            return ResponseEntity.ok(columns);
        } catch (Exception ex) {
            return ResponseEntity.status(400).body("Table columns could not be fetched");
        }
    }

    public ResponseEntity<?> allDbTableNames() {
        EntityManager   em = null;
        try {
            em=openTransactionalConnection();
            Query query = em.createNativeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'");
            List<String> columns = query.getResultList();
            return ResponseEntity.ok(columns);
        } catch (Exception ex) {
            return ResponseEntity.status(400).body("Table columns could not be fetched");
        }
    }


}
