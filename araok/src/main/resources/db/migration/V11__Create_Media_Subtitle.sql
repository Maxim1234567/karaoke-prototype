DROP TABLE IF EXISTS MEDIA_SUBTITLE;
CREATE TABLE MEDIA_SUBTITLE
(
    ID SERIAL PRIMARY KEY NOT NULL,
    CONTENT_ID BIGINT,
    LANGUAGE_ID BIGINT,
    CONSTRAINT UQ_MEDIA_SUBTITLE UNIQUE (CONTENT_ID, LANGUAGE_ID),
    CONSTRAINT FK_LANGUAGE FOREIGN KEY (LANGUAGE_ID) REFERENCES LANGUAGES (ID),
    CONSTRAINT FK_MEDIA_SUBTITLE_CONTENT FOREIGN KEY (CONTENT_ID) REFERENCES CONTENT (ID)
);