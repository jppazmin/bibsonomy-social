/**
 *
 *  BibSonomy-Rest-Common - Common things for the REST-client and server.
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

package org.bibsonomy.rest.renderer;

import static org.bibsonomy.util.ValidationUtils.present;

import org.bibsonomy.common.exceptions.InternServerException;

/**
 * The supported rendering formats.
 * 
 * @author Christian Schenk
 * @version $Id: RenderingFormat.java,v 1.6 2011-04-29 07:59:08 nosebrain Exp $
 */
public class RenderingFormat {
	
	/**
	 * the media wildcard
	 */
	public static final String TYPE_WILDCARD = "*";
	
	/**
	 * anything
	 */
	public static final RenderingFormat WILDCARD = new RenderingFormat(TYPE_WILDCARD, TYPE_WILDCARD);
	
	/**
	 * text xml format
	 */
	public static final RenderingFormat XML = new RenderingFormat("text", "xml");	
	
	/**
	 * application xml format
	 */
	public static final RenderingFormat APP_XML = new RenderingFormat("application", "xml");
	
	/**
	 * json format
	 */
	public static final RenderingFormat JSON = new RenderingFormat("application", "json");
	
	/**
	 * pdf format for documents
	 */
	public static final RenderingFormat PDF = new RenderingFormat("application", "pdf");
	

	/**
	 * @param string like <CODE>application/json</CODE> or
	 * <CODE>application/xml; charset=UTF8</CODE>
	 * 			
	 * @return the mediaType for the string
	 */
	public static RenderingFormat getMediaType(final String string) {
		if (!present(string)) return null;
		
		// check if there is a charset given
		final String[] mediaTypeEncoding = string.split(";");
		
		String mediaType = string;
		if (mediaTypeEncoding.length > 1) {
			mediaType = mediaTypeEncoding[0];
		}
		
		final String[] typeSubType = mediaType.split("/");
		
		if (typeSubType.length != 2) {
			throw new IllegalArgumentException(string + " is not a mediaType string representation");
		}
		
		return new RenderingFormat(typeSubType[0], typeSubType[1]);
	}

	/**
	 * @param renderingFormat 
	 * @return the rendering format to the given string.
	 */
	public static RenderingFormat getMediaTypeByFormat(final String renderingFormat) {
		if (renderingFormat == null) throw new InternServerException("RenderingFormat is null");

		final String format = renderingFormat.toLowerCase().trim();
		if ("xml".equals(format)) {
			return XML;
		}
		
		if ("json".equals(format)) {
			return JSON;
		}
		
		if ("pdf".equals(format)){
			return PDF;
		}
		
		return null;
	}
	
	private final String type;
	private final String subtype;

	/**
	 * 
	 * @param type
	 * @param subtype
	 */
	public RenderingFormat(final String type, final String subtype) {
		this.type = type;
		this.subtype = subtype;
	}
	
	/**
	 * @return the mimeType
	 */
	public String getMimeType() {
		return this.type + "/" + this.subtype;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @return the subtype
	 */
	public String getSubtype() {
		return this.subtype;
	}
	
	/**
	 * checks if both renderingformats are compatible
	 * @param other the other renderer
	 * @return <true> iff renderingformat is compatible
	 */
	public boolean isCompatible(final RenderingFormat other) {
        if (other == null)
            return false;
        // both wildcard?
        if (type.equals(TYPE_WILDCARD) || other.type.equals(TYPE_WILDCARD))
            return true;
        
        // type and subtype wildcard?
        if (type.equalsIgnoreCase(other.type) && (subtype.equals(TYPE_WILDCARD) || other.subtype.equals(TYPE_WILDCARD)))
            return true;
        
        // type and subtype?
        return this.type.equalsIgnoreCase(other.type) && this.subtype.equalsIgnoreCase(other.subtype);
    }
	
	/**
     * Checks if the subtype is a wildcard
     * @return true if the subtype is a wildcard  
     */
    public boolean isWildcardSubtype() {
        return TYPE_WILDCARD.equals(this.getSubtype());
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.subtype == null) ? 0 : this.subtype.hashCode());
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RenderingFormat)) {
			return false;
		}
		RenderingFormat other = (RenderingFormat) obj;
		if (this.subtype == null) {
			if (other.subtype != null) {
				return false;
			}
		} else if (!this.subtype.equals(other.subtype)) {
			return false;
		}
		if (this.type == null) {
			if (other.type != null) {
				return false;
			}
		} else if (!this.type.equals(other.type)) {
			return false;
		}
		return true;
	}
	
	@Override
	@Deprecated
	public String toString() {
		return this.subtype.toUpperCase();
	}
}