package com.ophidia.DatabaseManager.service;

import com.ophidia.DatabaseManager.ServiceFacade;
import com.ophidia.DatabaseManager.constans.Constans;
import com.ophidia.DatabaseManager.dao.BaseDao;
import com.ophidia.DatabaseManager.dao.IBaseDao;
import com.ophidia.DatabaseManager.dto.ConnectionParameterDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.http.ResponseEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class ManagerService extends BaseDao {

    private IBaseDao baseDao = null;

    public ManagerService() {
        baseDao = ServiceFacade.getInstance().getBaseDao();
    }

    public void connect(ConnectionParameterDto connectionParamterDto) {
        Constans.DATABASE_URL = connectionParamterDto.getUrl();
        Constans.DATABASE_USER = connectionParamterDto.getUsername();
        Constans.DATABASE_PASSWORD = connectionParamterDto.getPassword();
        baseDao.connect(connectionParamterDto);
    }

    public ResponseEntity<?> allDbTables(String tableName) {
        EntityManager em = null;
        try {
            em = openConnection();
            String sqlQuery = "SELECT * FROM \"" + tableName + "\"";
            Query query = em.createNativeQuery(sqlQuery);

            List<Object> resultList = query.getResultList();


            return ResponseEntity.status(200).body(resultList);
        } catch (Exception ex) {
            return ResponseEntity.status(400).body("Table columns could not be fetched");
        }
    }

    public ResponseEntity<?> getDynamicTables(String tableName) {
        EntityManager em = null;
        try {
            em = openTransactionalConnection();
            Query query = em.createNativeQuery("SELECT column_name FROM information_schema.columns WHERE table_name = ?");
            query.setParameter(1, tableName);
            List<String> columns = query.getResultList();
            return ResponseEntity.ok(columns);
        } catch (Exception ex) {
            return ResponseEntity.status(400).body("Table columns could not be fetched");
        }
    }

    public ResponseEntity<?> allDbTableNames() {
        EntityManager em = null;
        try {
            em = openTransactionalConnection();
            Query query = em.createNativeQuery("SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'");
            List<String> columns = query.getResultList();
            return ResponseEntity.ok(columns);
        } catch (Exception ex) {
            return ResponseEntity.status(400).body("Table columns could not be fetched");
        }
    }

    public ResponseEntity<?> updateEntity(String tableName, String id, Map<String, Object> updates) {
        try (Connection conn = DriverManager.getConnection(Constans.DATABASE_URL, Constans.DATABASE_USER, Constans.DATABASE_PASSWORD);) {

            StringBuilder sqlBuilder = new StringBuilder();

            sqlBuilder.append("UPDATE \"").append(tableName).append("\" SET ");

            int index = 1;
            for (String key : updates.keySet()) {
                if (!key.equals("id")) {
                    if (index > 1) {
                        sqlBuilder.append(", ");
                    }
                    sqlBuilder.append(key).append(" = ?");
                    index++;
                }
            }

            sqlBuilder.append(" WHERE id = ?");

            try (PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {
                index = 1;
                for (String key : updates.keySet()) {
                    if (!key.equals("id")) {
                        pstmt.setObject(index, updates.get(key));
                        index++;
                    }
                }
                pstmt.setObject(index, UUID.fromString(id));
                int affectedRows = pstmt.executeUpdate();
                return ResponseEntity.ok("Entity updated successfully");
            } catch (SQLException ex) {
                ex.printStackTrace();
                return ResponseEntity.status(400).body("Error executing update");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500).body("Database connection error");
        }
    }


}