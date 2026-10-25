-- 以 MySQL 管理员身份执行；请先把 change-me 改成自己的密码。
CREATE DATABASE IF NOT EXISTS maintenance
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

CREATE USER IF NOT EXISTS 'maintenance'@'localhost'
  IDENTIFIED BY 'change-me';

GRANT ALL PRIVILEGES ON maintenance.* TO 'maintenance'@'localhost';
FLUSH PRIVILEGES;
