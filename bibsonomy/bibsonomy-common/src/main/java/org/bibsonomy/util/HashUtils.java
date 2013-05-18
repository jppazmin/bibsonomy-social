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

package org.bibsonomy.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author rja
 * @version $Id: HashUtils.java,v 1.8 2011-04-29 06:36:50 bibsonomy Exp $
 */
public class HashUtils {
	
	/**
	 * Calculates the MD5-Hash of a byte array and returns it encoded as a hex
	 * string of 32 characters length.
	 * 
	 * @param data
	 *            the byte array to be hashed
	 * @return the MD5 hash of s as a 32 character string
	 */
	public static String getMD5Hash(final byte[] data) {
		return HashUtils.getHash(data, "MD5");
	}
	
	/**
	 * Calculates the SHA-1-Hash of a byte array and returns it
	 * 
	 * @param data the byte array to be hashed
	 * @return the SHA-1 hash
	 */
	public static String getSHA1Hash(final byte[] data) {
		return HashUtils.getHash(data, "SHA-1");	
	}

	private static String getHash(final byte[] data, final String hashAlgorithm) {
		if (data == null) return null;
		
		try {
			final MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
			return toHexString(md.digest(data));
		} catch (final NoSuchAlgorithmException e) {
			return null;
		}		
	}
	
	/**
	 * Converts a buffer of bytes into a string of hex values.
	 * 
	 * @param buffer
	 *            array of bytes which should be converted
	 * @return hex string representation of buffer
	 */
	public static String toHexString(byte[] buffer) {
		final StringBuilder result = new StringBuilder();
		for (int i = 0; i < buffer.length; i++) {
			String hex = Integer.toHexString(buffer[i]);
			if (hex.length() == 1) {
				hex = "0" + hex;
			}
			
			result.append(hex.substring(hex.length() - 2));
		}
		
		return result.toString();
	}
}
