-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: car_rental
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) NOT NULL COMMENT 'имя',
  `patronymic` varchar(100) DEFAULT NULL COMMENT 'отчество',
  `last_name` varchar(100) NOT NULL COMMENT 'фамилия',
  `phone` varchar(45) NOT NULL COMMENT 'номер телефона\n',
  `birthday` date NOT NULL COMMENT 'дата рождения',
  `passport_series` int(11) NOT NULL COMMENT 'серия паспорта',
  `passport_id` int(11) NOT NULL COMMENT 'номер паспорта',
  `password` varchar(100) NOT NULL COMMENT 'пароль',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Клиенты';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (1,'Иван_ред','Васильевич_ред','Егоров_ред','+7(999)9923265','1988-07-19',1234,123456,'81DC9BDB52D04DC20036DBD8313ED055'),(2,'Олег','Александрович','Самойлов','+7(992)9541257','1995-01-18',6547,952147,'81DC9BDB52D04DC20036DBD8313ED055'),(3,'Юлия','Андреевна','Колиесова','+7(995)6542121','1989-05-12',6541,325741,'81DC9BDB52D04DC20036DBD8313ED055'),(4,'Петр','Яковлевич','Янковский','+7(992)3516541','1970-07-31',6541,258412,'81DC9BDB52D04DC20036DBD8313ED055'),(5,'Иван','Петрович','Рогов','+7(995)6542142','1990-05-23',6542,456321,'81DC9BDB52D04DC20036DBD8313ED055');
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-04 20:23:50
