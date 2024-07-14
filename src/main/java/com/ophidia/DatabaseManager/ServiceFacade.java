package com.ophidia.DatabaseManager;

import com.ophidia.DatabaseManager.dao.BaseDao;
import com.ophidia.DatabaseManager.dao.IBaseDao;
import com.ophidia.DatabaseManager.service.ManagerService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Getter
@Setter
@Service
public class ServiceFacade {

    private static final Logger logger = LogManager.getLogger(ServiceFacade.class);

    private static ServiceFacade instance = null;

    private EntityManagerFactory FACTORY = null;

    private IBaseDao baseDao=null;

    private ManagerService managerService = null;


    Properties appProps = new Properties();

    public static ServiceFacade getInstance() {
        if (instance == null) {
            instance = new ServiceFacade();
        }
        return instance;
    }

    public void StartServices() {
        String yellow = "\033[0;93m"; // Sarı
        String blue = "\033[0;94m";   // Lacivert
        String reset = "\033[0m";     // Renkleri sıfırlamak için

        String version = "1.0.0";

        // Şu anki zamanı alma ve formatlama
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = now.format(formatter);

        StringBuilder logString = new StringBuilder();
        logString.append(yellow + "================================================" + reset).append("\n");
        logString.append(yellow + " .   ______        ___    ____ ___ _______   __" + reset).append("\n");
        logString.append(blue + "/\\\\ / ___\\ \\      / / \\  |  _ \\_ _|  ___\\ \\ / /" + reset).append("\n");
        logString.append(yellow + "(( )\\___ \\\\ \\ /\\ / / _ \\ | |_) | || |_   \\ V /" + reset).append("\n");
        logString.append(blue + "\\\\/  ___) |\\ V  V / ___ \\|  __/| ||  _|   | | " + reset).append("\n");
        logString.append(yellow + " '  |____/  \\_/\\_/_/   \\_\\_|  |___|_|     |_| " + reset).append("\n");
        logString.append(yellow + "================================================" + reset).append("\n");
        logString.append(blue + ":: SWAPIFY  ::                            v" + version + "              " + reset).append("\n");
        logString.append(blue + ":: Launched ::                          ").append(time).append("              " + reset).append("\n");
        logString.append(yellow + "================================================" + reset).append("\n");

        System.out.println(logString.toString());


        try {
            initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() throws Exception {

        baseDao = new BaseDao();

        managerService = new ManagerService();

    }

}
