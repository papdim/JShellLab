
package jShellLabEdit;

import jShellLabEdit.jshellLabEditor;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;




import jshellLabGlobal.Interpreter.GlobalValues;
import jshellLabExec.jshellLab.StreamGobbler;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Properties;
import java.util.Vector;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


   class autoCompletionHelpAction extends AbstractAction {
       autoCompletionHelpAction() {
           super("AutoCompletion with regular expression matching-- F3  (JScript-JShellLabSci), F11 (Java)");
       }    
     public void actionPerformed(ActionEvent e)  {
         
                 JFrame  regExpFrame = new JFrame("Help on Regular Expression based autocompletion");
                 regExpFrame.setSize(400, 300);
                 regExpFrame.setLayout(new BorderLayout());
                 
                JTextArea  regExpArea = new JTextArea();
                regExpArea.setFont(GlobalValues.defaultTextFont);
                regExpArea.setText("Examples of regular expression based matching: \n"+
 "1.   p.*             // All commands that start with p \n"+
 "2.   [ap].*         // All commands that start with a or p \n"+
 "3.   [^a].*         // All commands that do not start with a \n"+
 "4.   .*cos.*        // All commands that include \"cos\" somewhere in their description \n"+
 "5.   [b-e].*        // All commands that start with a letter at the range b-e (i.e. b,c,d,e) \n+" +
 "6.   [B-De-h].*     // All commands that start with B-D (i.e. B, C, D) or e-h (i.e. e,f,g,h) \n"+
 "7.   [^A-Z^a-e].*  // All commands that do not start with A-Z and also not with a-e\n"+
 "8.   [^A-Z&&[^a]].*   // not with A-Z and not with a \n"+
 "9.  ^p.*           // p at the start \n"+
 "10.  [^a-c].*     // first character not in  [a-c] \n"+
 "11.   *.component  // last word is 'component'\n"
                        );
                
                regExpFrame.add(regExpArea);
                regExpFrame.setVisible(true);
               }
    }
   
   


   class jShellLabSciExamplesJTreeAction extends AbstractAction {
       jShellLabSciExamplesJTreeAction() {
           super("JShellLabSci Examples with JTree displaying");
       }

    public void actionPerformed(ActionEvent e) {
        
   jshellLabExec.gui.watchExamples weObj = new jshellLabExec.gui.watchExamples();
   weObj.scanMainJarFile(".jsci");
   weObj.displayExamples("JShellLabSci Examples");
   }
   }



   class jEquationsExamplesJTreeAction extends AbstractAction {
       jEquationsExamplesJTreeAction() {
           super("process equations  Examples with JTree displaying");
       }

    public void actionPerformed(ActionEvent e) {
        
   jshellLabExec.gui.watchExamples weObj = new jshellLabExec.gui.watchExamples();
   weObj.scanMainJarFile(".m");
   weObj.displayExamples("Equation  Examples");
   }
   }


   class jShellLabSciPlotExamplesJTreeAction extends AbstractAction {
       jShellLabSciPlotExamplesJTreeAction() {
           super("JShellLabSci Plot Examples with JTree displaying");
       }

    public void actionPerformed(ActionEvent e) {
        
   jshellLabExec.gui.watchExamples weObj = new jshellLabExec.gui.watchExamples();
   weObj.scanMainJarFile(".plots-jsci");
   weObj.displayExamples("JShellLabSci Plot Examples");
   }
   }

   
   
   class jEquationExamplesAction extends AbstractAction {
       jEquationExamplesAction() {
           super("JEquation Examples");
       }

    public void actionPerformed(ActionEvent e) {
    
   jshellLabExec.gui.watchExamples weObj = new jshellLabExec.gui.watchExamples();
   weObj.scanMainJarFile(".m");
   weObj.displayExamples("Equation  Examples");
   }
   }
   
      

   
   
   
    

   class editAction extends AbstractAction {
       public editAction()  { super("jShellLab Editor");}
       public void actionPerformed(ActionEvent e) {
                           SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
                   GlobalValues.myGEdit = new  jshellLabEditor(GlobalValues.workingDir+File.separatorChar+"Untitled");
           }
                     });
                    }
       }
   
   
   
   
      class jeditAction extends AbstractAction {
       public jeditAction()  { super("jEdit Editor ");}
       public void actionPerformed(ActionEvent e) {
               String [] command  = new String[6];
               command[0] =  "java";
// classpath to the jedit seems that is not used, but in any case it  is not harmful
               String  jeditClassPath =  "-classpath";
               jeditClassPath +=  " "+GlobalValues.jarFilePath+File.pathSeparator;
                       
               command[1] = "-cp";
               command[2] = jeditClassPath;
               
               command[3] = "-jar";
               String jeditPath = GlobalValues.jshellLabLibPath+ "4.3.2"+File.separator+"jedit.jar";
               command[4] =   jeditPath;
               
           
               
               String fileName = "Untitled.groovy";
               command[5] = fileName;
               
            String jEditcommandString = command[0]+"  "+command[1]+"  "+command[2]+" "+command[3];
            System.out.println("jEditCommandString = "+jEditcommandString); 
            try {
                Runtime rt = Runtime.getRuntime();
                Process javaProcess = rt.exec(command);
                // an error message?
                StreamGobbler errorGobbler = new StreamGobbler(javaProcess.getErrorStream(), "ERROR");

                // any output?
                StreamGobbler outputGobbler = new StreamGobbler(javaProcess.getInputStream(), "OUTPUT");

                // kick them off
                errorGobbler.start();
                outputGobbler.start();

                } catch (IOException exio) {
                    System.out.println("IOException trying to executing "+command);
                    exio.printStackTrace();

                }
               
           }
                     
                    }
                
   

   
   class jShellLabServerAction extends AbstractAction {
       jShellLabServerAction() { super("Control IP of jShellLab server"); }
       public void actionPerformed(ActionEvent e) {
           SwingUtilities.invokeLater(new Runnable() {
               public void run() {
                   GlobalValues.serverIP =
                           JOptionPane.showInputDialog(null,  "Specify IP of jShellLab server", GlobalValues.serverIP);
                   GlobalValues.settings.setProperty("serverIPProp", GlobalValues.serverIP);
                           
                  }
               });
           };
   }
           




