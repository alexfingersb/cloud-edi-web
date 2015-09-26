-- Generated by Oracle SQL Developer Data Modeler 4.0.3.853
--   at:        2015-09-23 15:20:59 BRT
--   site:      Oracle Database 11g
--   type:      Oracle Database 11g




CREATE
  TABLE AGENT_DOWNLOAD
  (
    ID           NUMBER NOT NULL ,
    HOST_ADDRESS VARCHAR2 (30) ,
    USER_ID      NUMBER NOT NULL
  ) ;
CREATE UNIQUE INDEX AGENT_DOWNLOAD_ID_IDX ON AGENT_DOWNLOAD
  (
    ID ASC
  )
  ;
  CREATE
    INDEX AGENT_DOWNLOAD_USER_ID_IDX ON AGENT_DOWNLOAD
    (
      USER_ID ASC
    ) ;
  ALTER TABLE AGENT_DOWNLOAD ADD CONSTRAINT AGENT_DOWNLOAD_PK PRIMARY KEY ( ID
  ) ;

CREATE
  TABLE MESSAGE_LOG
  (
    ID            NUMBER NOT NULL ,
    FILE_NAME     VARCHAR2 (300) ,
    FILE_LENGTH   NUMBER ,
    FILE_DATE     TIMESTAMP ,
    SENT_DATE     TIMESTAMP ,
    RECEIVED_DATE TIMESTAMP ,
    ROUTE_TO_ID   NUMBER NOT NULL ,
    ROUTE_FROM_ID NUMBER NOT NULL
  ) ;
CREATE
  INDEX MESSAGE_LOG_FILENAME_IDX ON MESSAGE_LOG
  (
    FILE_NAME ASC
  ) ;
CREATE
  INDEX MESSAGE_LOG_RECEIVEDDATE_IDX ON MESSAGE_LOG
  (
    RECEIVED_DATE ASC
  ) ;
CREATE
  INDEX MESSAGE_LOG_SENTDATE_IDX ON MESSAGE_LOG
  (
    SENT_DATE ASC
  ) ;
CREATE UNIQUE INDEX MESSAGE_LOG_ID_IDX ON MESSAGE_LOG
  (
    ID ASC
  )
  ;
  CREATE
    INDEX MESSAGE_LOG_ROUTE_TO_ID_IDX ON MESSAGE_LOG
    (
      ROUTE_TO_ID ASC
    ) ;
  CREATE
    INDEX MESSAGE_LOG_ROUTE_FROM_ID_IDX ON MESSAGE_LOG
    (
      ROUTE_FROM_ID ASC
    ) ;
  ALTER TABLE MESSAGE_LOG ADD CONSTRAINT MESSAGE_LOG_PK PRIMARY KEY ( ID ) ;

CREATE
  TABLE ROUTE
  (
    ID          NUMBER NOT NULL ,
    DESCRIPTION VARCHAR2 (100) NOT NULL
  ) ;
CREATE UNIQUE INDEX ROUTE_ID_IDX ON ROUTE
  (
    ID ASC
  )
  ;
  ALTER TABLE ROUTE ADD CONSTRAINT ROUTE_PK PRIMARY KEY ( ID ) ;

CREATE
  TABLE ROUTE_FROM
  (
    ID           NUMBER NOT NULL ,
    ROUTE_ID     NUMBER NOT NULL ,
    ROUTE_URI_ID NUMBER NOT NULL
  ) ;
CREATE
  INDEX ROUTE_FROM_ROUTE_URI_FK ON ROUTE_FROM
  (
    ROUTE_URI_ID ASC
  ) ;
CREATE UNIQUE INDEX ROUTE_FROM_ID_IDX ON ROUTE_FROM
  (
    ID ASC
  )
  ;
  CREATE
    INDEX ROUTE_FROM_ROUTE_ID_IDX ON ROUTE_FROM
    (
      ROUTE_ID ASC
    ) ;
  ALTER TABLE ROUTE_FROM ADD CONSTRAINT ROUTE_FROM_PK PRIMARY KEY ( ID ) ;

CREATE
  TABLE ROUTE_TO
  (
    ID            NUMBER NOT NULL ,
    CHOICE_WHEN   VARCHAR2 (100) ,
    ROUTE_URI_ID  NUMBER NOT NULL ,
    ROUTE_FROM_ID NUMBER NOT NULL
  ) ;
