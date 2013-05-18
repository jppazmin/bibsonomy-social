/**
 *
 *  BibSonomy-Common - Common things (e.g., exceptions, enums, utils, etc.)
 *
 *  Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.bibsonomy.util.file;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bibsonomy.util.StringUtils;


/**
 * @author rja
 * @version $Id: FileUtil.java,v 1.14 2011-04-29 06:36:49 bibsonomy Exp $
 */
public class FileUtil {
	/**
	 * The pattern extracts the extension of a file.
	 */
	private static final Pattern fileExtensionPattern = Pattern.compile("(.+)\\.(.+)");
	
	private static final SimpleDateFormat RANDOM_FILE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Constructs the file path of a document
	 * 
	 * @param filePath - the absolute path to the document directory in the file system
	 * @param fileName - the file name of the document (an MD5 hash) 
	 * 
	 * @return The absolute path of the document on the file system.
	 */
	public static String getFilePath(final String filePath, final String fileName) {
		return getFileDir(filePath, fileName) + fileName;
	}

	/**
	 * Constructs the directory path of the file using the first two digits of
	 * the file name.
	 * 
	 * @param filePath - the absolute path to the document directory in the file system
	 * @param fileName - the file name of the document (an MD5 hash)
	 * @return The directory of the file
	 */
	public static String getFileDir(final String filePath, final String fileName) {
		return filePath + fileName.substring(0, 2) + "/";
	}
	
	/**
	 * Generates random hash for file
	 * @param fileName
	 * @return The hash for the file
	 */
	public static String getRandomFileHash(String fileName) {
		return StringUtils.getMD5Hash(fileName + Math.random() + RANDOM_FILE_DATE_FORMAT.format(new Date()));
	}
	
	/**
	 * Depending on the extension of the file, returns the correct MIME content
	 * type. NOTE: the method looks only at the name of the file not at the
	 * content!
	 * 
	 * @param filename
	 *            - name of the file.
	 * @return - the MIME content type of the file.
	 */
	public static String getContentType(final String filename) {
		if (StringUtils.matchExtension(filename, "ps")) {
			return "application/postscript";
		} else if (StringUtils.matchExtension(filename, "pdf")) {
			return "application/pdf";
		} else if (StringUtils.matchExtension(filename, "txt")) {
			return "text/plain";
		} else if (StringUtils.matchExtension(filename, "djv", "djvu")) {
			return "image/vnd.djvu";
		} else if (StringUtils.matchExtension(filename, "jpg", "jpeg")) {
			return "image/jpeg";
		} else if (StringUtils.matchExtension(filename, "png")) {
			return "image/png";			
		} else {
			return "application/octet-stream";
		}
	}
	
	/**
	 * Extracts the extension of a file (without ".").
	 * 
	 * If no extension is found, "" is returned;
	 * 
	 * @param filename
	 * @return The extension of the given file.
	 */
	public static String getFileExtension(final String filename) {
		final Matcher m = fileExtensionPattern.matcher(filename);
		if (m.find())
			return m.group(2).toLowerCase();
		return "";
	}

}
