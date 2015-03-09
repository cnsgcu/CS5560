package com.ibm.cloudoe.labs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NGrammer
{
	public List<String> ngram(List<String> input, int n, String separator)
	{
		if (input.size() <= n) {
			String gram = "";
			
			for (int i = 0; i < input.size(); i++) {
				gram += input.get(i) + separator;
			}
			
			return Arrays.asList(gram.substring(0, gram.length() - separator.length()));
		}
		
		List<String> outGrams = new ArrayList<String>(input.size() - n + 1);

		for (int i = 0; i + n <= input.size(); i++) {
			String gram = "";
			
			for (int j = i; j < i + n; j++) {
				gram += input.get(j) + separator;
			}
			
			gram = gram.substring(0, gram.length() - separator.length());
			outGrams.add(gram);
		}
		
		return outGrams;
	}
}
