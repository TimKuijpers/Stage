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

/**
 *
 * @author Stan Wehkamp
 */
public class AminoAcidSequence {
    
    private final Strand strand;                    
    private final String sequence;            
    private final Integer codonTableKey;      
    
    /**
     * constructor for AminoAcidSequence
     * @param strand        enum strand duid aan of de sequentie van de positieve of negative strand komt 
     * @param sequence      aminozuur sequentie
     * @param codonTableKey key value van hasmap met codontabellen. duid aan welke codontabel is gebruikt voor translatie
     */
    public AminoAcidSequence(Strand strand, String sequence, Integer codonTableKey) {
        this.strand = strand;
        this.sequence = sequence;
        this.codonTableKey = codonTableKey;
    }
    /**
     * 
     * @return strand enum
     */
    public Strand getStrand() {
        return strand;
    }
    /**
     * een string die een serie van de eenletterige aminozuur afkortingen bevat
     * @return sequence String 
     */
    public String getSequence() {
        return sequence;
    }
    /**
     * returns the key/ID of the codontable that has been used to translate the sequence
     * @return codonTableKey Interger
     */
    public Integer getCodonTableKey() {
        return codonTableKey;
    }

}