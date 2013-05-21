--
--
--  BibSonomy-Recommender - Various methods to provide recommendations for BibSonomy
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

DROP TABLE IF EXISTS log_recommender;
CREATE TABLE log_recommender(
	query_id BIGINT NOT NULL AUTO_INCREMENT,
	post_id INT NOT NULL DEFAULT '-1',
	user_name VARCHAR(30) NOT NULL,
	date TIMESTAMP NOT NULL,
	content_type TINYINT(1) UNSIGNED NOT NULL DEFAULT '0',
	`timeout` INT(5) DEFAULT '1000',
	PRIMARY KEY (query_id),
	KEY `post_id_user_name_date` (`post_id`,`user_name`,`date`)
) DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS recommender_postmap;
CREATE TABLE recommender_postmap(
	post_id INT NOT NULL,
	user_name VARCHAR(30) NOT NULL,
	date DATETIME NOT NULL,
	hash char(32) NOT NULL,
	PRIMARY KEY (post_id, user_name, date)
) DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS recommender_result;
CREATE TABLE recommender_result(
    result_id BIGINT NOT NULL AUTO_INCREMENT,
	query_id BIGINT NOT NULL,
	setting_id BIGINT NOT NULL,
	rec_latency INT,
	score DOUBLE NOT NULL,
	confidence DOUBLE NOT NULL,
	tag VARCHAR(255) NOT NULL,
   PRIMARY KEY (result_id),
   UNIQUE KEY (query_id, setting_id, tag)
) DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS recommender_recommendations;
CREATE TABLE recommender_recommendations(
	query_id BIGINT NOT NULL,
	score DOUBLE NOT NULL,
	confidence DOUBLE NOT NULL,
	tag VARCHAR(255) NOT NULL,
	PRIMARY KEY (query_id, tag)
) DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS recommender_preset;
CREATE TABLE recommender_preset(
	query_id VARCHAR(20) NOT NULL,
	setting_id BIGINT NOT NULL,
	tag VARCHAR(30) NOT NULL,
	PRIMARY KEY (query_id, setting_id, tag)
) DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS recommender_querymap;
CREATE TABLE recommender_querymap(
	query_id BIGINT NOT NULL,
	setting_id BIGINT NOT NULL,
	PRIMARY KEY (query_id, setting_id)
) DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS recommender_settings;
CREATE TABLE recommender_settings(
	setting_id BIGINT NOT NULL AUTO_INCREMENT,
	rec_id VARCHAR(255) NOT NULL,
	rec_meta BLOB,
	rec_descr VARCHAR(255),
	PRIMARY KEY (setting_id)
) DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS recommender_status;
CREATE TABLE `recommender_status` (
  `setting_id` bigint(20) unsigned NOT NULL,
  `url` varchar(255) default NULL,
  `status` int(1) default NULL,
  PRIMARY KEY  (`setting_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS recommender_selectors;
CREATE TABLE recommender_selectors(
	selector_id BIGINT NOT NULL AUTO_INCREMENT,
	selector_name VARCHAR(50) NOT NULL,
	selector_meta BLOB,
	PRIMARY KEY (selector_id)
) DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS recommender_selectormap;
CREATE TABLE recommender_selectormap(
	query_id BIGINT NOT NULL,
	selector_id BIGINT NOT NULL,
	PRIMARY KEY (query_id, selector_id)
) DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS recommender_selection;
CREATE TABLE recommender_selection (
	query_id BIGINT(20) UNSIGNED NOT NULL DEFAULT '0', 
	setting_id BIGINT(20) UNSIGNED NOT NULL DEFAULT '0', 
	PRIMARY KEY (query_id, setting_id)
) DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `recommender_bookmark`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `recommender_bookmark` (
  `content_id` int(10) unsigned NOT NULL default '0',
  `book_url_hash` varchar(32) default '',
  `book_url` text,
  `book_description` text,
  `book_extended` text,
  `group` int(10) default '0',
  `date` datetime default '1815-12-10 00:00:00',
  `user_name` varchar(30) default '',
  `to_bib` tinyint(3) default '0',
  `change_date` timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `rating` tinyint(3) default '0',
  PRIMARY KEY  (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

DROP TABLE IF EXISTS `recommender_bibtex`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `recommender_bibtex` (
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
  `pages` varchar(15) default NULL,
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
  PRIMARY KEY  (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