class controlPrecisionAction extends AbstractAction {

    public controlPrecisionAction() {
      super("Controls precision of displayed numbers and truncation of large matrices");
    }

    public void actionPerformed(ActionEvent e)  {
        JFrame precisionConfigFrame = new JFrame("Display Precision and Matrix display truncation Configuration");
        precisionConfigFrame.setLayout(new GridLayout(6,2));
        
        precisionConfigFrame.add(new JLabel("Decimal digits for matrices"));
        final JTextField  precisionMatEditField = new JTextField(java.lang.String.valueOf(jShellLabSci.PrintFormatParams.getMatDigitsPrecision()));
        precisionMatEditField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              jShellLabSci.PrintFormatParams.setMatDigitsPrecision(java.lang.Integer.parseInt(precisionMatEditField.getText()));
            }
        });
        precisionConfigFrame.add(precisionMatEditField);
        
        precisionConfigFrame.add(new JLabel("Decimal digits for vectors"));
        final JTextField  precisionVecEditField = new JTextField(java.lang.String.valueOf(jShellLabSci.PrintFormatParams.getVecDigitsPrecision()));
        precisionVecEditField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jShellLabSci.PrintFormatParams.setVecDigitsPrecision(java.lang.Integer.parseInt(precisionVecEditField.getText()));
                
            }
        });
        precisionConfigFrame.add(precisionVecEditField);
        
        
        precisionConfigFrame.add(new JLabel("Number of rows for matrices"));
        final JTextField  rowsMatEditField = new JTextField(java.lang.String.valueOf(jShellLabSci.PrintFormatParams.getMatMxRowsToDisplay()));
        rowsMatEditField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              jShellLabSci.PrintFormatParams.setMatMxRowsToDisplay(java.lang.Integer.parseInt(rowsMatEditField.getText()));
            }
        });
        precisionConfigFrame.add(rowsMatEditField);
        
        precisionConfigFrame.add(new JLabel("Number of columns  for matrices"));
        final JTextField colsMatEditField = new JTextField(java.lang.String.valueOf(jShellLabSci.PrintFormatParams.getMatMxColsToDisplay()));
        colsMatEditField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jShellLabSci.PrintFormatParams.setMatMxColsToDisplay(java.lang.Integer.parseInt(colsMatEditField.getText()));
                
            }
        });
        precisionConfigFrame.add(colsMatEditField);
      
        
        precisionConfigFrame.add(new JLabel("Verbose"));
        final JCheckBox verboseCheckBox = new JCheckBox("Verbose Flag ");
        verboseCheckBox.setToolTipText("Verbose off stops the output of some results");
        verboseCheckBox.setSelected(jShellLabSci.PrintFormatParams.getVerbose());
        verboseCheckBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
              jShellLabSci.PrintFormatParams.setVerbose(verboseCheckBox.isSelected());
            }
        });
        precisionConfigFrame.add(verboseCheckBox);
                
        JButton acceptButton = new JButton("Accept all text field values");
        acceptButton.setToolTipText("Press to read all the contents of text fields (each individual text field is readed by pressing ENTER");
        acceptButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
        jShellLabSci.PrintFormatParams.setMatDigitsPrecision(java.lang.Integer.parseInt(precisionMatEditField.getText()));
        jShellLabSci.PrintFormatParams.setVecDigitsPrecision(java.lang.Integer.parseInt(precisionVecEditField.getText()));
        jShellLabSci.PrintFormatParams.setMatMxRowsToDisplay(java.lang.Integer.parseInt(rowsMatEditField.getText()));
        jShellLabSci.PrintFormatParams.setMatMxColsToDisplay(java.lang.Integer.parseInt(colsMatEditField.getText()));
            }
        });
        precisionConfigFrame.add(acceptButton);
        
        JButton displayButton = new JButton("Display the current parameters");
        displayButton.setToolTipText("Displays the current parameter setting");
        displayButton.addActionListener(new ActionListener() {
        
            @Override
            public void actionPerformed(ActionEvent e) {
    System.out.println("Matrix Digits Precision = "+jShellLabSci.PrintFormatParams.getMatDigitsPrecision());
    System.out.println("Vector Digits Precision = "+jShellLabSci.PrintFormatParams.getVecDigitsPrecision());
    System.out.println("Rows to display = "+jShellLabSci.PrintFormatParams.getMatMxRowsToDisplay());
    System.out.println("Columns to display = "+jShellLabSci.PrintFormatParams.getMatMxColsToDisplay());
            }
          });
       precisionConfigFrame.add(displayButton);         
        
        precisionConfigFrame.pack();
        precisionConfigFrame.setLocation(GlobalValues.globalEditorPane.getX()+200, GlobalValues.globalEditorPane.getY());
        precisionConfigFrame.setVisible(true);
    
    }
    
    
    
}


