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

package org.bibsonomy.common.enums;

/**
 * @author Jens Illig
 * @version $Id: HashID.java,v 1.13 2011-04-29 06:36:54 bibsonomy Exp $
 */
public enum HashID {

	/** some special hash. Try to use INTRA_HASH or INTER_HASH instead */
	SIM_HASH0(0),
	/** some special hash. Try to use INTRA_HASH or INTER_HASH instead */
	SIM_HASH1(1),
	/** some special hash. Try to use INTRA_HASH or INTER_HASH instead */
	SIM_HASH2(2),
	/** some special hash. Try to use INTRA_HASH or INTER_HASH instead */
	SIM_HASH3(3),
	/** some special default hash. Try to use INTRA_HASH or INTER_HASH instead */
	SIM_HASH(SIM_HASH1),
	/** hash over more fields */
	INTRA_HASH(SIM_HASH2),
	/** hash over less fields */
	INTER_HASH(SIM_HASH);

	private final int id;

	private HashID(final int id) {
		this.id = id;
	}

	private HashID(final HashID id) {
		this.id = id.getId();
	}

	/**
	 * @return constant value behind the symbol
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @param simHash
	 *            constant value of the HashID symbol to retrieve
	 * @return the corresponding simhash.
	 */
	public static HashID getSimHash(final int simHash) {
		switch (simHash) {
		case 0:
			return SIM_HASH0;
		case 1:
			return SIM_HASH1;
		case 2:
			return SIM_HASH2;
		case 3:
			return SIM_HASH3;
		default:
			throw new RuntimeException("SimHash " + simHash + " doesn't exist.");
		}
	}
	
	/**
	 * @return an HashID array that contains all HashIDs
	 */
	public static HashID[] getAllHashIDs() {
		return new HashID[] { SIM_HASH0, SIM_HASH1, SIM_HASH2, SIM_HASH3 };
	}

	/**
	 * @return an integer array that contains all ids.
	 */
	public static int[] getHashRange() {
		return new int[] { 0, 1, 2, 3 };
	}
}