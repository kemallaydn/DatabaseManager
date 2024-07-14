package com.ophidia.DatabaseManager.dao;

import com.ophidia.DatabaseManager.dto.ConnectionParameterDto;
import jakarta.persistence.EntityManager;

public interface IBaseDao {

        public abstract EntityManager openTransactionalConnection();

        public abstract void rollbackTransaction(EntityManager em);

        public abstract void closeConnection(EntityManager em);

        public abstract void commitTransaction(EntityManager em);

        public abstract EntityManager openConnection();

        public abstract void initialize();

        public abstract void startTransaction(EntityManager em);

        public abstract void connect(ConnectionParameterDto connectionParamterDto);

        public abstract String  hibernateClobConverterToString(Object object) throws Exception;



}
