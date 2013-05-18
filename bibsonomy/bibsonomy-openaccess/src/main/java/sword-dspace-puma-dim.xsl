<?xml version="1.0" encoding="utf-8"?>
<!--


     BibSonomy-OpenAccess - Check Open Access Policies for Publications

     Copyright (C) 2006 - 2011 Knowledge & Data Engineering Group,
                               University of Kassel, Germany
                               http://www.kde.cs.uni-kassel.de/

     This program is free software; you can redistribute it and/or
     modify it under the terms of the GNU Lesser General Public License
     as published by the Free Software Foundation; either version 2
     of the License, or (at your option) any later version.

     This program is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU Lesser General Public License for more details.

     You should have received a copy of the GNU Lesser General Public License
     along with this program; if not, write to the Free Software
     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

-->

<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:dim="http://www.dspace.org/xmlns/dspace/dim"
        xmlns:puma="http://puma.uni-kassel.de/2010/11/PUMA-SWORD"
        xmlns:mets="http://www.loc.gov/METS/"
        version="1.0">

<!-- NOTE: This stylesheet is a work in progress, and does not
     cover all aspects of bibtex schema.
     BTW: who knows a bibtex schema?
-->

	<!-- nicer output -->
	<xsl:output indent="yes"/>


	<!-- Catch all.  This template will ensure that nothing
	     other than explicitly what we want to xwalk will be dealt
	     with -->
	<xsl:template match="text()"></xsl:template> 


