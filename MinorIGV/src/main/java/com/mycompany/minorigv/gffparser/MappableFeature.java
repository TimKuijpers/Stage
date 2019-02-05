package com.mycompany.minorigv.gffparser;

import com.mycompany.minorigv.sequence.Strand;

public interface MappableFeature {

    int getStart();
    int getStop();
    Strand getStrand();

}
