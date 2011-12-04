/*
 * 10/30/2011
 *
 * Theme.java - A color theme for RSyntaxTextArea.
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

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import javax.swing.JViewport;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import org.fife.io.UnicodeWriter;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;


/**
 * A theme is a set of fonts and colors to use to style RSyntaxTextArea.
 * Themes are defined in XML files that are validated against
 * <code>themes.dtd</code>.  This provides applications and other consumers an
 * easy way to style RSyntaxTextArea without having to use the API.<p>
 * 
 * Sample themes are included in the source tree under the <code>/themes</code>
 * folder, but are not a part of the built RSyntaxTextArea jar.  Hosting
 * applications are free to ship and use these themes as-is, modify them, or
 * create their own.<p>
 *
 * Note that to save a <code>Theme</code> via {@link #save(OutputStream)},
 * you must currently create a <code>Theme</code> from a text area wrapped in
 * an <code>RTextScrollPane</code>, so that the color information for the
 * gutter can be retrieved.
 *
 * @author Robert Futrell
 * @version 1.0
 */
public class Theme {

	private Font baseFont;
	private Color bgColor;
	private Color caretColor;
	private Color selectionBG;
	private boolean selectionRoundedEdges;
	private Color currentLineHighlight;
	private boolean fadeCurrentLineHighlight;
	private Color marginLineColor;
	private Color markAllHighlightColor;
	private Color markOccurrencesColor;
	private Color matchedBracketFG;
	private Color matchedBracketBG;
	private boolean matchedBracketAnimate;

	private SyntaxScheme scheme;

	private Color gutterBorderColor;
	private Color lineNumberColor;
	private Color foldIndicatorFG;
	private Color foldBG;


	/**
	 * Private constructor, used when loading from a stream.
	 */
	private Theme() {
	}


	/**
	 * Creates a theme from an RSyntaxTextArea.  It should be contained in
	 * an <code>RTextScrollPane</code> to get all gutter color information.
	 *
	 * @param textArea The text area.
	 */
	public Theme(RSyntaxTextArea textArea) {

		baseFont = textArea.getFont();
		bgColor = textArea.getBackground();
		caretColor = textArea.getCaretColor();
		selectionBG = textArea.getSelectionColor();
		selectionRoundedEdges = textArea.getRoundedSelectionEdges();
		currentLineHighlight = textArea.getCurrentLineHighlightColor();
		fadeCurrentLineHighlight = textArea.getFadeCurrentLineHighlight();
		marginLineColor = textArea.getMarginLineColor();
		markAllHighlightColor = textArea.getMarkAllHighlightColor();
		markOccurrencesColor = textArea.getMarkOccurrencesColor();
		matchedBracketBG = textArea.getMatchedBracketBGColor();
		matchedBracketFG = textArea.getMatchedBracketBorderColor();
		matchedBracketAnimate = textArea.getAnimateBracketMatching();

		scheme = textArea.getSyntaxScheme();

		if (textArea.getParent() instanceof JViewport &&
				textArea.getParent().getParent() instanceof RTextScrollPane) {
			RTextScrollPane scrollPane = (RTextScrollPane)textArea.getParent().getParent();
			Gutter gutter = scrollPane.getGutter();
			bgColor = gutter.getBackground();
			gutterBorderColor = gutter.getBorderColor();
			lineNumberColor = gutter.getLineNumberColor();
			foldIndicatorFG = gutter.getFoldIndicatorForeground();
			foldBG = gutter.getFoldBackground();
		}

	}


	/**
	 * Applies this theme to a text area.
	 *
	 * @param textArea The text area to apply this theme to.
	 */
	public void apply(RSyntaxTextArea textArea) {

		textArea.setFont(baseFont);
		textArea.setBackground(bgColor);
		textArea.setCaretColor(caretColor);
		textArea.setSelectionColor(selectionBG);
		textArea.setRoundedSelectionEdges(selectionRoundedEdges);
		textArea.setCurrentLineHighlightColor(currentLineHighlight);
		textArea.setFadeCurrentLineHighlight(fadeCurrentLineHighlight);
		textArea.setMarginLineColor(marginLineColor);
		textArea.setMarkAllHighlightColor(markAllHighlightColor);
		textArea.setMarkOccurrencesColor(markOccurrencesColor);
		textArea.setMatchedBracketBGColor(matchedBracketBG);
		textArea.setMatchedBracketBorderColor(matchedBracketFG);
		textArea.setAnimateBracketMatching(matchedBracketAnimate);

		textArea.setSyntaxScheme(scheme);

		if (textArea.getParent() instanceof JViewport &&
				textArea.getParent().getParent() instanceof RTextScrollPane) {
			RTextScrollPane scrollPane = (RTextScrollPane)textArea.getParent().getParent();
			Gutter gutter = scrollPane.getGutter();
			gutter.setBackground(bgColor);
			gutter.setBorderColor(gutterBorderColor);
			gutter.setLineNumberColor(lineNumberColor);
			gutter.setFoldIndicatorForeground(foldIndicatorFG);
			gutter.setFoldBackground(foldBG);
		}

	}


