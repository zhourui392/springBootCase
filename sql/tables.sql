CREATE DATABASE IF NOT EXISTS teleus_user default charset utf8 COLLATE utf8_general_ci;

drop table if exists t_user;
/*用户*/
CREATE TABLE t_user(
  id INT NOT NULL AUTO_INCREMENT,
  showName VARCHAR(50) COMMENT '登录名',
  username VARCHAR(100) COMMENT '用户名，唯一',
  password VARCHAR(50) COMMENT '登录密码',
  status TINYINT DEFAULT '0' COMMENT '1:删除,0:未删除',
  createdtime TIMESTAMP DEFAULT current_timestamp COMMENT '',
  updatedtime TIMESTAMP ON UPDATE current_timestamp COMMENT'' ,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
