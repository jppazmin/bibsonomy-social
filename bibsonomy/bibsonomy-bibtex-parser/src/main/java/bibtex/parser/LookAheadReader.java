/**
 *  
 *  BibSonomy-BibTeX-Parser - BibTeX Parser from
 * 		http://www-plan.cs.colorado.edu/henkel/stuff/javabib/
 *   
 *  Copyright (C) 2006 - 2010 Knowledge & Data Engineering Group, 
 *                            University of Kassel, Germany
 *                            http://www.kde.cs.uni-kassel.de/
 *  
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

/*
 * Created on Mar 19, 2003
 * 
 * @author henkel@cs.colorado.edu
 *  
 */
package bibtex.parser;
import java.io.IOException;
import java.io.Reader;
/**
 * This implementation now features a buffer. This is more efficient than
 * relying on BufferedReader since BufferedReader is synchronized.
 * 
 * @author henkel
 */
final class LookAheadReader {
	private final int BUFFERLEN = 512;
	public LookAheadReader(Reader input) throws IOException {
		this.input = input;
		this.bufferPos = -1;
		this.bufferFilledUntil = 0;
		this.buffer = new char[BUFFERLEN];
		this.eof = false;
		this.line = 1;
		this.column = 0;
		step();
	}
	private final Reader input;
	private boolean eof;
	private int line, column;
	private char buffer[];
	private int bufferFilledUntil;
	private int bufferPos;
	public void step() throws IOException {
		if (this.eof)
			return;
		this.bufferPos++;
		if (this.bufferFilledUntil <= this.bufferPos) {
			this.bufferFilledUntil = input.read(buffer);
			if (this.bufferFilledUntil == -1) {
				this.eof = true;
				input.close();
			}
			this.bufferPos = 0;
		}
		char currentChar = this.buffer[bufferPos];
		if (currentChar == '\n') {
			line++;
			column = 0;
		} else {
			column++;
		}
	}
	public char getCurrent() {
		assert (!this.eof);
		return this.buffer[this.bufferPos];
	}
	public boolean eof() {
		return this.eof;
	}
	public int getLine() {
		return line;
	}
	public int getColumn() {
		return column;
	}
}