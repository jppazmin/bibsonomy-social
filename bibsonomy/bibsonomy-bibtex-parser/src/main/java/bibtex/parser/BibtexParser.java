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
import java.util.LinkedList;

import bibtex.dom.BibtexAbstractValue;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;

/**
 * The parser will parse the bibtex into a basic AST. Have a look at the
 * different Expanders defined in the bibtex.expansions package if you need more
 * than that.
 * 
 * @author henkel
 */
public final class BibtexParser {

    /**
     * @param throwAllParseExceptions
     *            Setting this to true means that all exceptions will be thrown
     *            immediately. Otherwise, the parser will skip over things it
     *            can't parse and you can use getExceptions to retrieve the
     *            exceptions later.
     */
    public BibtexParser(boolean throwAllParseExceptions) {
        this.throwAllParseExceptions = throwAllParseExceptions;
    }

    private PseudoLexer lexer;

    private BibtexFile bibtexFile;

    private LinkedList exceptions;

    private boolean throwAllParseExceptions;

    private int multipleFieldValuesPolicy = BibtexMultipleFieldValuesPolicy.KEEP_FIRST;

    /**
     * Returns the list of non-fatal exceptions that occured during parsing.
     * Usually, these occur while parsing an entry. Usually, the remainder of
     * the entry will be treated as part of a comment - thus the following entry
     * will be parsed again.
     * 
     * @return List
     */
    public ParseException[] getExceptions() {
        if (exceptions == null)
            return new ParseException[0];
        ParseException[] result = new ParseException[exceptions.size()];
        exceptions.toArray(result);
        return result;
    }

    /**
     * Parses the input into bibtexFile - don't forget to check getExceptions()
     * afterwards (if you don't use throwAllParseExceptions which you can
     * configure in the constructor)...
     * 
     * @param bibtexFile
     * @param input
     * @throws ParseException
     * @throws IOException
     */

    public void parse(BibtexFile bibtexFile, Reader input) throws ParseException, IOException {

        assert bibtexFile != null : "bibtexFile parameter may not be null.";
        assert input != null : "input parameter may not be null.";

        this.lexer = new PseudoLexer(input);
        this.bibtexFile = bibtexFile;
        this.exceptions = new LinkedList();
        while (true) {
            PseudoLexer.Token token = lexer.scanTopLevelCommentOrAtOrEOF();
            switch (token.choice) {
            case 0: // top level comment
                bibtexFile.addEntry(bibtexFile.makeToplevelComment(token.content));
                break;
            case 1: // @ sign
                if (throwAllParseExceptions)
                    parseEntry();
                else {
                    try {
                        parseEntry();
                    } catch (ParseException parseException) {
                        exceptions.add(parseException);
                    }
                }
                break;
            case 2: // EOF
                return;
            }
        }
    }

    private final static char[] EXCEPTION_SET_NAMES = new char[] { '"', '#', '%', '\'', '(', ')', ',', '=', '{', '}' };

    private final static String[] ENTRY_TYPES = new String[] { "string", "preamble", "article", "book", "booklet",
            "conference", "inbook", "incollection", "inproceedings", "manual", "mastersthesis", "misc", "phdthesis",
            "proceedings", "techreport", "unpublished", "periodical" // not
    // really
    // standard
    // but
    // commonly
    // used.
    };

