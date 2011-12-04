/*
 * 12/04/2011
 *
 * TokenTypes.java - All token types supported by RSyntaxTextArea.
 * Copyright (C) 2011 Robert Futrell
 * robert_futrell at users.sourceforge.net
 * http://fifesoft.com/rsyntaxtextarea
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA.
 */
package org.fife.ui.rsyntaxtextarea;


/**
 * All token types supported by RSyntaxTextArea.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public interface TokenTypes {

	// NOTE: All valid token types are >= 0, so extensions of the TokenMaker
	// class are free to internally use all ints < 0 ONLY for "end-of-line"
	// style markers; they are ignored by painting implementations.

	public static final int NULL							= 0;	// Marks EOL with no multiline token at end.

	public static final int COMMENT_EOL						= 1;
	public static final int COMMENT_MULTILINE				= 2;
	public static final int COMMENT_DOCUMENTATION			= 3;
	public static final int COMMENT_KEYWORD					= 4;

	public static final int RESERVED_WORD					= 5;

	public static final int FUNCTION						= 6;

	public static final int LITERAL_BOOLEAN					= 7;
	public static final int LITERAL_NUMBER_DECIMAL_INT		= 8;
	public static final int LITERAL_NUMBER_FLOAT			= 9;
	public static final int LITERAL_NUMBER_HEXADECIMAL		= 10;
	public static final int LITERAL_STRING_DOUBLE_QUOTE		= 11;
	public static final int LITERAL_CHAR					= 12;
	public static final int LITERAL_BACKQUOTE				= 13;

	public static final int DATA_TYPE						= 14;

	public static final int VARIABLE						= 15;

	public static final int ANNOTATION						= 16;

	public static final int IDENTIFIER						= 17;

	public static final int WHITESPACE						= 18;

	public static final int SEPARATOR						= 19;

	public static final int OPERATOR						= 20;

	public static final int PREPROCESSOR					= 21;

	public static final int MARKUP_TAG_DELIMITER			= 22;
	public static final int MARKUP_TAG_NAME					= 23;
	public static final int MARKUP_TAG_ATTRIBUTE			= 24;
	public static final int MARKUP_TAG_ATTRIBUTE_VALUE		= 25;

	public static final int ERROR_IDENTIFIER				= 26;
	public static final int ERROR_NUMBER_FORMAT				= 27;
	public static final int ERROR_STRING_DOUBLE 			= 28;
	public static final int ERROR_CHAR						= 29;

	public static final int NUM_TOKEN_TYPES					= 30;

}
