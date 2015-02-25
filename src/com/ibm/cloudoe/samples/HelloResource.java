package com.ibm.cloudoe.samples;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;

import opennlp.tools.util.Span;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.namefind.TokenNameFinderModel;

@Path("/nlp")
public class HelloResource
{
	@Context
	private static ServletContext context;

	private static TokenizerModel       enModel;
	private static TokenNameFinderModel orgModel;
	private static TokenNameFinderModel dateModel;
	private static TokenNameFinderModel personModel;
	private static TokenNameFinderModel locationModel;
	
	@POST
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public String getInformation(String doc) throws InvalidFormatException, IOException
	{
		Tokenizer tokenizer = new TokenizerME(getEnglishModel());
		String[] tokens = tokenizer.tokenize(doc);

		NameFinderME orgFinder = new NameFinderME(getOrgModel());
		NameFinderME dateFinder = new NameFinderME(getDateModel());
		NameFinderME nameFinder = new NameFinderME(getPersonModel());
		NameFinderME locationFinder = new NameFinderME(getLocationModel());		
		
		nameFinder.clearAdaptiveData();
		Span[] personSpans = nameFinder.find(tokens);
		
		locationFinder.clearAdaptiveData();
		Span[] locationSpans = locationFinder.find(tokens);

		dateFinder.clearAdaptiveData();
		Span[] dateSpans = dateFinder.find(tokens);
		
		orgFinder.clearAdaptiveData();
		Span[] orgSpans = orgFinder.find(tokens);

		for (Span s : personSpans) {
			// TODO: nlp gender detection by name
			tokens[s.getStart()] = "<span class='nlp_person' title='Person name'>" + tokens[s.getStart()];
			tokens[s.getEnd() - 1] = tokens[s.getEnd() - 1] + "</span>";
		}
		
		for (Span s : locationSpans) {
			// TODO: QA fetch location info
			tokens[s.getStart()] = "<span class='nlp_location'  title='Location name'>" + tokens[s.getStart()];
			tokens[s.getEnd() - 1] = tokens[s.getEnd() - 1] + "</span>";
		}
		
		for (Span s : dateSpans) {
			tokens[s.getStart()] = "<span class='nlp_date'  title='Date'>" + tokens[s.getStart()];
			tokens[s.getEnd() - 1] = tokens[s.getEnd() - 1] + "</span>";
		}
		
		for (Span s : orgSpans) {
			// TODO: send stock
			tokens[s.getStart()] = "<span class='nlp_organization'  title='Organization name'>" + tokens[s.getStart()];
			tokens[s.getEnd() - 1] = tokens[s.getEnd() - 1] + "</span>";
		}
		
		return StringUtils.join(tokens, " ");
	}
	
	private TokenizerModel getEnglishModel()
	{
		if (enModel == null) {
			enModel = createTokenizerModel("/models/en-token.bin");
		}
		
		return enModel;
	}
	
	private TokenNameFinderModel getOrgModel()
	{
		if (orgModel == null) {
			orgModel = createNameFinderModel("/models/en-ner-organization.bin");
		}
		
		return orgModel;
	}
	
	private TokenNameFinderModel getDateModel()
	{
		if (dateModel == null) {
			dateModel = createNameFinderModel("/models/en-ner-date.bin");
		}
		
		return dateModel;
	}
	
	private TokenNameFinderModel getPersonModel()
	{
		if (personModel == null) {
			personModel = createNameFinderModel("/models/en-ner-person.bin");
		}
		
		return personModel;
	}
	
	private TokenNameFinderModel getLocationModel()
	{
		if (locationModel == null) {
			locationModel = createNameFinderModel("/models/en-ner-location.bin");
		}
		
		return locationModel;
	}

	private TokenizerModel createTokenizerModel(String modelPath)
	{
		try {
			InputStream is = context.getResourceAsStream(modelPath);
			return new TokenizerModel(is);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private TokenNameFinderModel createNameFinderModel(String modelPath)
	{
		try {
			InputStream is = context.getResourceAsStream(modelPath);
			return new TokenNameFinderModel(is);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}