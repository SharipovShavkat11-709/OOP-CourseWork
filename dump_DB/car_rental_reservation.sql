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
-- Table structure for table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reservation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_id` int(11) NOT NULL,
  `car_id` int(11) NOT NULL,
  `car_center_receiving_id` int(11) NOT NULL,
  `car_center_return_id` int(11) NOT NULL,
  `date_application` date NOT NULL COMMENT 'дата бронирования',
  `date_receiving` date NOT NULL COMMENT 'дата получения',
  `date_return` date NOT NULL COMMENT 'дата возврата',
  `status_reservation` int(11) NOT NULL COMMENT 'статус бронирования\n1 - бронь\n2 - обработан\n3 отказ',
  `discount_percent` float NOT NULL COMMENT 'размер скидки',
  `payment` float NOT NULL COMMENT 'размер платежа',
  PRIMARY KEY (`id`),
  KEY `reservation_client_idx` (`client_id`),
  KEY `reservation_car_idx` (`car_id`),
  KEY `reservation_receiving_car_center_idx` (`car_center_receiving_id`),
  KEY `reservation_return_car_center_idx` (`car_center_return_id`),
  CONSTRAINT `reservation_car` FOREIGN KEY (`car_id`) REFERENCES `car` (`id`),
  CONSTRAINT `reservation_client` FOREIGN KEY (`client_id`) REFERENCES `client` (`id`),
  CONSTRAINT `reservation_receiving_car_center` FOREIGN KEY (`car_center_receiving_id`) REFERENCES `car_center` (`id`),
  CONSTRAINT `reservation_return_car_center` FOREIGN KEY (`car_center_return_id`) REFERENCES `car_center` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='бронирование авто';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reservation`
--

LOCK TABLES `reservation` WRITE;
/*!40000 ALTER TABLE `reservation` DISABLE KEYS */;
INSERT INTO `reservation` VALUES (1,1,1,1,2,'2021-05-30','2021-05-31','2021-06-01',4,0,650),(2,2,2,1,1,'2021-05-30','2021-05-31','2021-06-04',3,5,3800),(3,3,5,2,2,'2021-05-19','2021-05-20','2021-05-31',4,10,22725),(4,2,4,2,2,'2021-05-31','2021-05-31','2021-06-03',4,5,1681.5);
/*!40000 ALTER TABLE `reservation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-06-04 20:23:51