    /**
     *  
     */
    private void parseEntry() throws ParseException, IOException {
        String entryType = lexer.scanEntryTypeName().toLowerCase();
        final int bracketChoice = lexer.scanAlternatives(new char[] { '{', '(' }, false);

        if (entryType.equals("string")) {
            String stringName = lexer.scanLiteral(EXCEPTION_SET_NAMES, true, true);
            lexer.scan('=');
            BibtexAbstractValue value = parseValue();
            bibtexFile.addEntry(bibtexFile.makeMacroDefinition(stringName, value));
        } else if (entryType.equals("preamble")) {
            BibtexAbstractValue value = parseValue();
            bibtexFile.addEntry(bibtexFile.makePreamble(value));
            // read the comment field but try to ignore all content
        } else if (entryType.equals("comment")) {
        	// scan until next @ and then jump back to entry scanning (rja)
        	lexer.scanTopLevelCommentOrAtOrEOF();
            return;
        } else { // all others
            lexer.skipWhitespace();
            String bibkey = (lexer.currentInputChar() == ',') ? "" : lexer.scanLiteral(new char[] { ',' }, true, true);
            final BibtexEntry entry = bibtexFile.makeEntry(entryType, bibkey);
            bibtexFile.addEntry(entry);
            while (true) {
                lexer.enforceNoEof("',' or corresponding closing bracket", true);
                //System.out.println("---------->'"+lexer.currentInputChar()+"'");
                if (lexer.currentInputChar() == ',') {
                    lexer.scan(',');
                    lexer.enforceNoEof("'}' or [FIELDNAME]", true);
                    if (lexer.currentInputChar() == '}')
                        break;
                    String fieldName = lexer.scanLiteral(EXCEPTION_SET_NAMES, true, true);
                    lexer.scan('=');
                    BibtexAbstractValue value = parseValue();
                    switch (this.multipleFieldValuesPolicy) {
                    case BibtexMultipleFieldValuesPolicy.KEEP_FIRST:
                        if (entry.getFieldValue(fieldName) == null)
                            entry.setField(fieldName, value);
                        break;
                    case BibtexMultipleFieldValuesPolicy.KEEP_LAST:
                        entry.setField(fieldName, value);
                        break;
                    case BibtexMultipleFieldValuesPolicy.KEEP_ALL:
                        entry.addFieldValue(fieldName,value);
                        break;
                    default:
                        assert false : "this should not happen.";
                    }
                } else
                    break;
            }
        }

        if (bracketChoice == 0)
            lexer.scan('}');
        else
            lexer.scan(')');
    }

    private static boolean isNumber(String string) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c < '0' || '9' < c)
                return false;
        }
        return true;
    }

    /**
     *  
     */
    private BibtexAbstractValue parseValue() throws ParseException, IOException {
        lexer.enforceNoEof("[STRING] or [STRINGREFERENCE] or [NUMBER]", true);
        char inputCharacter = lexer.currentInputChar();
        BibtexAbstractValue result;

        if (inputCharacter == '"') {
            result = parseQuotedString();
        } else if (inputCharacter == '{') {
            result = parseBracketedString();
        } else {
            String stringContent = lexer.scanLiteral(EXCEPTION_SET_NAMES, false, true).trim();
            if (isNumber(stringContent))
                result = bibtexFile.makeString(stringContent);
            else
                result = bibtexFile.makeMacroReference(stringContent);
        }

        lexer.enforceNoEof("'#' or something else", true);
        if (lexer.currentInputChar() == '#') {
            lexer.scan('#');
            return bibtexFile.makeConcatenatedValue(result, parseValue());
        } else {
            return result;
        }

    }

    /**
     * @return BibtexAbstractValue
     */
    private BibtexAbstractValue parseBracketedString() throws ParseException, IOException {
        StringBuffer buffer = new StringBuffer();
        lexer.scanBracketedString(buffer, false);
        return bibtexFile.makeString(buffer.toString());
    }

    /**
     * @return BibtexAbstractValue
     */
    private BibtexAbstractValue parseQuotedString() throws IOException, ParseException {
        return bibtexFile.makeString(lexer.scanQuotedString());
    }

    /**
     * In bibtex files, fields can have multiple values - this constant
     * determines how to deal with them. Check out BibtexMultipleFieldValuesPolicy for
     * possible values. The default is BibtexMultipleValuesPolicy.KEEP_FIRST.
     * 
     * @see BibtexMultipleFieldValuesPolicy
     * @param multipleFieldValuesPolicy
     *            The multipleFieldValuesPolicy to set.
     */
    public void setMultipleFieldValuesPolicy(int multipleFieldValuesPolicy) {

        assert multipleFieldValuesPolicy == BibtexMultipleFieldValuesPolicy.KEEP_ALL
                || multipleFieldValuesPolicy == BibtexMultipleFieldValuesPolicy.KEEP_FIRST
                || multipleFieldValuesPolicy == BibtexMultipleFieldValuesPolicy.KEEP_LAST :
                    "multipleFieldValuesPolicy parameter must be one of the constants defined in BibtexMultiplefieldValuesPolicy.";

        this.multipleFieldValuesPolicy = multipleFieldValuesPolicy;
    }
}