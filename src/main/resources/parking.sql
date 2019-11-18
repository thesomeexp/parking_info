DROP DATABASE IF EXISTS parking;
CREATE DATABASE parking DEFAULT CHARACTER SET utf8;
use parking;

CREATE TABLE user (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(64) NOT NULL,
  password varchar(64) NOT NULL,
  salt varchar(64) NOT NULL,
  phone varchar(64) NOT NULL UNIQUE,
  role varchar(64) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE info (
  id int(11) NOT NULL AUTO_INCREMENT,
  name varchar(64) NOT NULL,
  content varchar(4000) DEFAULT NULL,
  longitude double NOT NULL,
  latitude double NOT NULL,
  geohash varchar(64) NOT NULL,
  uid int(11) NOT NULL,
  state varchar(64) NOT NULL,
  infoSubmitDate DATETIME NOT NULL,
  stateUpdateDate DATETIME NOT NULL,
  t0 int NOT NULL,
  t1 int NOT NULL,
  t2 int NOT NULL,
  t3 int NOT NULL,
  t4 int NOT NULL,
  t5 int NOT NULL,
  t6 int NOT NULL,
  t7 int NOT NULL,
  t8 int NOT NULL,
  t9 int NOT NULL,
  t10 int NOT NULL,
  t11 int NOT NULL,
  t12 int NOT NULL,
  t13 int NOT NULL,
  t14 int NOT NULL,
  t15 int NOT NULL,
  t16 int NOT NULL,
  t17 int NOT NULL,
  t18 int NOT NULL,
  t19 int NOT NULL,
  t20 int NOT NULL,
  t21 int NOT NULL,
  t22 int NOT NULL,
  t23 int NOT NULL,
  CONSTRAINT fk_info_user FOREIGN KEY (uid) REFERENCES user (id),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE review (
  id int(11) NOT NULL AUTO_INCREMENT,
  uid int(11) NOT NULL,
  pid int(11) NOT NULL,
  submitDate  timestamp NOT NULL,
  accuracy int(11) NOT NULL,
  easyToFind int(11) NOT NULL,
  content varchar(4000) NOT NULL,
  CONSTRAINT fk_review_user FOREIGN KEY (uid) REFERENCES user (id),
  CONSTRAINT fk_review_info FOREIGN KEY (pid) REFERENCES info (id),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX isExistPidAndUidIndex ON review (pid, uid);

CREATE TABLE temp (
  id int(11) NOT NULL AUTO_INCREMENT,
  uid int(11) NOT NULL,
  pid int(11) NOT NULL,
  submitDate  timestamp NOT NULL,
  state int NOT NULL,
  CONSTRAINT fk_temp_user FOREIGN KEY (uid) REFERENCES user (id),
  CONSTRAINT fk_temp_info FOREIGN KEY (pid) REFERENCES info (id),
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX tempTimeIndex ON temp (submitDate);

CREATE TABLE infoImage (
  id int(11) NOT NULL AUTO_INCREMENT,
  pid int(11) NOT NULL,
  uid int(11) NOT NULL,
  submitDate  timestamp NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_image_info FOREIGN KEY (pid) REFERENCES info (id),
  CONSTRAINT fk_image_user FOREIGN KEY (uid) REFERENCES user (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;