<!-- This stylesheet converts incoming DC metadata in a SWAP
     profile into the DSpace Interal Metadata format (DIM) -->

    <!-- match the top level bibtex-entry element and kick off the
         template matching process -->
    <xsl:template match="puma:PumaPost">
    	<dim:dim>
    		<xsl:apply-templates/>
	    <!-- dc.identifier.isbn -->
		<xsl:if test="./@ISBN">
	    	<dim:field mdschema="dc" element="identifier" qualifier="isbn">
	    		<xsl:value-of select="@ISBN"/>
	    	</dim:field>
		</xsl:if>
	
	    <!-- dc.identifier.issn -->
		<xsl:if test="./@ISSN">
	    	<dim:field mdschema="dc" element="identifier" qualifier="issn">
	    		<xsl:value-of select="@ISSN"/>
	    	</dim:field>
		</xsl:if>

	    <!-- dc.description.everything -->
		<xsl:if test="./@description">
	    	<dim:field mdschema="dc" element="description" qualifier="everything">
	    		<xsl:value-of select="@description"/>
	    	</dim:field>
		</xsl:if>
    	</dim:dim>

    </xsl:template>
    
    <xsl:template match="bibtex">

	    <!-- dc.title -->
		<xsl:if test="./@title">
	    	<dim:field mdschema="dc" element="title">
	    		<xsl:value-of select="@title"/>
	    	</dim:field>
		</xsl:if>
	
		<xsl:choose>
			<xsl:when test="./@entrytype='article'"><dim:field mdschema="dc" element="type">Aufsatz</dim:field></xsl:when>
			<xsl:when test="./@entrytype='book'"><dim:field mdschema="dc" element="type">Buch</dim:field></xsl:when>
			<xsl:when test="./@entrytype='mastersthesis'"><dim:field mdschema="dc" element="type">Diplomarbeit</dim:field></xsl:when>
			<xsl:when test="./@entrytype='misc'"><dim:field mdschema="dc" element="type">Sonstige</dim:field></xsl:when>
			<xsl:when test="./@entrytype='phdthesis'"><dim:field mdschema="dc" element="type">Dissertation</dim:field></xsl:when>
			<xsl:otherwise>misc</xsl:otherwise>
		</xsl:choose>
 	
	    <!-- dc.contributor.editor -->
		<xsl:if test="./@editor">
	    	<dim:field mdschema="dc" element="contributor" qualifier="editor">
	    		<xsl:value-of select="@editor"/>
	    	</dim:field>
		</xsl:if>
	
	    <!-- dc.publisher -->
		<xsl:if test="./@publisher">
	    	<dim:field mdschema="dc" element="publisher">
	    		<xsl:value-of select="@publisher"/>
	    	</dim:field>
		</xsl:if>
	
	    <!-- dc.date -->
		<xsl:if test="./@year">
	    	<dim:field mdschema="dc" element="date">
	    		<xsl:value-of select="@year"/>
			<xsl:if test="./@month">-<xsl:value-of select="@month"/></xsl:if>
			<xsl:if test="./@day">-<xsl:value-of select="@day"/></xsl:if>
	    	</dim:field>

	
		<!-- WORKAROUND -->
    		<dim:field mdschema="dc" element="description" qualifier="everything">
    			<xsl:text>Datum der Veröffentlichung: </xsl:text>
	    		<xsl:value-of select="@year"/>
			<xsl:if test="./@month">-<xsl:value-of select="@month"/></xsl:if>
			<xsl:if test="./@day">-<xsl:value-of select="@day"/></xsl:if>
		</dim:field>

		</xsl:if>
	
	    <!-- dc.identifier.uri -->
		<xsl:if test="./@href">
	    	<dim:field mdschema="dc" element="identifier" qualifier="uri">
	    		<xsl:value-of select="@href"/>
	    	</dim:field>
		</xsl:if>
	
	    <!-- dc.relation.ispartof .... journal oder booktitle, je nach publikationstyp? ...exklusiv oder? -->
		<xsl:if test="./@journal">
	    	<dim:field mdschema="dc" element="relation" qualifier="ispartof">
	    		<xsl:value-of select="@journal"/>
	    	</dim:field>
		</xsl:if>
	
	    <!-- dc.relation.ispartof .... journal oder booktitle, je nach publikationstyp? ...exklusiv oder? -->
		<xsl:if test="./@booktitle">
	    	<dim:field mdschema="dc" element="relation" qualifier="ispartof">
	    		<xsl:value-of select="@booktitle"/>
	    	</dim:field>
		</xsl:if>
	
	    <!-- dc.description.abstract -->
		<xsl:if test="./@bibtexAbstract">
	    	<dim:field mdschema="dc" element="description" qualifier="abstract">
	    		<xsl:value-of select="@bibtexAbstract"/>
	    	</dim:field>
		</xsl:if>
	
	</xsl:template>

    <!-- dc.subject -->
    <xsl:template match="tag">
    	<dim:field mdschema="dc" element="subject">
    		<xsl:value-of select="@name"/>
    	</dim:field>
    </xsl:template>

    <!-- dc.contributor.corporatename -->
    <xsl:template match="puma:examinstitution">
    	<dim:field mdschema="dc" element="contributor" qualifier="corporatename">
    		<xsl:value-of select="."/>
    	</dim:field>
    </xsl:template>

    <!-- dc.contributor.referee -->
    <xsl:template match="puma:examreferee">
    	<dim:field mdschema="dc" element="contributor" qualifier="referee">
    		<xsl:value-of select="."/>
    	</dim:field>
    </xsl:template>

    <!-- dc.date.examination -->
    <xsl:template match="puma:phdoralexam">
    	<dim:field mdschema="dc" element="date" qualifier="examination">
    		<xsl:value-of select="."/>
    	</dim:field>
	
	<!-- WORKAROUND -->
    	<dim:field mdschema="dc" element="description" qualifier="everything">
    		<xsl:text>Tag der mundlichen Prüfung: </xsl:text>
    		<xsl:value-of select="."/>
		</dim:field>
    </xsl:template>

    <!-- dc.description.sponsorship -->
    <xsl:template match="puma:sponsors">
    	<dim:field mdschema="dc" element="description" qualifier="sponsorship">
    		<xsl:value-of select="."/>
    	</dim:field>
    </xsl:template>

    <!-- dc.title.alternative -->
    <xsl:template match="puma:additionaltitle">
    	<dim:field mdschema="dc" element="title" qualifier="alternative">
    		<xsl:value-of select="."/>
    	</dim:field>
    </xsl:template>

    <!-- dc.contributor.author -->
    <xsl:template match="puma:author">
    	<dim:field mdschema="dc" element="contributor" qualifier="author">
    		<xsl:value-of select="."/>
    	</dim:field>
    </xsl:template>

    <xsl:template match="puma:classification">
      <xsl:choose>
	    <!-- jel -->
		<xsl:when test="./@name='acm'">
	    	<dim:field mdschema="dc" element="subject" qualifier="{@name}">
	    		<xsl:value-of select="@value"/>
	    	</dim:field>
		</xsl:when>

	    <!-- jel -->
		<xsl:when test="./@name='jel'">
	    	<dim:field mdschema="dc" element="subject" qualifier="{@name}">
	    		<xsl:value-of select="@value"/>
	    	</dim:field>
		</xsl:when>

	    <!-- ddc -->
		<xsl:when test="./@name='ddc'">
	    	<dim:field mdschema="dc" element="subject" qualifier="{@name}">
	    		<xsl:value-of select="@value"/>
	    	</dim:field>

		<!-- WORKAROUND -->
    		<dim:field mdschema="dc" element="description" qualifier="everything">
    			<xsl:text>DDC: </xsl:text>
	    		<xsl:value-of select="@value"/>
			</dim:field>
		</xsl:when>

	    <!-- other -->
        <xsl:otherwise>
          <xsl:if test="./@name">
	    	<dim:field mdschema="dc" element="subject" qualifier="other">
	    		<xsl:value-of select="@name"/>
	    		<xsl:text> </xsl:text>
	    		<xsl:value-of select="@value"/>
	    	</dim:field>
          </xsl:if>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:template>

	<!-- puma user who submits publication -->
    <xsl:template match="puma:user">
		<xsl:if test="./@id">
	    	<dim:field mdschema="dc" element="submitter" qualifier="id">
	    		<xsl:value-of select="@id"/>
	    	</dim:field>
		</xsl:if>
		
		<xsl:if test="./@email">
	    	<dim:field mdschema="dc" element="submitter" qualifier="email">
	    		<xsl:value-of select="@email"/>
	    	</dim:field>
		</xsl:if>
		
		<xsl:if test="./@realname">
	    	<dim:field mdschema="dc" element="submitter" qualifier="realname">
	    		<xsl:value-of select="@realname"/>
	    	</dim:field>
		</xsl:if>
		
		<xsl:if test="./@name">
	    	<dim:field mdschema="dc" element="submitter" qualifier="name">
	    		<xsl:value-of select="@name"/>
	    	</dim:field>
		</xsl:if>
		
    	<dim:field mdschema="dc" element="description" qualifier="everything">
    		<xsl:text>Puma-Nutzer: </xsl:text>
		<xsl:if test="./@realname">
		    		<xsl:value-of select="@realname"/>
			</xsl:if>
			
			<xsl:if test="./@email">
		    		<xsl:text> </xsl:text><xsl:value-of select="@email"/>
			</xsl:if>
			
			<xsl:if test="./@id">
		    		<xsl:text> (</xsl:text><xsl:value-of select="@id"/><xsl:text>)</xsl:text>
			</xsl:if>
    	</dim:field>


		
    </xsl:template>


</xsl:stylesheet>