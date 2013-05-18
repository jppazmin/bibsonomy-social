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


INSERT INTO `ids` VALUES 
	(0,0,'content_id'),
	(1,0,'tas id'),
	(2,0,'relation id'),
	(3,0,'question id'),
	(4,0,'cycle id'),
	(5,0,'extended_fields_id'),
	(7,0,'scraper_metadata_id'),
	(12,0,'grouptas id'),
	(14,0,'message_id'),
	(15,0,'discussion_id'),
	(16,0,'synchronization_id');

INSERT INTO `groupids` VALUES 
	('public', -2147483648,1,0),
	('private',-2147483647,1,0),
	('friends',-2147483646,1,0),
	('public', 0,1,0),
	('private',1,1,0),
	('friends',2,1,0);


