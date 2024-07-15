package com.ophidia.DatabaseManager.controller;

import com.ophidia.DatabaseManager.ServiceFacade;
import com.ophidia.DatabaseManager.dto.ConnectionParameterDto;
import com.ophidia.DatabaseManager.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/api/db")
public class ManagerController {


    private ManagerService managerService;

    public ManagerController() {
        managerService = ServiceFacade.getInstance().getManagerService();
    }

    @PostMapping("/connect")
    public void connect(@RequestBody ConnectionParameterDto connectionParamterDto) throws SQLException {
        managerService.connect(connectionParamterDto);
    }

    @GetMapping("/allDbTables")
    public ResponseEntity<?> allDbTables(@RequestParam String tableName) {
        ResponseEntity<?> response = managerService.allDbTables(tableName);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getBody());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }

    @GetMapping("/getDynamicTables")
    public ResponseEntity<?> getDynamicTables(@RequestParam String tableName) {
        ResponseEntity<?> response = managerService.getDynamicTables(tableName);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getBody());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }

    @GetMapping("/allDbTableNames")
    public ResponseEntity<?> allDbTableNames() {
        ResponseEntity<?> response = managerService.allDbTableNames();
        if (!response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getBody());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestParam String tableName, @RequestParam String id, @RequestBody Map<String, Object>  data) {
        ResponseEntity<?> response = managerService.updateEntity(tableName, id, data);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getBody());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Entity updated");
    }
}