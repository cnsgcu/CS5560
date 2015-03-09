package com.ibm.cloudoe.labs;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;

@Path("/transform")
public class _7_
{
	private static final Logger LOGGER = Logger.getLogger(_7_.class.getName());
	
	private static final Random rand = new Random();
	private static final WordNetter netter = new WordNetter();

	@Context
	private static ServletContext context;

	private static POSModel taggerModel;
	private static ChunkerModel chunkerModel ;
	private static TokenizerModel tokenizerModel;

	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String rewrite(String sentence) throws InvalidFormatException, IOException
	{
		LOGGER.info("Rewriting - " + sentence);
		
		final POSTaggerME tagger = getTagger();
		final ChunkerME chunker = getChunker();
		final TokenizerME tokenizer = getTokenizer();
		
		final String[] words = tokenizer.tokenize(sentence);
		final String[] tags = tagger.tag(words);
//		final Span[] chunks = chunker.chunkAsSpans(words, tags);
//		final String[] chunkStrings = Span.spansToStrings(chunks, words);
		
		for (int i = 0; i < tags.length; i++) {
			if (tags[i].equalsIgnoreCase("JJ") || tags[i].equalsIgnoreCase("JJR") || tags[i].equalsIgnoreCase("JJS")) {
				final List<String> synonyms = netter.synonyms(words[i]).get("a");
				
				if (synonyms != null && !synonyms.isEmpty()) {
					words[i] = synonyms.get(rand.nextInt(synonyms.size()));					
				}
			} else if (tags[i].equalsIgnoreCase("RB") || tags[i].equalsIgnoreCase("RBR") || tags[i].equalsIgnoreCase("RBS")) {
				final List<String> synonyms = netter.synonyms(words[i]).get("r");
				
				if (synonyms != null && !synonyms.isEmpty()) {
					words[i] = synonyms.get(rand.nextInt(synonyms.size()));					
				}
			}
		}
		
		String msg = "";
		for (int i = 0; i < words.length; i++) {
			msg += words[i] + " ";
		}
		
		return msg;
	}
	
	private TokenizerME getTokenizer() throws InvalidFormatException, IOException
	{
		if (tokenizerModel == null) {
			final InputStream tokenizerIS = context.getResourceAsStream("/models/en-token.bin");		
			tokenizerModel = new TokenizerModel(tokenizerIS);
		}

		return new TokenizerME(tokenizerModel);
	}
	
	private POSTaggerME getTagger() throws InvalidFormatException, IOException
	{
		if (taggerModel == null) {
			final InputStream taggerIS = context.getResourceAsStream("/models/en-pos-maxent.bin");
			taggerModel = new POSModel(taggerIS);
		}

		return new POSTaggerME(taggerModel);
	}
	
	private ChunkerME getChunker() throws InvalidFormatException, IOException
	{
		if (chunkerModel == null) {
			final InputStream chunkerIS = context.getResourceAsStream("/models/en-chunker.bin");
			chunkerModel = new ChunkerModel(chunkerIS);
		}

		return new ChunkerME(chunkerModel);
	}
}