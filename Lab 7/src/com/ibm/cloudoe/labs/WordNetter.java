package com.ibm.cloudoe.labs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rita.wordnet.RiWordnet;

public class WordNetter
{
	private static final RiWordnet wordnet = new RiWordnet(null);
	
	public Map<String, List<String>> synonyms(String word)
	{
		final String[] pos = wordnet.getPos(word);		
		final Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		for (int i = 0; i < pos.length; i++) {
			final String[] words = wordnet.getAllSynonyms(word, pos[i], 10);
			
			if (words != null) {
				map.put(pos[i], Arrays.asList(words));				
			}
		}
		
		return map;
	}
	
	public Map<String, List<String>> antonyms(String word)
	{
		final String[] pos = wordnet.getPos(word);
		final Map<String, List<String>> map = new HashMap<String, List<String>>();

		for (int i = 0; i < pos.length; i++) {
			final String[] words = wordnet.getAllAntonyms(word, pos[i]);
			
			if (words != null) {
				map.put(pos[i], Arrays.asList(words));				
			}
		}
		
		return map;
	}
}