CREATE UNIQUE INDEX ROUTE_TO_ID_IDX ON ROUTE_TO
  (
    ID ASC
  )
  ;
  CREATE
    INDEX ROUTE_TO_ROUTE_URI_ID_IDX ON ROUTE_TO
    (
      ROUTE_URI_ID ASC
    ) ;
  CREATE
    INDEX ROUTE_TO_ROUTE_FROM_ID_IDX ON ROUTE_TO
    (
      ROUTE_FROM_ID ASC
    ) ;
  ALTER TABLE ROUTE_TO ADD CONSTRAINT ROUTE_TO_PK PRIMARY KEY ( ID ) ;

CREATE
  TABLE ROUTE_URI
  (
    ID           NUMBER NOT NULL ,
    SCHEME       VARCHAR2 (10) NOT NULL ,
    CONTEXT_PATH VARCHAR2 (300) ,
    OPTIONS      VARCHAR2 (300)
  ) ;
CREATE UNIQUE INDEX ROUTE_URI_ID_IDX ON ROUTE_URI
  (
    ID ASC
  )
  ;
  ALTER TABLE ROUTE_URI ADD CONSTRAINT ROUTE_URI_PK PRIMARY KEY ( ID ) ;

CREATE
  TABLE "USER"
  (
    ID       NUMBER NOT NULL ,
    NAME     VARCHAR2 (200) ,
    USERNAME VARCHAR2 (30) NOT NULL ,
    PASSWORD VARCHAR2 (100) NOT NULL ,
    EMAIL    VARCHAR2 (100) ,
    STATUS   VARCHAR2 (3 BYTE) ,
    PROFILE  VARCHAR2 (50)
  ) ;
CREATE UNIQUE INDEX USER_USERNAME_IDX ON "USER"
  (
    USERNAME ASC
  )
  ;
  CREATE
    INDEX USER_NAME_IDX ON "USER"
    (
      NAME ASC
    ) ;
  CREATE
    INDEX USER_EMAIL_IDX ON "USER"
    (
      EMAIL ASC
    ) ;
CREATE UNIQUE INDEX USER_ID_IDX ON "USER"
  (
    ID ASC
  )
  ;
  ALTER TABLE "USER" ADD CONSTRAINT USER_PK PRIMARY KEY ( ID ) ;

CREATE
  TABLE USER_ROUTE
  (
    ID           NUMBER NOT NULL ,
    ROUTE_ID     NUMBER NOT NULL ,
    USER_ID      NUMBER NOT NULL ,
    ROUTE_URI_ID NUMBER NOT NULL
  ) ;
CREATE UNIQUE INDEX USER_ROUTE_ID_IDX ON USER_ROUTE
  (
    ID ASC
  )
  ;
  CREATE
    INDEX USER_ROUTE_ROUTE_ID_IDX ON USER_ROUTE
    (
      ROUTE_ID ASC
    ) ;
  CREATE
    INDEX USER_ROUTE_USER_ID_IDX ON USER_ROUTE
    (
      USER_ID ASC
    ) ;
  CREATE
    INDEX USER_ROUTE_ROUTE_URI_ID_IDX ON USER_ROUTE
    (
      ROUTE_URI_ID ASC
    ) ;
  ALTER TABLE USER_ROUTE ADD CONSTRAINT USER_ROUTE_PK PRIMARY KEY ( ID ) ;

ALTER TABLE AGENT_DOWNLOAD ADD CONSTRAINT AGENT_DOWNLOAD_USER_FK FOREIGN KEY (
USER_ID ) REFERENCES "USER" ( ID ) ;

ALTER TABLE MESSAGE_LOG ADD CONSTRAINT MESSAGE_LOG_ROUTE_FROM_FK FOREIGN KEY (
ROUTE_FROM_ID ) REFERENCES ROUTE_FROM ( ID ) ;

ALTER TABLE MESSAGE_LOG ADD CONSTRAINT MESSAGE_LOG_ROUTE_TO_FK FOREIGN KEY (
ROUTE_TO_ID ) REFERENCES ROUTE_TO ( ID ) ;

ALTER TABLE ROUTE_FROM ADD CONSTRAINT ROUTE_FROM_ROUTE_FK FOREIGN KEY (
ROUTE_ID ) REFERENCES ROUTE ( ID ) ;

ALTER TABLE ROUTE_TO ADD CONSTRAINT ROUTE_FROM_ROUTE_TO_FK FOREIGN KEY (
ROUTE_FROM_ID ) REFERENCES ROUTE_FROM ( ID ) ;