	private static final String colorToString(Color c) {
		int color = c.getRGB() & 0xffffff;
		String str = Integer.toHexString(color);
		while (str.length()<6) {
			str = "0" + str;
		}
		return str;
	}


	/**
	 * Loads a theme.
	 *
	 * @param in The input stream to read from.  This will be closed when this
	 *        method returns.
	 * @return The theme.
	 * @throws IOException If an IO error occurs.
	 * @see #save(OutputStream)
	 */
	public static Theme load(InputStream in) throws IOException {

		Theme theme = new Theme();

		BufferedInputStream bin = new BufferedInputStream(in);
		try {
			XmlHandler.load(theme, bin);
		} finally {
			bin.close();
		}

		return theme;

	}


	/**
	 * Saves this theme to an output stream.
	 *
	 * @param out The output stream to write to.
	 * @throws IOException If an IO error occurs.
	 * @see #load(InputStream)
	 */
	public void save(OutputStream out) throws IOException {

		BufferedOutputStream bout = new BufferedOutputStream(out);
		try {

			DocumentBuilder db = DocumentBuilderFactory.newInstance().
					newDocumentBuilder();
			DOMImplementation impl = db.getDOMImplementation();

			Document doc = impl.createDocument(null, "RSyntaxTheme", null);
			Element root = doc.getDocumentElement();
			root.setAttribute("version", "1.0");

			Element elem = doc.createElement("baseFont");
			if (!baseFont.getFamily().equals(
					RSyntaxTextArea.getDefaultFont().getFamily())) {
				elem.setAttribute("family", baseFont.getFamily());
			}
			elem.setAttribute("size", Integer.toString(baseFont.getSize()));
			root.appendChild(elem);

			elem = doc.createElement("background");
			elem.setAttribute("color", colorToString(bgColor));
			root.appendChild(elem);

			elem = doc.createElement("caret");
			elem.setAttribute("color", colorToString(caretColor));
			root.appendChild(elem);

			elem = doc.createElement("selection");
			elem.setAttribute("bg", colorToString(selectionBG));
			elem.setAttribute("roundedEdges", Boolean.toString(selectionRoundedEdges));
			root.appendChild(elem);

			elem = doc.createElement("currentLineHighlight");
			elem.setAttribute("color", colorToString(currentLineHighlight));
			elem.setAttribute("fade", Boolean.toString(fadeCurrentLineHighlight));
			root.appendChild(elem);

			elem = doc.createElement("marginLine");
			elem.setAttribute("fg", colorToString(marginLineColor));
			root.appendChild(elem);

			elem = doc.createElement("markAllHighlight");
			elem.setAttribute("color", colorToString(markAllHighlightColor));
			root.appendChild(elem);

			elem = doc.createElement("markOccurrencesHighlight");
			elem.setAttribute("color", colorToString(markOccurrencesColor));
			root.appendChild(elem);

			elem = doc.createElement("matchedBracket");
			elem.setAttribute("fg", colorToString(matchedBracketFG));
			elem.setAttribute("bg", colorToString(matchedBracketBG));
			elem.setAttribute("animate", Boolean.toString(matchedBracketAnimate));
			root.appendChild(elem);

			elem = doc.createElement("gutterBorder");
			elem.setAttribute("color", colorToString(gutterBorderColor));
			root.appendChild(elem);

			elem = doc.createElement("lineNumbers");
			elem.setAttribute("fg", colorToString(lineNumberColor));
			root.appendChild(elem);

			elem = doc.createElement("foldIndicator");
			elem.setAttribute("fg", colorToString(foldIndicatorFG));
			elem.setAttribute("iconBg", colorToString(foldBG));
			root.appendChild(elem);

			elem = doc.createElement("tokenStyles");
			Field[] fields = TokenTypes.class.getFields();
			for (int i=0; i<fields.length; i++) {
				Field field = fields[i];
				int value = field.getInt(null);
				if (value!=TokenTypes.NUM_TOKEN_TYPES) {
					Style style = scheme.getStyle(value);
					if (style!=null) {
						Element elem2 = doc.createElement("style");
						elem2.setAttribute("token", field.getName());
						Color fg = style.foreground;
						if (fg!=null) {
							elem2.setAttribute("fg", colorToString(fg));
						}
						Color bg = style.background;
						if (bg!=null) {
							elem2.setAttribute("bg", colorToString(bg));
						}
						Font font = style.font;
						if (font!=null) {
							if (!font.getFamily().equals(
									baseFont.getFamily())) {
								elem2.setAttribute("fontFamily", font.getFamily());
							}
							if (font.getSize()!=baseFont.getSize()) {
								elem2.setAttribute("fontSize", Integer.toString(font.getSize()));
							}
							if (font.isBold()) {
								elem2.setAttribute("bold", "true");
							}
							if (font.isItalic()) {
								elem2.setAttribute("italic", "true");
							}
						}
						if (style.underline) {
							elem2.setAttribute("underline", "true");
						}
						elem.appendChild(elem2);
					}
				}
			}
			root.appendChild(elem);

			DOMSource source = new DOMSource(doc);
			// Use a writer instead of OutputStream to allow pretty printing.
			// See http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6337981
			StreamResult result = new StreamResult(new PrintWriter(
					new UnicodeWriter(bout, "UTF-8")));
			TransformerFactory transFac = TransformerFactory.newInstance();
			Transformer transformer = transFac.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "theme.dtd");
			transformer.transform(source, result);

		} catch (RuntimeException re) {
			throw re; // FindBugs
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException("Error generating XML: " + e.getMessage());
			// When Java 6 is minimum required version
			//throw new IOException("Error generating XML: " + e.getMessage(), e);
		} finally {
			bout.close();
		}

	}


	/**
	 * Returns the color represented by a string.  The input is expected to
	 * be a 6-digit hex string, optionally prefixed by a '$'.  For example,
	 * either of the following:
	 * <pre>
	 * "$00ff00"
	 * "00ff00"
	 * </pre>
	 * will return <code>new Color(0, 255, 0)</code>.
	 *
	 * @param s The string to evaluate.
	 * @return The color.
	 */
	private static final Color stringToColor(String s) {
		if (s!=null && (s.length()==6 || s.length()==7)) {
			if (s.charAt(0)=='$') {
				s = s.substring(1);
			}
			return new Color(Integer.parseInt(s, 16));
		}
		return null;
	}


	/**
	 * Loads a <code>SyntaxScheme</code> from an XML file.
	 */
	private static class XmlHandler extends DefaultHandler {

		private Theme theme;

		public void error(SAXParseException e) throws SAXException {
			throw e;
		}

		public void fatalError(SAXParseException e) throws SAXException {
			throw e;
		}

	    public static void load(Theme theme, InputStream in) throws IOException {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			spf.setValidating(true);
			try {
				SAXParser parser = spf.newSAXParser();
				XMLReader reader = parser.getXMLReader();
				XmlHandler handler = new XmlHandler();
				handler.theme = theme;
				reader.setEntityResolver(handler);
				reader.setContentHandler(handler);
				reader.setDTDHandler(handler);
				reader.setErrorHandler(handler);
				InputSource is = new InputSource(in);
				is.setEncoding("UTF-8");
				reader.parse(is);
			} catch (/*SAX|ParserConfiguration*/Exception se) {
				se.printStackTrace();
				throw new IOException(se.toString());
			}
		}

	    public InputSource resolveEntity(String publicID, 
				String systemID) throws SAXException {
			return new InputSource(getClass().
					getResourceAsStream("/theme.dtd"));
		}

		public void startElement(String uri, String localName, String qName,
								Attributes attrs) {

			if ("background".equals(qName)) {

				String color = attrs.getValue("color");
				if (color!=null) {
					theme.bgColor = stringToColor(color);
				}
				else {
					String img = attrs.getValue("image");
					if (img!=null) {
						throw new IllegalArgumentException("Not yet implemented");
					}
				}
			}

			// The base font to use in the editor.
			else if ("baseFont".equals(qName)) {
				String family = attrs.getValue("family");
				int size = Integer.parseInt(attrs.getValue("size"));
				if (family!=null) {
					theme.baseFont = new Font(family, Font.PLAIN, size);
				}
				else {
					theme.baseFont = RSyntaxTextArea.getDefaultFont();
					theme.baseFont = theme.baseFont.deriveFont(size*1f);
				}
			}

			else if ("caret".equals(qName)) {
				String color = attrs.getValue("color");
				theme.caretColor = stringToColor(color);
			}

			else if ("currentLineHighlight".equals(qName)) {
				String color = attrs.getValue("color");
				theme.currentLineHighlight = stringToColor(color);
				String fadeStr = attrs.getValue("fade");
				boolean fade = Boolean.valueOf(fadeStr).booleanValue();
				theme.fadeCurrentLineHighlight = fade;
			}

			else if ("foldIndicator".equals(qName)) {
				String color = attrs.getValue("fg");
				theme.foldIndicatorFG = stringToColor(color);
				color = attrs.getValue("iconBg");
				theme.foldBG = stringToColor(color);
			}

			else if ("gutterBorder".equals(qName)) {
				String color = attrs.getValue("color");
				theme.gutterBorderColor = stringToColor(color);
			}

			else if ("lineNumbers".equals(qName)) {
				String color = attrs.getValue("fg");
				theme.lineNumberColor = stringToColor(color);
			}

			else if ("marginLine".equals(qName)) {
				String color = attrs.getValue("fg");
				theme.marginLineColor = stringToColor(color);
			}

			else if ("markAllHighlight".equals(qName)) {
				String color = attrs.getValue("color");
				theme.markAllHighlightColor = stringToColor(color);
			}

			else if ("markOccurrencesHighlight".equals(qName)) {
				String color = attrs.getValue("color");
				theme.markOccurrencesColor = stringToColor(color);
			}

			else if ("matchedBracket".equals(qName)) {
				String fg = attrs.getValue("fg");
				theme.matchedBracketFG = stringToColor(fg);
				String bg = attrs.getValue("bg");
				theme.matchedBracketBG = stringToColor(bg);
				String animate = attrs.getValue("animate");
				theme.matchedBracketAnimate = Boolean.valueOf(animate).booleanValue();
			}

			else if ("selection".equals(qName)) {
				String color = attrs.getValue("bg");
				theme.selectionBG = stringToColor(color);
				String roundedStr = attrs.getValue("roundedEdges");
				theme.selectionRoundedEdges = Boolean.valueOf(roundedStr).booleanValue();
			}

			// Start of the syntax scheme definition
			else if ("tokenStyles".equals(qName)) {
				theme.scheme = new SyntaxScheme(theme.baseFont);
			}

			// A style in the syntax scheme
			else if ("style".equals(qName)) {

				String type = attrs.getValue("token");
				Field field = null;
				try {
					field = Token.class.getField(type);
				} catch (RuntimeException re) {
					throw re; // FindBugs
				} catch (Exception e) {
					System.err.println("Invalid token type: " + type);
					return;
				}

				if (field.getType()==int.class) {

					int index = 0;
					try {
						index = field.getInt(theme.scheme);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						return;
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						return;
					}

					String fgStr = attrs.getValue("fg");
					Color fg = stringToColor(fgStr);
					theme.scheme.getStyle(index).foreground = fg;

					String bgStr = attrs.getValue("bg");
					Color bg = stringToColor(bgStr);
					theme.scheme.getStyle(index).background = bg;

					boolean styleSpecified = false;
					boolean bold = false;
					boolean italic = false;
					String boldStr = attrs.getValue("bold");
					if (boldStr!=null) {
						bold = Boolean.valueOf(boldStr).booleanValue();
						styleSpecified = true;
					}
					String italicStr = attrs.getValue("italic");
					if (italicStr!=null) {
						italic = Boolean.valueOf(italicStr).booleanValue();
						styleSpecified = true;
					}
					if (styleSpecified) {
						int style = 0;
						if (bold) { style |= Font.BOLD; }
						if (italic) { style |= Font.ITALIC; }
						theme.scheme.getStyle(index).font =
								theme.baseFont.deriveFont(style);
					}

					String ulineStr = attrs.getValue("underline");
					if (ulineStr!=null) {
						boolean uline= Boolean.valueOf(ulineStr).booleanValue();
						theme.scheme.getStyle(index).underline = uline;
					}

				}

			}

		}

		public void warning(SAXParseException e) throws SAXException {
			throw e;
		}

	}


}