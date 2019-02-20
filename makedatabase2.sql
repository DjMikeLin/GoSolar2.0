CREATE TABLE IF NOT EXISTS Users ( 
	ID 		  	INTEGER 	PRIMARY KEY NOT NULL, /*A column declared INTEGER PRIMARY KEY will autoincrement in sqlite*/
    USERNAME  	TEXT 		NOT NULL,
    PASS 	  	TEXT 		NOT NULL
);

CREATE TABLE IF NOT EXISTS Students (
	STUDENTID	INT 		REFERENCES Users(ID),
    FIRSTNAME 	TEXT 		NOT NULL,
    LASTNAME 	TEXT 		NOT NULL,
    EMAIL 	  	CHAR(10)	NOT NULL,
    CELL 	  	CHAR(10) 	NOT NULL,
    MAILING   	CHAR(50) 	NOT NULL,
    PRIMARY KEY (STUDENTID)
);

CREATE TABLE IF NOT EXISTS Instructor (
	INSTRUCTORID	INT 	REFERENCES Users(ID),
	CLASSES 		TEXT	NOT NULL,
	PRIMARY KEY (INSTRUCTORID)
);

CREATE TABLE IF NOT EXISTS User_Types (
	USERID 		INTEGER 	REFERENCES Users(ID),
    USERTYPE 	CHAR(10) 	NOT NULL
);

CREATE TABLE IF NOT EXISTS Classes (
    IDCLASSES INTEGER PRIMARY KEY NOT NULL,
    CLASSNAME TEXT NOT NULL,
	SIZE INT NOT NULL,
	SPOTSTAKEN INT NOT NULL,
	INSTRUCTOR TEXT NOT NULL,
	CRN INT NOT NULL,
	DAYS TEXT NOT NULL,
	STARTTIME TEXT NOT NULL,
	ENDTIME TEXT NOT NULL,
	AREA TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS INSTRUCTOR_TEACH_CLASSES (
	INTRUCTOR_ID	INT 	REFERENCES Instructor(INSTRUCTORID),
	I_USERNAME 		TEXT	REFERENCES Instructor(IUSERNAME),
	CLASS_ID 	INT 	REFERENCES Classes(IDCLASSES),
    CLASS_NAME	TEXT 	REFERENCES Classes(CLASSNAME)
);

CREATE TABLE IF NOT EXISTS CLASSES_HAVE_STUDENTS(
	CLASS_ID 	INT 	REFERENCES Classes(IDCLASSES),
    CLASS_NAME	TEXT 	REFERENCES Classes(CLASSNAME),
    STUDENT_ID 	INT		REFERENCES Students(STUDENTID),
    S_USERNAME 	TEXT	REFERENCES Students(SUSERNAME)
);

INSERT INTO Users(USERNAME, PASS)
VALUES('admin', '111111');

INSERT INTO User_Types
VALUES(1, 'admin');

INSERT INTO Users(USERNAME, PASS)
VALUES('mikelin55', '222222');

INSERT INTO User_Types
VALUES(2, 'student');

INSERT INTO Students(STUDENTID, FIRSTNAME, LASTNAME, EMAIL, CELL, MAILING)
VALUES(2, 'Michael', 'Lin', 'linmichael97@yahoo.com', '555-625-5525', '4394 Freedom Drive');

INSERT INTO CLASSES (CLASSNAME, SIZE, SPOTSTAKEN, INSTRUCTOR, CRN, DAYS, STARTTIME, ENDTIME, AREA)
VALUES ('CSC1000', 30, 0, 'Nevil Garrett', 93045, 'MW', '12:00:00', '13:00:00', 'Computer Science');

INSERT INTO CLASSES (CLASSNAME, SIZE, SPOTSTAKEN, INSTRUCTOR, CRN, DAYS, STARTTIME, ENDTIME, AREA)
VALUES ('CSC1010', 30, 0, 'Roxana Jemmy', 93046, 'MWF', '09:00:00', '11:00:00', 'Computer Science');

INSERT INTO CLASSES (CLASSNAME, SIZE, SPOTSTAKEN, INSTRUCTOR, CRN, DAYS, STARTTIME, ENDTIME, AREA)
VALUES ('CSC1020', 30, 0, 'Wayland Sadie', 93047, 'MW', '11:10:00', '11:55:00', 'Computer Science');

INSERT INTO CLASSES (CLASSNAME, SIZE, SPOTSTAKEN, INSTRUCTOR, CRN, DAYS, STARTTIME, ENDTIME, AREA)
VALUES ('CSC1030', 30, 0, 'Azure Esther', 93048, 'MW', '08:30:00', '10:00:00', 'Computer Science');

INSERT INTO CLASSES (CLASSNAME, SIZE, SPOTSTAKEN, INSTRUCTOR, CRN, DAYS, STARTTIME, ENDTIME, AREA)
VALUES ('CSC1040', 30, 1, 'Lana Caelie', 93049, 'TR', '15:00:00', '16:00:00', 'Computer Science');

INSERT INTO CLASSES (CLASSNAME, SIZE, SPOTSTAKEN, INSTRUCTOR, CRN, DAYS, STARTTIME, ENDTIME, AREA)
VALUES ('PHY3000', 30, 0, 'Nathaniel Tye', 10451, 'MW', '12:00:00', '13:00:00', 'Physics');

INSERT INTO CLASSES (CLASSNAME, SIZE, SPOTSTAKEN, INSTRUCTOR, CRN, DAYS, STARTTIME, ENDTIME, AREA)
VALUES ('PHY3100', 30, 0, 'Samara Kaylin', 10452, 'MWF', '09:00:00', '11:00:00', 'Physics');

INSERT INTO CLASSES (CLASSNAME, SIZE, SPOTSTAKEN, INSTRUCTOR, CRN, DAYS, STARTTIME, ENDTIME, AREA)
VALUES ('PHY3200', 30, 1, 'Beverly Casey', 10453, 'TR', '12:00:00', '14:00:00', 'Physics');

INSERT INTO CLASSES (CLASSNAME, SIZE, SPOTSTAKEN, INSTRUCTOR, CRN, DAYS, STARTTIME, ENDTIME, AREA)
VALUES ('PHY3300', 30, 0, 'Rene Jeanine', 10454, 'MW', '08:00:00', '10:00:00', 'Physics');

INSERT INTO CLASSES (CLASSNAME, SIZE, SPOTSTAKEN, INSTRUCTOR, CRN, DAYS, STARTTIME, ENDTIME, AREA)
VALUES ('PHY3400', 30, 0, 'Lindsey Biddy', 10455, 'TR', '13:00:00', '14:30:00', 'Physics');

INSERT INTO CLASSES_HAVE_STUDENTS
VALUES(5, 'CSC1040', 2, 'mikelin55');

INSERT INTO CLASSES_HAVE_STUDENTS
VALUES(8, 'PHY3200', 2, 'mikelin55');