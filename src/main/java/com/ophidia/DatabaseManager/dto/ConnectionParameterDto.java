package com.ophidia.DatabaseManager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionParameterDto {
    private String url;
    private String username;
    private String password;
    private String driver;
    private String persistenceUnitName;
}
