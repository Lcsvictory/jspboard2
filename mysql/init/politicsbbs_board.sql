-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: lcsvictory.iptime.org    Database: politicsbbs
-- ------------------------------------------------------
-- Server version	8.3.0

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
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
  `idx` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `title` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `pass` varchar(50) NOT NULL,
  `postdate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `ofile` varchar(1024) DEFAULT NULL,
  `sfile` varchar(1024) DEFAULT NULL,
  `visitcount` int NOT NULL DEFAULT '0',
  `downloadcount` int NOT NULL DEFAULT '0',
  `upcount` int NOT NULL DEFAULT '0',
  `downcount` int NOT NULL DEFAULT '0',
  `boardid` int NOT NULL,
  PRIMARY KEY (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board`
--

LOCK TABLES `board` WRITE;
/*!40000 ALTER TABLE `board` DISABLE KEYS */;
INSERT INTO `board` VALUES (1,'홍길동','제목1입니다','jdk17이거 실화냐?\r\n대단하다 jsk17.','test1234','2024-11-17 05:13:17','jsk17.png','20241117_33432970.png',1,0,1,0,1),(2,'박유신','공지제목1입니다','본문1입니다','test1234','2024-11-17 05:13:21',NULL,NULL,2,0,0,0,999),(3,'토론자1','토론제목1입니다','본문1입니다','test1234','2024-11-17 05:13:23',NULL,NULL,1,0,0,0,3),(4,'폴드2','241117 리 테스트','이게 글쓰는 창이맞냐? 정말 답답하다','1234','2024-11-17 06:26:21','','noFile',2,0,1,1,3);
/*!40000 ALTER TABLE `board` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-04-22 16:07:14