ALTER TABLE ROUTE_FROM ADD CONSTRAINT ROUTE_FROM_ROUTE_URI_FK FOREIGN KEY (
ROUTE_URI_ID ) REFERENCES ROUTE_URI ( ID ) ;

ALTER TABLE ROUTE_TO ADD CONSTRAINT ROUTE_URI_ROUTE_TO_FK FOREIGN KEY (
ROUTE_URI_ID ) REFERENCES ROUTE_URI ( ID ) ;

ALTER TABLE USER_ROUTE ADD CONSTRAINT USER_ROUTE_ROUTE_FK FOREIGN KEY (
ROUTE_ID ) REFERENCES ROUTE ( ID ) ;

ALTER TABLE USER_ROUTE ADD CONSTRAINT USER_ROUTE_ROUTE_URI_FK FOREIGN KEY (
ROUTE_URI_ID ) REFERENCES ROUTE_URI ( ID ) ;

ALTER TABLE USER_ROUTE ADD CONSTRAINT USER_ROUTE_USER_FK FOREIGN KEY ( USER_ID
) REFERENCES "USER" ( ID ) ;

CREATE SEQUENCE SEQ_AGENT_DOWNLOAD_ID START WITH 1 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER TRG_AGENT_DOWNLOAD BEFORE
  INSERT
    ON AGENT_DOWNLOAD FOR EACH ROW WHEN
    (
      NEW.ID IS NULL
    )
    BEGIN :NEW.ID := SEQ_AGENT_DOWNLOAD_ID.NEXTVAL;
END;
/

CREATE SEQUENCE SEQ_MESSAGE_LOG_ID START WITH 1 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER TRG_MESSAGE_LOG BEFORE
  INSERT
    ON MESSAGE_LOG FOR EACH ROW WHEN
    (
      NEW.ID IS NULL
    )
    BEGIN :NEW.ID := SEQ_MESSAGE_LOG_ID.NEXTVAL;
END;
/

CREATE SEQUENCE SEQ_ROUTE_ID START WITH 1 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER TRG_ROUTE BEFORE
  INSERT
    ON ROUTE FOR EACH ROW WHEN
    (
      NEW.ID IS NULL
    )
    BEGIN :NEW.ID := SEQ_ROUTE_ID.NEXTVAL;
END;
/

CREATE SEQUENCE SEQ_ROUTE_FROM_ID START WITH 1 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER TRG_ROUTE_FROM BEFORE
  INSERT
    ON ROUTE_FROM FOR EACH ROW WHEN
    (
      NEW.ID IS NULL
    )
    BEGIN :NEW.ID := SEQ_ROUTE_FROM_ID.NEXTVAL;
END;
/

CREATE SEQUENCE SEQ_ROUTE_TO_ID START WITH 1 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER TRG_ROUTE_TO BEFORE
  INSERT
    ON ROUTE_TO FOR EACH ROW WHEN
    (
      NEW.ID IS NULL
    )
    BEGIN :NEW.ID := SEQ_ROUTE_TO_ID.NEXTVAL;
END;
/

CREATE SEQUENCE SEQ_ROUTE_URI_ID START WITH 1 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER TRG_ROUTE_URI BEFORE
  INSERT
    ON ROUTE_URI FOR EACH ROW WHEN
    (
      NEW.ID IS NULL
    )
    BEGIN :NEW.ID := SEQ_ROUTE_URI_ID.NEXTVAL;
END;
/

CREATE SEQUENCE SEQ_USER_ID START WITH 1 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER TRG_USER BEFORE
  INSERT
    ON "USER" FOR EACH ROW WHEN
    (
      NEW.ID IS NULL
    )
    BEGIN :NEW.ID := SEQ_USER_ID.NEXTVAL;
END;
/

CREATE SEQUENCE SEQ_USER_ROUTE_ID START WITH 1 NOCACHE ORDER ;
CREATE OR REPLACE TRIGGER TRG_USER_ROUTE BEFORE
  INSERT
    ON USER_ROUTE FOR EACH ROW WHEN
    (
      NEW.ID IS NULL
    )
    BEGIN :NEW.ID := SEQ_USER_ROUTE_ID.NEXTVAL;
END;
/


-- Oracle SQL Developer Data Modeler Summary Report: 
-- 
-- CREATE TABLE                             8
-- CREATE INDEX                            24
-- ALTER TABLE                             18
-- CREATE VIEW                              0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           8
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          8
-- CREATE MATERIALIZED VIEW                 0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
