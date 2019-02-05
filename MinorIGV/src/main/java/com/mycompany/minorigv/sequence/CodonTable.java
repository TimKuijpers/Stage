/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2007-2015 Broad Institute
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.mycompany.minorigv.sequence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author jrobinso, Stan Wehkamp
 * Bevat constructor voor codontable.
 * Bevat method "build" om arguments te genereren voor de constuctor.
 */
public class CodonTable {
	private Integer key;
	private String[] names;
	private Set<String> starts;
	private Map<String, String> codonMap;

	/**
	 * Constructor voor CodonTable
	 * @param key key functioneert als id voor de tabel and als key voor de hashmap die de CodonTable objecten gaat bevatten
	 * @param names array voor de namen van de codontabel
	 * @param starts set voor de alternative start aminozuren
	 * @param codonMap Map met een 3 letterige codons als key en een een letterige afkorting voor een aminozuur als value
	 */
	public CodonTable(Integer key, String[] names, Set<String> starts, Map<String, String> codonMap){
		this.key = key;                 
		this.names = names;              
		this.starts = starts;            
		this.codonMap = codonMap;        
	}
	
	 /**
	 * Genereerd de argumenten codonMap en Starts voor de CodonTable constructor die een nieuwe CodonTable terug geeft.
	 * @param key functioneerd als id voor de table en als key voor de hasmap die de CodonTable objecten gaat bevatten
	 * @param names array voor de namen van de codontabel
	 * @param base arrays met nucleotides om elke codon combinatie te maken
	 * @param aas string van aminozuren
	 * @param startString alternative start aminozuren
	 * @return een aanroep naar de codontable constructor (new CodonTable). als het object gemaakt word geeft het de nieuwe CodonTable terug
	 **/
	public static CodonTable build(Integer key, String[] names, String[] base, String aas, String startString) {

		String base1 = base[0];  //base word opgedeelt in 3 strings die elk nucleotides bevatten
		String base2 = base[1];  // de nucleotides van string 1, 2 en 3 zijn respectivelijk voor positie 1, 2 en 3 in het codon
		String base3 = base[2];

		Map<String, String> codonMap = new HashMap<String, String>(aas.length());
		Set<String> starts = new HashSet<String>(aas.length());   // set voor alternative start aminozuren

		for (int index = 0; index < aas.length(); index++) {
			String codon = base1.substring(index, index + 1) + base2.substring(index, index + 1) + base3.substring(index, index + 1);		//pakt uit de 3 strings dezelfde index om een codon te maken
			String aa = aas.substring(index, index +1);

			codonMap.put(codon, aa);  // codon en aminozuur worden aan elkaar gekoppeld door het in een hashmap te zetten

			if (startString.charAt(index) == 'M') {  // voegt alternatieve start aminozuur toe waneer er een 'M' in de array aas staat.
				starts.add(aa);
			}
		}

		return new CodonTable(key, names, starts, codonMap);
	}

	/**
	 * @return de Integer key
	 */
	public Integer getKey() {
		return key;
	}

	/**
	 * @return de String array names
	 */
	public String[] getNames() {
		return names;
	}

	/**
	 * @return de Set<String> starts
	 */
	public Set<String> getStarts() {
		return starts;
	}

	/**
	 * @return de Map<String, String> codonMap
	 */
	public Map<String, String> getCodonMap() {
		return codonMap;
	}

}

