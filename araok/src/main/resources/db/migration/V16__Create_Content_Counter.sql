DROP TABLE IF EXISTS CONTENT_COUNTER;
CREATE TABLE CONTENT_COUNTER
(
    ID SERIAL PRIMARY KEY NOT NULL,
    CONTENT_ID BIGINT,
    USER_ID BIGINT,
    COUNT BIGINT,
    CONSTRAINT FK_CONTENT_COUNTER_CONTENT FOREIGN KEY (CONTENT_ID) REFERENCES CONTENT (ID),
    CONSTRAINT FK_CONTENT_COUNTER_USERS FOREIGN KEY (USER_ID) REFERENCES USERS (ID)
);