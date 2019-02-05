package com.mycompany.minorigv.gui;

import javax.swing.*;

public class ExceptionDialogs {


    public static void ErrorDialog(String message, String title){

        JOptionPane.showMessageDialog(null,
                message,
                title, JOptionPane.ERROR_MESSAGE);

    }

}
