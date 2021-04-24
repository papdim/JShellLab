package jshellLabExec.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;

public class AboutjShellLab extends javax.swing.JPanel {
    
        private JLabel jLabLabel;
        private JLabel stergLabel;
        private JLabel imgLabel;
        private JLabel GroovySciLabel;
        private JLabel GroovySciDescrLabel;
        private JLabel seperatorLabel1;
        private JLabel seperatorLabel2;
        private JLabel seperatorLabel3;
        private JLabel seperatorLabel4;
        
    public AboutjShellLab() {
        setLayout(new GridLayout(7,1));
        initComponents();
    }
    
    private void initComponents() {
        
                seperatorLabel1 = new JLabel("----------------------------------------------------------------------------------------------------");
                seperatorLabel1.setFont( new Font("Times New Roman", Font.BOLD, 24));
                seperatorLabel1.setForeground(Color.BLUE);
                
                seperatorLabel2 = new JLabel("----------------------------------------------------------------------------------------------------");
                seperatorLabel2.setFont( new Font("Times New Roman", Font.BOLD, 24));
                seperatorLabel2.setForeground(Color.GREEN);
                
                seperatorLabel3 = new JLabel("----------------------------------------------------------------------------------------------------");
                seperatorLabel3.setFont( new Font("Times New Roman", Font.BOLD, 24));
                seperatorLabel3.setForeground(Color.GREEN);
                
                
                jLabLabel = new JLabel("JShellLab:    Scientific Scripting for the Java Platform with JShell and Groovy");
                jLabLabel.setFont(new Font("Times New Roman", Font.BOLD, 26));
                jLabLabel.setForeground(Color.RED);
		
                        
                stergLabel = new JLabel("GitHub  research project, https://github.com/papdim");
                stergLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
                stergLabel.setForeground(Color.BLUE);
                        
                imgLabel = new JLabel("Stergios Papadimitriou, International Hellenic University, Dept. of Informatics  Kavala,  Greece ");
                imgLabel.setFont(new Font("Times New Roman", Font.BOLD, 12));
                imgLabel.setForeground(Color.BLUE);
                
                GroovySciLabel = new JLabel("JShell scripting: F7, JShell code completion: F3 ");
                GroovySciLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
                GroovySciLabel.setForeground(Color.DARK_GRAY);
		
                GroovySciDescrLabel = new JLabel("Groovy Scripting: F6, Groovy code completion: F12");
                GroovySciDescrLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
                GroovySciDescrLabel.setForeground(Color.DARK_GRAY);
                
                
                add(jLabLabel);
                add(seperatorLabel1);
                add(stergLabel);
                add(imgLabel);
                add(seperatorLabel3);
                add(GroovySciLabel);
                add(GroovySciDescrLabel);
                
                
    }

   

}
