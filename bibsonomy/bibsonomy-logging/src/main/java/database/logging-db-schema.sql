--
--
--  BibSonomy-Logging - Logs clicks from users of the BibSonomy webapp.
--
--  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
--                            University of Kassel, Germany
--                            http://www.kde.cs.uni-kassel.de/
--
--  This program is free software; you can redistribute it and/or
--  modify it under the terms of the GNU General Public License
--  as published by the Free Software Foundation; either version 2
--  of the License, or (at your option) any later version.
--
--  This program is distributed in the hope that it will be useful,
--  but WITHOUT ANY WARRANTY; without even the implied warranty of
--  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--  GNU General Public License for more details.
--
--  You should have received a copy of the GNU General Public License
--  along with this program; if not, write to the Free Software
--  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
--

-- MySQL dump 10.11
--
-- Host: localhost    Database: logging
-- ------------------------------------------------------
-- Server version	5.0.67-log

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

SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `clicklog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `logdate` datetime DEFAULT NULL,
  `dompath` text,
  `dompathwclasses` text,
  `type` text,
  `pageurl` text,
  `ahref` text,
  `acontent` longtext,
  `useragent` text,
  `host` text,
  `completeheader` text,
  `xforwardedfor` text,
  `username` varchar(30) DEFAULT NULL,
  `sessionid` text,
  `listpos` varchar(20) DEFAULT NULL,
  `mouseclientpos` varchar(20) DEFAULT NULL COMMENT 'mouse position in current client window',
  `mousedocumentpos` varchar(20) DEFAULT NULL COMMENT 'mouse position in current document',
  `clientwindowsize` varchar(20) DEFAULT NULL,
  `anumofposts` int(11) DEFAULT NULL,
  `abmown` tinyint(4) DEFAULT NULL,
  `referer` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;