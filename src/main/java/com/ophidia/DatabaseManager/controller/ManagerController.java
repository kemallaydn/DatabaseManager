package com.ophidia.DatabaseManager.controller;

import com.ophidia.DatabaseManager.ServiceFacade;
import com.ophidia.DatabaseManager.constans.Constans;
import com.ophidia.DatabaseManager.dao.IBaseDao;
import com.ophidia.DatabaseManager.dto.ConnectionParameterDto;
import com.ophidia.DatabaseManager.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/db")
public class ManagerController {


    private IBaseDao baseDao;

    private ManagerService managerService;

    public ManagerController() {
        System.out.println("Manager created");
        baseDao = ServiceFacade.getInstance().getBaseDao();
        managerService = ServiceFacade.getInstance().getManagerService();
    }

    @PostMapping("/connect")
    public void connect(@RequestBody ConnectionParameterDto connectionParamterDto) {
        managerService.connect(connectionParamterDto);
    }

    @GetMapping("/allDbTables")
    public ResponseEntity<?> allDbTables(@RequestParam String tableName) {
        ResponseEntity<?> response = managerService.allDbTables(tableName);
        if (!response.getStatusCode().is2xxSuccessful()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getBody());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }

    @GetMapping("/getDynamicTables")
    public ResponseEntity<?> getDynamicTables(@RequestParam String tableName) {
        ResponseEntity<?> response = managerService.getDynamicTables(tableName);
        if (!response.getStatusCode().is2xxSuccessful()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getBody());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }

    @GetMapping("/allDbTableNames")
    public ResponseEntity<?> allDbTableNames() {
        ResponseEntity<?> response = managerService.allDbTableNames();
        if (!response.getStatusCode().is2xxSuccessful()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getBody());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
    }

}
