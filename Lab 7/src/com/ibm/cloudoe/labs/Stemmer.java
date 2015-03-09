package com.ibm.cloudoe.labs;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Logger;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

public class Stemmer
{
	private static final Logger LOGGER = Logger.getLogger(Stemmer.class.getName());
	
	@SuppressWarnings("deprecation")
	public String strem(String msg) throws IOException
	{
		LOGGER.info("Stremming - " + msg);
		
		TokenStream tokenizer = new StandardTokenizer(Version.LUCENE_4_10_4, new StringReader(msg));
		tokenizer = new StandardFilter(Version.LUCENE_4_10_4, tokenizer);
		tokenizer = new LowerCaseFilter(Version.LUCENE_4_10_4, tokenizer);
		tokenizer = new PorterStemFilter(tokenizer);
		
		final CharTermAttribute token = tokenizer.getAttribute(CharTermAttribute.class);
		
		tokenizer.reset();
		
		final StringBuilder stringBuilder = new StringBuilder();
		
		while (tokenizer.incrementToken()) {
			if (stringBuilder.length() > 0) {
				stringBuilder.append(" ");
			}
			
			stringBuilder.append(token.toString());
		}
		
		tokenizer.end();
		tokenizer.close();
		return stringBuilder.toString();
	}
}
