package com.mycompany.minorigv.fastqparser;

/**
 * @author Stan Wehkamp
 * Exception voor foutief bestandsformat.
 */
public class InvalidFileTypeException extends Exception{

    /**
     * Constructor
     */
    public InvalidFileTypeException(){

    }

    /**
     * Constructor
     * @param msg Berichtje om bij te sluiten.
     */
    public InvalidFileTypeException(String msg){
        super(msg);
    }
}
