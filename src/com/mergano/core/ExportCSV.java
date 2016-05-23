/*
 * Copyright 2016 vchuk.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mergano.core;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JFileChooser;

public class ExportCSV {

    public void exportToCSV(JTable table) {
        JOptionPane.showMessageDialog(null, "Please choose directory and specify your file name (in .csv form)", "Information", JOptionPane.WARNING_MESSAGE);
        String pathToSave = "";
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION){
            pathToSave = fc.getCurrentDirectory() + "\\" + fc.getSelectedFile().getName();
        }
        else return;

        if(!acceptFileName(pathToSave)){
            return;
        }
        
        Writer writer = null;
        DefaultTableModel dtm = (DefaultTableModel) table.getModel();
        int nRow = dtm.getRowCount();
        int nCol = dtm.getColumnCount();

        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathToSave), "utf-8"));

            //write the header information
            StringBuilder bufferHeader = new StringBuilder();
            for (int j = 0; j < nCol; j++) {
                bufferHeader.append(dtm.getColumnName(j));
                if (j != nCol) {
                    bufferHeader.append(", ");
                }
            }
            writer.write(bufferHeader.toString() + "\r\n");

            //write row information
            for (int i = 0; i < nRow; i++) {
                StringBuilder buffer = new StringBuilder();
                for (int j = 0; j < nCol; j++) {
                    buffer.append(dtm.getValueAt(i, j));
                    if (j != nCol) {
                        buffer.append(", ");
                    }
                }
                writer.write(buffer.toString() + "\r\n");
            }
            JOptionPane.showMessageDialog(null, "Data table exported to " + fc.getCurrentDirectory() + " successfully.", "Export Successfully", INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error 301 : Can't export data because " + ex.getMessage(), "Export Failed", ERROR_MESSAGE);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(ExportCSV.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    private boolean acceptFileName(String str){
        if(str.toLowerCase().endsWith(".csv")){
            JOptionPane.showMessageDialog(null, "File Accepted.", "Success.", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
        else{
            JOptionPane.showMessageDialog(null, "Please Enter .csv file.", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
