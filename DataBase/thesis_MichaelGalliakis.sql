-- MySQL dump 10.13  Distrib 5.7.9, for linux-glibc2.5 (x86_64)
--
-- Host: localhost    Database: thesisv1
-- ------------------------------------------------------
-- Server version	5.5.44-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `deviceaccess`
--

DROP TABLE IF EXISTS `deviceaccess`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `deviceaccess` (
  `UserID` int(11) NOT NULL,
  `DeviceID` int(11) NOT NULL,
  `TypeAccess` varchar(1) NOT NULL DEFAULT 'R',
  `isDeleted` bit(1) DEFAULT b'0',
  `lastModified` datetime NOT NULL,
  `UserModified` int(11) NOT NULL,
  PRIMARY KEY (`UserID`,`DeviceID`),
  KEY `fk_DeviceID_DeviceID_idx` (`DeviceID`),
  KEY `fk_UserModified_UserID_idx` (`UserModified`),
  CONSTRAINT `fk_DeviceID_DeviceID` FOREIGN KEY (`DeviceID`) REFERENCES `devices` (`DeviceID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserID_UserID` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserModifiedA_UserID` FOREIGN KEY (`UserModified`) REFERENCES `users` (`UserID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deviceaccess`
--

LOCK TABLES `deviceaccess` WRITE;
/*!40000 ALTER TABLE `deviceaccess` DISABLE KEYS */;
INSERT INTO `deviceaccess` VALUES (12,16,'F','\0','0000-00-00 00:00:00',12),(12,17,'A','\0','0000-00-00 00:00:00',12),(12,18,'R','\0','0000-00-00 00:00:00',13),(13,15,'A','\0','0000-00-00 00:00:00',12),(13,18,'F','\0','0000-00-00 00:00:00',14),(14,15,'R','\0','0000-00-00 00:00:00',12),(14,16,'A','\0','0000-00-00 00:00:00',14),(14,17,'R','\0','0000-00-00 00:00:00',14);
/*!40000 ALTER TABLE `deviceaccess` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `devices`
--

DROP TABLE IF EXISTS `devices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `devices` (
  `DeviceID` int(11) NOT NULL AUTO_INCREMENT,
  `DeviceName` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  `Comment` varchar(200) DEFAULT NULL,
  `Owner` int(11) NOT NULL,
  `isDeleted` bit(1) DEFAULT b'0',
  `lastModified` datetime NOT NULL,
  `UserModified` int(11) NOT NULL,
  PRIMARY KEY (`DeviceID`),
  UNIQUE KEY `DeviceName_UNIQUE` (`DeviceName`),
  KEY `UserID_idx` (`Owner`,`UserModified`),
  KEY `fk_Owner_UserID_idx` (`Owner`),
  KEY `fk_UserModified_UserID_idx` (`UserModified`),
  CONSTRAINT `fk_Owner_UserID` FOREIGN KEY (`Owner`) REFERENCES `users` (`UserID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserModified_UserID` FOREIGN KEY (`UserModified`) REFERENCES `users` (`UserID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `devices`
--

LOCK TABLES `devices` WRITE;
/*!40000 ALTER TABLE `devices` DISABLE KEYS */;
INSERT INTO `devices` VALUES (15,'korinthos','19aedd6459eaf9067c086cfd942dd670','Spiti stin korintho!',12,'\0','0000-00-00 00:00:00',13),(16,'perama','19da8b07cc708b3fbd2bd8209eb824ea','To spiti sto perama!',13,'\0','0000-00-00 00:00:00',13),(17,'melidoni','fc9f9f4bedb9a43862cc2687184298ca','Î•Î¾Î¿Ï‡Î¹ÎºÏŒ ÏƒÏ„Î¿ Î¼ÎµÎ»Î¹Î´ÏŒÎ½Î¹!',13,'\0','0000-00-00 00:00:00',13),(18,'olxwrio','68cc7341984d9183e1b2af5abece1ea4','Spiti sto olympiako xorio!',14,'\0','0000-00-00 00:00:00',13),(19,'margarites','fbb44d03a7e56913a13e3523a3ae08de','Î•Î¾Î¿Ï‡Î¹ÎºÏŒ ÏƒÏ„Î¹Ï‚ ÎœÎ±ÏÎ³Î±ÏÎ¯Ï„ÎµÏ‚',14,'\0','0000-00-00 00:00:00',14),(20,'kavala','ce3f2eae50ccc7a898e35a06b61d97f8','Î£Ï€Î¯Ï„Î¹ ÏƒÏ„Î· ÎšÎ±Î²Î¬Î»Î±',12,'\0','0000-00-00 00:00:00',12),(21,'andros','e7b9ef4fc7b81ca7c19edb3dd0547da8','Î¤Î¿ ÎºÎ¬ÏƒÏ„ÏÎ¿ Î¼Î¿Ï… ÏƒÏ„Î·Î½ Î†Î½Î´ÏÎ¿',13,'\0','0000-00-00 00:00:00',13);
/*!40000 ALTER TABLE `devices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `requests`
--

DROP TABLE IF EXISTS `requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `requests` (
  `RequestID` int(11) NOT NULL AUTO_INCREMENT,
  `UserTo` int(11) NOT NULL,
  `UserFrom` int(11) NOT NULL,
  `TypeAccess` varchar(1) NOT NULL,
  `DeviceID` int(11) NOT NULL,
  `Text` varchar(500) DEFAULT NULL,
  `isDeleted` bit(1) DEFAULT b'0',
  `lastModified` datetime NOT NULL,
  PRIMARY KEY (`RequestID`),
  KEY `fk_UserTo_UserID_idx` (`UserTo`),
  KEY `fk_UserFrom_UserID_idx` (`UserFrom`),
  KEY `fk_DeviceID_R_DeviceID_idx` (`DeviceID`),
  CONSTRAINT `fk_DeviceID_R_DeviceID` FOREIGN KEY (`DeviceID`) REFERENCES `devices` (`DeviceID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserFrom_UserID` FOREIGN KEY (`UserFrom`) REFERENCES `users` (`UserID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_UserTo_UserID` FOREIGN KEY (`UserTo`) REFERENCES `users` (`UserID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `requests`
--

LOCK TABLES `requests` WRITE;
/*!40000 ALTER TABLE `requests` DISABLE KEYS */;
INSERT INTO `requests` VALUES (1,13,14,'F',19,'You want to have this device ?','\0','0000-00-00 00:00:00'),(2,13,13,'F',20,'Î˜Î­Î»ÎµÎ¹Ï‚ Î½Î± Î¼Îµ Ï€ÏÎ¿ÏƒÎ¸Î­ÏƒÎµÎ¹Ï‚; ','\0','0000-00-00 00:00:00'),(3,12,13,'F',21,'You want to have this device ?','\0','0000-00-00 00:00:00'),(4,14,13,'A',21,'You want to have this device ?','\0','0000-00-00 00:00:00'),(6,15,15,'R',21,'Î˜Î± Î¼Î¿Ï… Î´ÏŽÏƒÎµÎ¹Ï‚ Î´Î¹ÎºÎ±Î¹ÏŽÎ¼Î±Ï„Î± ;','\0','0000-00-00 00:00:00');
/*!40000 ALTER TABLE `requests` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `UserID` int(11) NOT NULL AUTO_INCREMENT,
  `Username` varchar(45) NOT NULL,
  `Password` varchar(45) NOT NULL,
  `Name` varchar(45) DEFAULT NULL,
  `Surname` varchar(45) DEFAULT NULL,
  `Type` varchar(1) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `isDeleted` bit(1) DEFAULT b'0',
  `lastModified` datetime NOT NULL,
  `isEnabled` bit(1) DEFAULT b'1',
  `Status` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `Username_UNIQUE` (`Username`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='Table with Users.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (12,'admin','21232f297a57a5a743894a0e4a801fc3','admin','admin','S','admin@yahoo.gr','\0','0000-00-00 00:00:00','',NULL),(13,'maria','263bce650e68ab4e23f28263760b9fa5','dsdfsdf','maria','V','maria@pmg.gr','\0','0000-00-00 00:00:00','',NULL),(14,'georgios','0ff3023d992eedd7d207ca49b359725f','georgios','georgios','U','georgios@pmg.gr','\0','0000-00-00 00:00:00','',NULL),(15,'mikeole','634379c32375875c1374090ec5fd3474','mike','mikeol','U','mike@mike.gr','\0','0000-00-00 00:00:00','',NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-29 20:08:23
