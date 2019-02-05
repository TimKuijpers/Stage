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

public class MakeCompStrand {

   /**
    * Creëert een omgekeerde complementaire sequentie van nucleotides. De volgorde van de nucleotide wordt bepaald door de volgorde
    * waarin ze tegengekomen zouden worden als de leesrichting van het readingframe gevolgd word.
    * in: atg aaa ccg -> out:cgg ttt cat
    * @param sequence een string die een nucleotide sequentie voorsteld.
    * @return complement een string die een nucleotide sequentie voorsteld.
    */ 
    public static String getReverseComplement(String sequence) {


        char[] complement = new char[sequence.length()];
        int length = complement.length;
        for (int index = 0; index < sequence.length(); index++) {
            char c = sequence.charAt(index);
            length--;
            switch (c) {
                case 'T':
                    complement[length] = 'A';
                    break;
                case 'A':
                    complement[length] = 'T';
                    break;
                case 'C':
                    complement[length] = 'G';
                    break;
                case 'G':
                    complement[length] = 'C';
                    break;
                case 't':
                    complement[length] = 'a';
                    break;
                case 'a':
                    complement[length] = 't';
                    break;
                case 'c':
                    complement[length] = 'g';
                    break;
                case 'g':
                    complement[length] = 'c';
                    break;
                default:
                    complement[length] = c;
            }
        }
        return new String(complement);
    }
}
