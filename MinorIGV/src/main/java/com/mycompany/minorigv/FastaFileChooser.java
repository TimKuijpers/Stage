package com.mycompany.minorigv;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class FastaFileChooser {

    public String fastafile() {
        String fasta = "";
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File SelectedFile = jfc.getSelectedFile();
            fasta = (SelectedFile.getAbsolutePath());
        }

        return fasta;

    }

}
