--
--
--  BibSonomy-Database - Database for BibSonomy.
--
--  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
--                            University of Kassel, Germany
--                            http://www.kde.cs.uni-kassel.de/
--
--  This program is free software; you can redistribute it and/or
--  modify it under the terms of the GNU Lesser General Public License
--  as published by the Free Software Foundation; either version 2
--  of the License, or (at your option) any later version.
--
--  This program is distributed in the hope that it will be useful,
--  but WITHOUT ANY WARRANTY; without even the implied warranty of
--  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--  GNU Lesser General Public License for more details.
--
--  You should have received a copy of the GNU Lesser General Public License
--  along with this program; if not, write to the Free Software
--  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
--


--
-- Table structure for Table `sync_data`
--
DROP TABLE IF EXISTS `sync_data`;
CREATE TABLE `sync_data`(
  `service_id` int(10) unsigned NOT NULL,
  `user_name` varchar(30) NOT NULL default '',
  `content_type` tinyint(1) unsigned default NULL,
  `last_sync_date` datetime NOT NULL default '1815-12-10 00:00:00',
  `status` varchar(8) NOT NULL,
  `info` varchar(255) default NULL,
   PRIMARY KEY  (`service_id`, `user_name`, `content_type`, `last_sync_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sync`;
CREATE TABLE `sync`(
  `user_name` varchar(30) NOT NULL,
  `service_id` int(10) unsigned NOT NULL,
  `credentials` text NOT NULL default '',
  `content_type` tinyint(1) unsigned default 0,
  `direction` varchar(4) default 'both',
  `strategy` varchar(2) default 'lw',
   PRIMARY KEY  (`service_id`, `user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sync_services`;
CREATE TABLE `sync_services` (
  `uri` varchar(255) NOT NULL,
  `service_id` int(10) unsigned NOT NULL,
  `server` boolean NOT NULL,
  PRIMARY KEY (`service_id`),
  UNIQUE KEY (`uri`, `server`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `DBLP`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `DBLP` (
  `lastupdate` datetime NOT NULL default '1815-12-10 00:00:00'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;


--
-- Table structure for table `author`
--

DROP TABLE IF EXISTS `author`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `author` (
  `author_id` int(10) unsigned NOT NULL auto_increment,
  `first_name` varchar(255) default NULL,
  `middle` varchar(255) default NULL,
  `last_name` varchar(255) default NULL,
  `ctr` int(10) unsigned NOT NULL default '1',
  PRIMARY KEY  (`author_id`),
  UNIQUE KEY `last_name_middle_first_name_idx` (`last_name`,`middle`,`first_name`),
  KEY `last_name_idx` (`last_name`)
) ENGINE=InnoDB AUTO_INCREMENT=20837 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `author_bibtex_content`
--

DROP TABLE IF EXISTS `author_bibtex_content`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `author_bibtex_content` (
  `author_id` int(10) unsigned NOT NULL,
  `content_id` int(10) unsigned NOT NULL,
  PRIMARY KEY  (`author_id`,`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `author_bibtex_name`
--

DROP TABLE IF EXISTS `author_bibtex_name`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `author_bibtex_name` (
  `author_id` int(10) unsigned NOT NULL,
  `bibtex_author_name` varchar(255) NOT NULL,
  PRIMARY KEY  (`author_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `bibhash`
--

DROP TABLE IF EXISTS `bibhash`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `bibhash` (
  `hash` char(32) NOT NULL default '',
  `ctr` int(10) unsigned NOT NULL default '1',
  `type` tinyint(3) NOT NULL default '0',
  PRIMARY KEY  (`type`,`hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `bibtex`
--

DROP TABLE IF EXISTS `bibtex`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `bibtex` (
  `content_id` int(10) unsigned NOT NULL default '0',
  `journal` text,
  `volume` varchar(255) default NULL,
  `chapter` varchar(255) default NULL,
  `edition` varchar(255) default NULL,
  `month` varchar(45) default NULL,
  `day` varchar(45) default NULL,
  `booktitle` text,
  `howPublished` varchar(255) default NULL,
  `institution` varchar(255) default NULL,
  `organization` varchar(255) default NULL,
  `publisher` varchar(255) default NULL,
  `address` varchar(255) default NULL,
  `school` varchar(255) default NULL,
  `series` varchar(255) default NULL,
  `bibtexKey` varchar(255) default NULL,
  `group` int(10) default '0',
  `date` datetime default '1815-12-10 00:00:00',
  `user_name` varchar(255) default NULL,
  `url` text,
  `type` varchar(255) default NULL,
  `description` text,
  `annote` varchar(255) default NULL,
  `note` text,
  `pages` varchar(50) default NULL,
  `bKey` varchar(255) default NULL,
  `number` varchar(45) default NULL,
  `crossref` varchar(255) default NULL,
  `misc` text,
  `bibtexAbstract` text,
  `simhash0` char(32) NOT NULL default '',
  `simhash1` char(32) NOT NULL default '',
  `simhash2` char(32) NOT NULL default '',
  `simhash3` char(32) NOT NULL default '',
  `entrytype` varchar(30) default NULL,
  `title` text,
  `author` text,
  `editor` text,
  `year` varchar(45) default NULL,
  `privnote` text,
  `scraperid` int(11) NOT NULL default '-1',
  `change_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `rating` tinyint(3) NOT NULL default '0',
  PRIMARY KEY  (`content_id`),
  UNIQUE KEY `unique_user_name_simhash2` (`user_name`,`simhash2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `repository`
--

DROP TABLE IF EXISTS `repository`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `repository` (
  `inter_hash` char(32) NOT NULL default '',
  `intra_hash` char(32) NOT NULL default '',
  `repository_date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `user_name` varchar(30) NOT NULL,
  `repository_name` varchar(30) NOT NULL,
  KEY  (`inter_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;


--
-- Table structure for table `bibtexurls`
--

DROP TABLE IF EXISTS `bibtexurls`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `bibtexurls` (
  `content_id` int(10) unsigned NOT NULL default '0',
  `url` varchar(255) NOT NULL default '',
  `text` text,
  `group` int(10) default '0',
  `date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`content_id`,`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `bookmark`
--

DROP TABLE IF EXISTS `bookmark`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `bookmark` (
  `content_id` int(10) unsigned NOT NULL default '0',
  `book_url_hash` varchar(32) NOT NULL default '',
  `book_description` text NOT NULL,
  `book_extended` text,
  `group` int(10) default '0',
  `date` datetime NOT NULL default '1815-12-10 00:00:00',
  `user_name` varchar(30) NOT NULL default '',
  `to_bib` tinyint(3) NOT NULL default '0',
  `change_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `rating` tinyint(3) NOT NULL default '0',
  PRIMARY KEY  (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `gold_standard_publications`
--

DROP TABLE IF EXISTS `gold_standard_publications`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `gold_standard_publications` (
  `content_id` int(10) unsigned NOT NULL default '0',
  `journal` text,
  `volume` varchar(255) default NULL,
  `chapter` varchar(255) default NULL,
  `edition` varchar(255) default NULL,
  `month` varchar(45) default NULL,
  `day` varchar(45) default NULL,
  `booktitle` text,
  `howPublished` varchar(255) default NULL,
  `institution` varchar(255) default NULL,
  `organization` varchar(255) default NULL,
  `publisher` varchar(255) default NULL,
  `address` varchar(255) default NULL,
  `school` varchar(255) default NULL,
  `series` varchar(255) default NULL,
  `bibtexKey` varchar(255) default NULL,
  `group` int(10) default '0',
  `date` datetime default '1815-12-10 00:00:00',
  `user_name` varchar(255) default NULL,
  `url` text,
  `type` varchar(255) default NULL,
  `description` text,
  `annote` varchar(255) default NULL,
  `note` text,
  `pages` varchar(50) default NULL,
  `bKey` varchar(255) default NULL,
  `number` varchar(45) default NULL,
  `crossref` varchar(255) default NULL,
  `misc` text,
  `bibtexAbstract` text,
  `simhash0` char(32) NOT NULL default '',
  `simhash1` char(32) NOT NULL default '',
  `simhash2` char(32) NOT NULL default '',
  `simhash3` char(32) NOT NULL default '',
  `entrytype` varchar(30) default NULL,
  `title` text,
  `author` text,
  `editor` text,
  `year` varchar(45) default NULL,
  `privnote` text,
  `scraperid` int(11) NOT NULL default '-1',
  `change_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `rating` tinyint(3) NOT NULL default '0',
  PRIMARY KEY  (`simhash1`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `gold_standard_publications_references`
--

DROP TABLE IF EXISTS `gold_standard_publication_references`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `gold_standard_publication_references` (
  `publication` char(32) NOT NULL default '',
  `reference` char(32) NOT NULL default '',
  `user_name` varchar(255) default NULL,
  `date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`publication`, `reference`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `log_gold_standard_publications`
--

DROP TABLE IF EXISTS `log_gold_standard_publications`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `log_gold_standard_publications` (
  `content_id` int(10) unsigned NOT NULL default '0',
  `journal` text,
  `volume` varchar(255) default NULL,
  `chapter` varchar(255) default NULL,
  `edition` varchar(255) default NULL,
  `month` varchar(45) default NULL,
  `day` varchar(45) default NULL,
  `bookTitle` text,
  `howPublished` varchar(255) default NULL,
  `institution` varchar(255) default NULL,
  `organization` varchar(255) default NULL,
  `publisher` varchar(255) default NULL,
  `address` varchar(255) default NULL,
  `school` varchar(255) default NULL,
  `series` varchar(255) default NULL,
  `bibtexKey` varchar(255) default NULL,
  `group` int(10) default '0',
  `date` datetime default '1815-12-10 00:00:00',
  `user_name` varchar(255) default NULL,
  `url` text,
  `type` varchar(255) default NULL,
  `description` text,
  `annote` varchar(255) default NULL,
  `note` text,
  `pages` varchar(50) default NULL,
  `bKey` varchar(255) default NULL,
  `number` varchar(45) default NULL,
  `crossref` varchar(255) default NULL,
  `misc` text,
  `bibtexAbstract` text,
  `entrytype` varchar(30) default NULL,
  `title` text,
  `author` text,
  `editor` text,
  `year` varchar(45) default NULL,
  `simhash0` char(32) NOT NULL default '',
  `simhash1` char(32) NOT NULL default '',
  `simhash2` char(32) NOT NULL default '',
  `simhash3` char(32) NOT NULL default '',
  `new_content_id` int(10) unsigned NOT NULL default '0',
  `scraperid` int(11) NOT NULL default '-1',
  `change_date` datetime NOT NULL default '1815-12-10 00:00:00',
  `rating` tinyint(3) NOT NULL default '0',
  `privnote` text,
  `new_simhash1` char(32) NOT NULL default '',
  `log_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;


--
-- Table structure for table `log_gold_standard_publication_references`
--

DROP TABLE IF EXISTS `log_gold_standard_publication_references`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `log_gold_standard_publication_references` (
  `publication` char(32) NOT NULL default '',
  `reference` char(32) NOT NULL default '',
  `user_name` varchar(255) default NULL,
  `log_date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`publication`, `reference`, `log_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `classifier_settings`
--

DROP TABLE IF EXISTS `classifier_settings`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `classifier_settings` (
  `ID` tinyint(4) NOT NULL auto_increment,
  `key` varchar(255) default NULL,
  `value` varchar(255) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `classifier_whitelist`
--
DROP TABLE IF EXISTS `classifier_whitelist`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `classifier_whitelist` (
  `white_regex` text
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `collector`
--

DROP TABLE IF EXISTS `collector`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `collector` (
  `user_name` varchar(30) NOT NULL,
  `content_id` int(10) unsigned NOT NULL,
  `date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`user_name`,`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `document`
--

DROP TABLE IF EXISTS `document`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `document` (
  `hash` varchar(255) NOT NULL default '',
  `content_id` int(10) unsigned NOT NULL default '0',
  `name` varchar(255) default '',
  `user_name` varchar(255) default '',
  `date` datetime default '0000-00-00 00:00:00',
  `md5hash` char(32) NOT NULL default '00000000000000000000000000000000',
  PRIMARY KEY  (`hash`,`content_id`),
  KEY `content_id_idx` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `extended_fields_data`
--
DROP TABLE IF EXISTS `extended_fields_data`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `extended_fields_data` (
  `key` varchar(255) NOT NULL,
  `value` text NOT NULL,
  `content_id` int(10) unsigned NOT NULL,
  `date_of_create` datetime NOT NULL,
  `date_of_last_mod` timestamp NOT NULL default CURRENT_TIMESTAMP on
update CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `friends`
--

DROP TABLE IF EXISTS `friends`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `friends` (
  `friends_id` int(11) NOT NULL auto_increment,
  `user_name` varchar(30) NOT NULL default '',
  `f_user_name` varchar(30) NOT NULL default '',
  `tag_name` varchar(255) NOT NULL DEFAULT 'sys:network:bibsonomy',
  `f_network_user_id` int(10) DEFAULT NULL,
  `friendship_date` datetime NOT NULL default '1815-12-10 00:00:00',
  PRIMARY KEY  (`friends_id`),
  UNIQUE KEY `unique_friendship` (`user_name`,`f_user_name`,`tag_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1601 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `followers`
--

DROP TABLE IF EXISTS `followers`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `followers` (
  `followers_id` int(11) NOT NULL auto_increment,
  `user_name` varchar(30) NOT NULL default '',
  `f_user_name` varchar(30) NOT NULL default '',
  `fellowship_date` datetime NOT NULL default '1815-12-10 00:00:00',
  PRIMARY KEY  (`followers_id`),
  UNIQUE KEY `unique_followers` (`user_name`,`f_user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `group_tagsets`
--

DROP TABLE IF EXISTS `group_tagsets`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `group_tagsets` (
  `tag_name` varchar(255) NOT NULL,
  `set_name` varchar(255) NOT NULL,
  `group` int(11) NOT NULL,
  PRIMARY KEY  (`group`,`set_name`,`tag_name`),
  CONSTRAINT `group_tagsets_ibfk_1` FOREIGN KEY (`group`) REFERENCES `groupids` (`group`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `groupids`
--

DROP TABLE IF EXISTS `groupids`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `groupids` (
  `group_name` varchar(30) NOT NULL default '',
  `group` int(10) NOT NULL default '0',
  `privlevel` tinyint(3) unsigned default '1',
  `sharedDocuments` tinyint(1) default '0',
  PRIMARY KEY  (`group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `groups` (
  `user_name` varchar(30) NOT NULL default '',
  `group` int(10) default '0',
  `defaultgroup` int(10) default '0',
  `start_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `user_status` int(10) NOT NULL default '7'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `grouptas`
--

DROP TABLE IF EXISTS `grouptas`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `grouptas` (
  `tas_id` int(10) unsigned NOT NULL,
  `tag_name` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `content_id` int(10) unsigned NOT NULL default '0',
  `content_type` tinyint(1) unsigned default NULL,
  `user_name` varchar(30) NOT NULL default '',
  `date` datetime NOT NULL default '1815-12-10 00:00:00',
  `group` int(10) default '0',
  `tag_lower` varchar(255) NOT NULL default '',
  `change_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`tas_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `highwirelist`
--

DROP TABLE IF EXISTS `highwirelist`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `highwirelist` (
  `list` longtext NOT NULL,
  `lastupdate` timestamp NOT NULL default CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `ids`
--

DROP TABLE IF EXISTS `ids`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ids` (
  `name` tinyint(3) unsigned NOT NULL,
  `value` int(10) unsigned NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY  (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `inboxMail`
--

DROP TABLE IF EXISTS `inboxMail`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `inboxMail` (
  `message_id` int(10) unsigned NOT NULL,
  `content_id` int(10) unsigned NOT NULL,
  `intraHash` varchar(32) NOT NULL default '',
  `sender_user` varchar(30) NOT NULL,
  `receiver_user` varchar(30) NOT NULL,
  `date` datetime default NULL,
  `content_type` tinyint(1) unsigned,
  PRIMARY KEY  (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `inbox_tas`
--

DROP TABLE IF EXISTS `inbox_tas`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `inbox_tas` (
	`message_id` int(10) unsigned NOT NULL,
	`tag_name` varchar(255) NOT NULL, 
PRIMARY KEY (message_id, tag_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;


--
-- Table structure for table `inetAddressStates`
--

DROP TABLE IF EXISTS `inetAddressStates`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `inetAddressStates` (
  `address` char(15) NOT NULL default '',
  `status` tinyint(4) default NULL,
  `updated_at` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `ldapUser`
--

DROP TABLE IF EXISTS `ldapUser`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ldapUser` (
  `user_name` varchar(30) NOT NULL,
  `ldapUserId` varchar(255) NOT NULL,
  `lastAccess` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ldapUserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `log_bibtex`
--

DROP TABLE IF EXISTS `log_bibtex`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `log_bibtex` (
  `content_id` int(10) unsigned NOT NULL default '0',
  `journal` text,
  `volume` varchar(255) default NULL,
  `chapter` varchar(255) default NULL,
  `edition` varchar(255) default NULL,
  `month` varchar(45) default NULL,
  `day` varchar(45) default NULL,
  `bookTitle` text,
  `howPublished` varchar(255) default NULL,
  `institution` varchar(255) default NULL,
  `organization` varchar(255) default NULL,
  `publisher` varchar(255) default NULL,
  `address` varchar(255) default NULL,
  `school` varchar(255) default NULL,
  `series` varchar(255) default NULL,
  `bibtexKey` varchar(255) default NULL,
  `group` int(10) default '0',
  `date` datetime default '1815-12-10 00:00:00',
  `user_name` varchar(255) default NULL,
  `url` text,
  `type` varchar(255) default NULL,
  `description` text,
  `annote` varchar(255) default NULL,
  `note` text,
  `pages` varchar(50) default NULL,
  `bKey` varchar(255) default NULL,
  `number` varchar(45) default NULL,
  `crossref` varchar(255) default NULL,
  `misc` text,
  `bibtexAbstract` text,
  `entrytype` varchar(30) default NULL,
  `title` text,
  `author` text,
  `editor` text,
  `year` varchar(45) default NULL,
  `simhash0` char(32) NOT NULL default '',
  `simhash1` char(32) NOT NULL default '',
  `simhash2` char(32) NOT NULL default '',
  `simhash3` char(32) NOT NULL default '',
  `new_content_id` int(10) unsigned NOT NULL default '0',
  `scraperid` int(11) NOT NULL default '-1',
  `change_date` datetime NOT NULL default '1815-12-10 00:00:00',
  `rating` tinyint(3) NOT NULL default '0',
  `privnote` text,
  `log_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `log_bookmark`
--

DROP TABLE IF EXISTS `log_bookmark`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `log_bookmark` (
  `content_id` int(10) unsigned NOT NULL default '0',
  `book_url_hash` varchar(32) NOT NULL default '',
  `book_description` text NOT NULL default '',
  `book_extended` text,
  `group` int(10) default '0',
  `date` datetime NOT NULL default '1815-12-10 00:00:00',
  `user_name` varchar(30) NOT NULL default '',
  `new_content_id` int(10) unsigned NOT NULL default '0',
  `change_date` datetime NOT NULL default '1815-12-10 00:00:00',
  `rating` tinyint(3) NOT NULL default '0',
  `log_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `log_collector`
--

DROP TABLE IF EXISTS `log_collector`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `log_collector` (
  `user_name` varchar(30) NOT NULL,
  `content_id` int(10) unsigned NOT NULL,
  `add_date` datetime NOT NULL default '1815-12-10 00:00:00',
  `del_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `log_friends`
--

DROP TABLE IF EXISTS `log_friends`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `log_friends` (
  `friends_id` int(11) NOT NULL auto_increment,
  `user_name` varchar(30) NOT NULL default '',
  `f_user_name` varchar(30) NOT NULL default '',
  `tag_name` varchar(255) NOT NULL DEFAULT 'sys:network:bibsonomy',
  `f_network_user_id` int(10) DEFAULT NULL,
  `friendship_date` datetime NOT NULL default '1815-12-10 00:00:00',
  `friendship_end_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`friends_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1565 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `log_followers`
--

DROP TABLE IF EXISTS `log_followers`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `log_followers` (
  `followers_id` int(11) NOT NULL auto_increment,
  `user_name` varchar(30) NOT NULL default '',
  `f_user_name` varchar(30) NOT NULL default '',
  `fellowship_date` datetime NOT NULL default '1815-12-10 00:00:00',
  `fellowship_end_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`followers_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1565 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `log_groups`
--

DROP TABLE IF EXISTS `log_groups`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `log_groups` (
  `user_name` varchar(30) NOT NULL default '',
  `group` int(10) default '0',
  `defaultgroup` int(10) default '0',
  `start_date` datetime NOT NULL default '1815-12-10 00:00:00',
  `end_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `user_status` int(11) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `log_prediction`
--

DROP TABLE IF EXISTS `log_prediction`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `log_prediction` (
  `ID` int(11) NOT NULL auto_increment,
  `user_name` varchar(30) NOT NULL,
  `prediction` tinyint(4) NOT NULL,
  `timestamp` bigint(20) default NULL,
  `updated_at` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `algorithm` varchar(100) default NULL,
  `mode` char(1) default NULL,
  `confidence` double default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `timestamp_user_name_idx` (`timestamp`,`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3455625 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `log_tagtagrelations`
--

DROP TABLE IF EXISTS `log_tagtagrelations`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `log_tagtagrelations` (
  `relationID` int(10) unsigned NOT NULL,
  `lower` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `upper` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `date_of_create` datetime NOT NULL default '1815-12-10 00:00:00',
  `date_of_last_mod` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `user_name` varchar(30) NOT NULL default '',
  PRIMARY KEY  (`relationID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `log_tas`
--

DROP TABLE IF EXISTS `log_tas`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `log_tas` (
  `tas_id` int(10) unsigned NOT NULL,
  `tag_name` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `content_id` int(10) unsigned NOT NULL default '0',
  `content_type` tinyint(3) unsigned default NULL,
  `date` datetime NOT NULL default '1815-12-10 00:00:00',
  `change_date` datetime NOT NULL default '1815-12-10 00:00:00',
  `log_date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`tas_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `log_user`
--

DROP TABLE IF EXISTS `log_user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `log_user` (
  `nr` int(11) NOT NULL auto_increment,
  `user_name` varchar(30) NOT NULL,
  `user_email` varchar(255) NOT NULL,
  `user_password` varchar(32) NOT NULL,
  `user_homepage` varchar(255) default '',
  `user_realname` varchar(255) NOT NULL,
  `spammer` tinyint(1) NOT NULL default '0',
  `openurl` varchar(255) default NULL,
  `reg_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `ip_address` varchar(255) default NULL,
  `id` int(11) default NULL,
  `tmp_password` char(32) default NULL,
  `tmp_request_date` datetime NOT NULL,
  `tagbox_style` tinyint(4) default NULL,
  `tagbox_sort` tinyint(4) default NULL,
  `tagbox_minfreq` tinyint(4) default NULL,
  `tagbox_max_count` integer default NULL,
  `is_max_count` boolean default true,
  `tagbox_tooltip` tinyint(4) default NULL,
  `list_itemcount` smallint(6) default NULL,
  `spammer_suggest` tinyint(1) NOT NULL default '1',
  `birthday` date default NULL,
  `gender` char(1) default NULL,
  `profession` varchar(255) default NULL,
  `institution` varchar(255) default NULL,
  `interests` varchar(255) default NULL,
  `hobbies` varchar(255) default NULL,
  `place` varchar(255) default NULL,
  `profilegroup` tinyint(1) NOT NULL default '0',
  `api_key` varchar(32) default NULL,
  `updated_by` varchar(30) default NULL,
  `updated_at` datetime default '1815-12-10 00:00:00',
  `lang` char(2) default NULL,
  `role` tinyint(4) NOT NULL,
  `timestamp` mediumtext NOT NULL,
  `prediction` int(11) default '9',
  `algorithm` varchar(255) default NULL,
  `count` int(11) default '0',
  `log_level` tinyint(4) NOT NULL default '0',
  `to_classify` tinyint(4) default '1',
  `confirmDelete` tinyint(1) default '1',
  `simple_interface` tinyint(1) default '0',
  `show_bookmark` tinyint(1) default '1',
  `show_bibtex` tinyint(1) default '1',
  PRIMARY KEY  (`nr`)
) ENGINE=InnoDB AUTO_INCREMENT=338 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `log_wiki`
--

DROP TABLE IF EXISTS `log_wiki`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `log_wiki` (
  `user_name` varchar(30) NOT NULL,
  `user_wiki` text,
  `date` DATETIME,
  PRIMARY KEY  (`user_name`,`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `openIDUser`
--

DROP TABLE IF EXISTS `openIDUser`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `openIDUser` (
  `user_name` varchar(30) NOT NULL,
  `openID` varchar(255) NOT NULL,
  PRIMARY KEY  (`openID`),
  KEY `user_name` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `pendingUser`
--

DROP TABLE IF EXISTS `pendingUser`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `pendingUser` (
  `user_name` varchar(30) NOT NULL,
  `user_email` varchar(255) NOT NULL,
  `user_password` varchar(32) NOT NULL,
  `user_homepage` varchar(255) default '',
  `user_realname` varchar(255) NOT NULL,
  `spammer` tinyint(1) NOT NULL default '0',
  `openurl` varchar(255) default NULL,
  `reg_date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `ip_address` varchar(255) default NULL,
  `id` int(11) default NULL,
  `tmp_password` char(32) default NULL,
  `tmp_request_date` datetime NOT NULL default '1815-12-10 00:00:00',
  `tagbox_style` tinyint(4) default '0',
  `tagbox_sort` tinyint(4) default '0',
  `tagbox_minfreq` tinyint(4) default '0',
  `tagbox_max_count` integer default '0',
  `is_max_count` boolean default true,
  `tagbox_tooltip` tinyint(4) default '0',
  `list_itemcount` smallint(6) default '10',
  `spammer_suggest` tinyint(1) NOT NULL default '1',
  `birthday` date default NULL,
  `gender` char(1) default NULL,
  `profession` varchar(255) default NULL,
  `institution` varchar(255) default NULL,
  `interests` varchar(255) default NULL,
  `hobbies` varchar(255) default NULL,
  `place` varchar(255) default NULL,
  `profilegroup` tinyint(1) default '1',
  `api_key` varchar(32) default NULL,
  `updated_by` varchar(30) default NULL,
  `updated_at` datetime default '1815-12-10 00:00:00',
  `role` tinyint(4) NOT NULL default '1',
  `lang` char(2) NOT NULL default 'en',
  `to_classify` tinyint(4) default '1',
  `log_level` tinyint(4) NOT NULL default '0',
  `confirmDelete` tinyint(1) default '1',
  `activation_code` varchar(32) NOT NULL,
  `simple_interface` tinyint(1) default '0',
  `show_bookmark` tinyint(1) default '1',
  `show_bibtex` tinyint(1) default '1',
  UNIQUE (`activation_code`),
  PRIMARY KEY  (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `picked_concepts`
--

DROP TABLE IF EXISTS `picked_concepts`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `picked_concepts` (
  `upper` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `user_name` varchar(30) NOT NULL,
  `date_of_create` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`user_name`,`upper`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `popular_tags`
--

DROP TABLE IF EXISTS `popular_tags`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `popular_tags` (
  `tag_lower` varchar(255) NOT NULL default '',
  `tag_ctr` int(11) default NULL,
  `content_type` tinyint(1) unsigned default NULL,
  `popular_days` smallint(6) default '0',
  KEY `tag_lower_idx` (`tag_lower`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `prediction`
--

DROP TABLE IF EXISTS `prediction`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `prediction` (
  `user_name` varchar(30) NOT NULL,
  `prediction` tinyint(4) NOT NULL,
  `timestamp` bigint(20) default NULL,
  `updated_at` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `algorithm` varchar(100) NOT NULL default '',
  `mode` char(1) default NULL,
  `evaluator` tinyint(4) default NULL,
  `confidence` double default NULL,
  PRIMARY KEY  (`user_name`,`algorithm`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

/*!50003 SET @SAVE_SQL_MODE=@@SQL_MODE*/;

DELIMITER ;;
/*!50003 SET SESSION SQL_MODE="" */;;
/*!50003 CREATE */ /*!50017 DEFINER=`root`@`localhost` */ /*!50003 TRIGGER `insert_evaluator` BEFORE INSERT ON `prediction` FOR EACH ROW BEGIN declare admin varchar(20); select u.updated_by into admin from user u where u.user_name = NEW.user_name LIMIT 1; if (admin != 'classifier') THEN set NEW.evaluator = 1; else set NEW.evaluator = 0; End IF; END */;;

DELIMITER ;
/*!50003 SET SESSION SQL_MODE=@SAVE_SQL_MODE*/;

--
-- Table structure for table `ranking_queue`
--

DROP TABLE IF EXISTS `ranking_queue`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `ranking_queue` (
  `id` int(11) default NULL,
  `dim` int(11) NOT NULL default '0',
  `item` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `status` int(1) default '0',
  PRIMARY KEY  (`dim`,`item`),
  UNIQUE KEY `id_idx` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `rankings`
--

DROP TABLE IF EXISTS `rankings`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `rankings` (
  `id` int(11) NOT NULL,
  `time` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `alpha` double default NULL,
  `beta` double default NULL,
  `gamma` double default NULL,
  `basepref` double default NULL,
  `dim` int(11) default NULL,
  `item` varchar(255) default NULL,
  `itemtype` int(11) default NULL,
  `itempref` double default NULL,
  `delta` double default NULL,
  `iter` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `scraperMetaData`
--

DROP TABLE IF EXISTS `scraperMetaData`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `scraperMetaData` (
  `id` int(10) unsigned NOT NULL,
  `metaResult` text,
  `scraper` varchar(255) NOT NULL,
  `url` text,
  `scrape_date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `search_bibtex`
--

DROP TABLE IF EXISTS `search_bibtex`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `search_bibtex` (
  `content_id` int(10) unsigned NOT NULL default '0',
  `content` text NOT NULL,
  `author` text NOT NULL,
  `group` int(10) default '0',
  `date` datetime NOT NULL default '1815-12-10 00:00:00',
  `user_name` varchar(30) NOT NULL default '',
  PRIMARY KEY  (`content_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;


--
-- Table structure for table `search_bookmark`
--

DROP TABLE IF EXISTS `search_bookmark`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `search_bookmark` (
  `content_id` int(10) unsigned NOT NULL default '0',
  `content` text NOT NULL,
  `group` int(10) default '0',
  `date` datetime NOT NULL default '1815-12-10 00:00:00',
  `user_name` varchar(30) NOT NULL default '',
  PRIMARY KEY  (`content_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;


--
-- Table structure for table `spammer_tags`
--

DROP TABLE IF EXISTS `spammer_tags`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `spammer_tags` (
  `tag_name` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `spammer` tinyint(1) NOT NULL default '1',
  UNIQUE KEY `tag_name` (`tag_name`),
  UNIQUE KEY `tag_name_2` (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tags` (
  `tag_id` int(10) unsigned NOT NULL auto_increment,
  `tag_name` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `tag_stem` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `tag_ctr` int(10) unsigned NOT NULL default '1',
  `waiting_content_sim` float NOT NULL default '0',
  `tag_ctr_public` int(10) unsigned NOT NULL default '0',
  `show_tag` tinyint(1) default '0',
  PRIMARY KEY  (`tag_id`),
  UNIQUE KEY `tag_name_idx` (`tag_name`)
) ENGINE=InnoDB AUTO_INCREMENT=21052613 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tagtag`
--

DROP TABLE IF EXISTS `tagtag`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tagtag` (
  `t1` varchar(255) default NULL,
  `t2` varchar(255) default NULL,
  `ctr` int(10) NOT NULL default '1',
  `ctr_public` int(10) unsigned NOT NULL default '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tagtag_batch`
--

DROP TABLE IF EXISTS `tagtag_batch`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tagtag_batch` (
  `content_id` int(10) unsigned NOT NULL default '0',
  `tags` text,
  `toinc` tinyint(1) default NULL,
  `isactive` tinyint(1) default '0',
  `id` int(10) unsigned NOT NULL auto_increment,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=711340 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tagtag_similarity`
--

DROP TABLE IF EXISTS `tagtag_similarity`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tagtag_similarity` (
  `t1` varchar(255) NOT NULL default '',
  `t2` varchar(255) NOT NULL default '',
  `sim` float NOT NULL default '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tagtag_similarity2`
--

DROP TABLE IF EXISTS `tagtag_similarity2`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tagtag_similarity2` (
  `t1` varchar(255) NOT NULL default '',
  `t2` varchar(255) NOT NULL default '',
  `sim` float NOT NULL default '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tagtag_temp`
--

DROP TABLE IF EXISTS `tagtag_temp`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tagtag_temp` (
  `t1` varchar(255) default NULL,
  `t2` varchar(255) default NULL,
  `incdec` tinyint(1) default NULL,
  `id` int(10) unsigned NOT NULL auto_increment,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tagtagrelations`
--

DROP TABLE IF EXISTS `tagtagrelations`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tagtagrelations` (
  `relationID` int(10) unsigned NOT NULL auto_increment,
  `date_of_create` datetime NOT NULL default '1815-12-10 00:00:00',
  `date_of_last_mod` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `user_name` varchar(30) NOT NULL default '',
  `lower` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `upper` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `picked` tinyint(1) default '1',
  `lower_lcase` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `upper_lcase` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  PRIMARY KEY  (`relationID`),
  UNIQUE KEY `user_name` (`user_name`,`lower`(150),`upper`(150))
) ENGINE=InnoDB AUTO_INCREMENT=156134 DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tas`
--

DROP TABLE IF EXISTS `tas`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `tas` (
  `tas_id` int(10) unsigned NOT NULL,
  `tag_name` varchar(255) character set utf8 collate utf8_bin NOT NULL default '',
  `content_id` int(10) unsigned NOT NULL default '0',
  `content_type` tinyint(1) unsigned default NULL,
  `user_name` varchar(30) NOT NULL default '',
  `date` datetime NOT NULL default '1815-12-10 00:00:00',
  `group` int(10) default '0',
  `tag_lower` varchar(255) NOT NULL default '',
  `change_date` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  PRIMARY KEY  (`tas_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `temp_bibtex`
--

DROP TABLE IF EXISTS `temp_bibtex`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `temp_bibtex` (
  `content_id` int(10) unsigned NOT NULL default '0',
  `journal` varchar(255) default NULL,
  `volume` varchar(255) default NULL,
  `chapter` varchar(255) default NULL,
  `edition` varchar(255) default NULL,
  `month` varchar(45) default NULL,
  `day` varchar(45) default NULL,
  `bookTitle` varchar(255) default NULL,
  `howPublished` varchar(255) default NULL,
  `institution` varchar(255) default NULL,
  `organization` varchar(255) default NULL,
  `publisher` varchar(255) default NULL,
  `address` varchar(255) default NULL,
  `school` varchar(255) default NULL,
  `series` varchar(255) default NULL,
  `bibtexKey` varchar(255) default NULL,
  `date` datetime default '1815-12-10 00:00:00',
  `user_name` varchar(255) default NULL,
  `url` text,
  `type` varchar(255) default NULL,
  `description` varchar(255) default NULL,
  `annote` varchar(255) default NULL,
  `note` text,
  `pages` varchar(50) default NULL,
  `bKey` varchar(255) default NULL,
  `number` varchar(45) default NULL,
  `crossref` varchar(255) default NULL,
  `misc` text,
  `bibtexAbstract` text,
  `simhash0` char(32) NOT NULL default '',
  `ctr` int(10) unsigned NOT NULL default '1',
  `rank` int(10) unsigned NOT NULL default '1',
  `simhash1` char(32) NOT NULL default '',
  `simhash2` char(32) NOT NULL default '',
  `simhash3` char(32) NOT NULL default '',
  `entrytype` varchar(30) default NULL,
  `title` text,
  `author` text,
  `editor` text,
  `year` varchar(45) default NULL,
  `rating` tinyint(3) NOT NULL default '0',
  `popular_days` smallint(6) NOT NULL default '0',
  PRIMARY KEY  (`popular_days`,`rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `temp_bookmark`
--

DROP TABLE IF EXISTS `temp_bookmark`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `temp_bookmark` (
  `content_id` int(10) unsigned NOT NULL default '0',
  `book_description` varchar(255) NOT NULL default '',
  `book_extended` text,
  `book_url_hash` varchar(32) NOT NULL default '',
  `date` datetime NOT NULL default '1815-12-10 00:00:00',
  `user_name` varchar(30) NOT NULL default '',
  `book_url_ctr` int(10) unsigned NOT NULL default '1',
  `rank` int(10) unsigned NOT NULL default '1',
  `rating` tinyint(3) NOT NULL default '0',
  `popular_days` smallint(6) NOT NULL default '0',
  PRIMARY KEY  (`popular_days`,`rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `urls`
--

DROP TABLE IF EXISTS `urls`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `urls` (
  `book_url_hash` varchar(32) NOT NULL default '',
  `book_url` text NOT NULL,
  `book_url_ctr` int(10) unsigned NOT NULL default '1',
  PRIMARY KEY  (`book_url_hash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `user` (
  `user_name` varchar(30) NOT NULL,
  `user_email` varchar(255) NOT NULL,
  `user_password` varchar(32) NOT NULL,
  `user_homepage` varchar(255) default '',
  `user_realname` varchar(255) NOT NULL,
  `spammer` tinyint(1) NOT NULL default '0',
  `openurl` varchar(255) default NULL,
  `reg_date` timestamp NOT NULL default CURRENT_TIMESTAMP,
  `ip_address` varchar(255) default NULL,
  `id` int(11) NOT NULL auto_increment,
  `tmp_password` char(32) default NULL,
  `tmp_request_date` datetime NOT NULL default '1815-12-10 00:00:00',
  `tagbox_style` tinyint(4) default '0',
  `tagbox_sort` tinyint(4) default '0',
  `tagbox_minfreq` tinyint(4) default '0',
  `tagbox_max_count` integer default '50',
  `is_max_count` boolean default true,
  `tagbox_tooltip` tinyint(4) default '0',
  `list_itemcount` smallint(6) default '10',
  `spammer_suggest` tinyint(1) NOT NULL default '1',
  `birthday` date default NULL,
  `gender` char(1) default NULL,
  `profession` varchar(255) default NULL,
  `institution` varchar(255) default NULL,
  `interests` varchar(255) default NULL,
  `hobbies` varchar(255) default NULL,
  `place` varchar(255) default NULL,
  `profilegroup` tinyint(1) default '1',
  `api_key` varchar(32) default NULL,
  `updated_by` varchar(30) default NULL,
  `updated_at` datetime default '1815-12-10 00:00:00',
  `role` tinyint(4) NOT NULL default '1',
  `lang` char(2) NOT NULL default 'en',
  `to_classify` tinyint(4) default '1',
  `log_level` tinyint(4) NOT NULL default '0',
  `confirmDelete` tinyint(1) default '1',
  `simple_interface` tinyint(1) default '0',
  `show_bookmark` tinyint(1) default '1',
  `show_bibtex` tinyint(1) default '1',
  PRIMARY KEY  (`user_name`),
  UNIQUE KEY `user_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

/*!50003 SET @SAVE_SQL_MODE=@@SQL_MODE*/;

DELIMITER ;;
/*!50003 SET SESSION SQL_MODE="" */;;
/*!50003 CREATE */ /*!50017 DEFINER=`root`@`localhost` */ /*!50003 TRIGGER `update_user` AFTER UPDATE ON `user` FOR EACH ROW BEGIN if NEW.updated_by != OLD.updated_by THEN if NEW.updated_by != 'classifier' THEN update prediction set evaluator = 1 where user_name = NEW.user_name; else update prediction set evaluator = 0 where user_name = NEW.user_name; End IF; END if; END */;;

DELIMITER ;
/*!50003 SET SESSION SQL_MODE=@SAVE_SQL_MODE*/;


--
-- Table structure for table `user_wiki`
--

SET character_set_client = @saved_cs_client;
DROP TABLE IF EXISTS `user_wiki`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `user_wiki` (
  `user_name` varchar(30) NOT NULL,
  `user_wiki` text,
  `date` DATETIME,
  PRIMARY KEY  (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `useruser_similarity`
--

DROP TABLE IF EXISTS `useruser_similarity`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `useruser_similarity` (
  `u1` varchar(255) NOT NULL default '',
  `u2` varchar(255) NOT NULL default '',
  `sim` float default NULL,
  `measure_id` tinyint(4) default NULL,
  PRIMARY KEY  (`u1`,`u2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `useruser_similarity2`
--

DROP TABLE IF EXISTS `useruser_similarity2`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `useruser_similarity2` (
  `u1` varchar(255) NOT NULL default '',
  `u2` varchar(255) NOT NULL default '',
  `sim` float default NULL,
  `measure_id` tinyint(4) default NULL,
  PRIMARY KEY  (`u1`,`u2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `useruser_similarity_measures`
--

DROP TABLE IF EXISTS `useruser_similarity_measures`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `useruser_similarity_measures` (
  `measure_id` tinyint(4) NOT NULL,
  `measure` varchar(100) default NULL,
  `description` text,
  PRIMARY KEY  (`measure_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `discussion`
--

DROP TABLE IF EXISTS `discussion`;
SET @saved_cs_client     = @@character_set_client;
CREATE TABLE `discussion` (
  `discussion_id` int(11) NOT NULL,
  `interHash` varchar(32) NOT NULL DEFAULT '',
  `hash` varchar(32) NOT NULL DEFAULT '',
  `type` tinyint(2) NOT NULL DEFAULT '0',
  `text` text,
  `user_name` varchar(30) NOT NULL DEFAULT '',
  `parent_hash` varchar(32) NULL DEFAULT NULL,
  `rating` double DEFAULT NULL,
  `anonymous` tinyint(1) DEFAULT '0',
  `group` int(10) default '0',
  `date` timestamp NULL DEFAULT NULL,
  `change_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`interHash`, `hash`, `user_name`, `group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `log_discussion`
--

DROP TABLE IF EXISTS `log_discussion`;
SET @saved_cs_client     = @@character_set_client;
CREATE TABLE `log_discussion` (
  `discussion_id` int(11) DEFAULT NULL,
  `interHash` varchar(32) DEFAULT NULL,
  `hash` varchar(32) NOT NULL DEFAULT '',
  `text` text,
  `user_name` varchar(30) DEFAULT NULL,
  `type` tinyint(2) NOT NULL DEFAULT '0',
  `parent_hash` varchar(32) NULL DEFAULT NULL,
  `rating` double DEFAULT NULL,
  `anonymous` tinyint(1) DEFAULT '0',
  `group` int(10) default '0',
  `date` timestamp NULL DEFAULT NULL,
  `change_date` timestamp NULL DEFAULT NULL,
  `log_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `review_ratings_cache`
--

DROP TABLE IF EXISTS `review_ratings_cache`;
SET @saved_cs_client     = @@character_set_client;
CREATE TABLE `review_ratings_cache` (
  `interHash` varchar(32) NOT NULL DEFAULT '',
  `number_of_ratings` int(11) DEFAULT NULL,
  `rating_arithmetic_mean` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`interHash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;


--
-- Table structure for table `weights`
--

DROP TABLE IF EXISTS `weights`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `weights` (
  `id` int(11) default NULL,
  `weight` double default NULL,
  `dim` int(11) default NULL,
  `item` varchar(255) default NULL,
  `itemtype` int(11) default NULL,
  CONSTRAINT `weights_ibfk_1` FOREIGN KEY (`id`) REFERENCES `rankings` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;
