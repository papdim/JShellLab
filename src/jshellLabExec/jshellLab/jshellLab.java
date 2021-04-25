
package jshellLabExec.jshellLab;

import jShellLabEdit.jshellLabEditor;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.URL;

import jshellLabGlobal.Interpreter.GlobalValues;
import jshellLabGlobals.JavaGlobals;




import java.net.URLDecoder;
import java.util.Objects;
import javax.script.ScriptContext;

import jdk.jshell.JShell;
import jdk.jshell.Snippet;
import jdk.jshell.SnippetEvent;
import jdk.jshell.execution.LocalExecutionControlProvider;

///import org.jetbrains.kotlin.cli.common.repl.KotlinJsr223JvmScriptEngineFactoryBase;
//import org.jetbrains.kotlin.cli.common.repl.ScriptArgsWithTypes;

/*
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngine;
import org.jetbrains.kotlin.script.jsr223.KotlinStandardJsr223ScriptTemplate;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MWE {

    private static class Factory extends KotlinJsr223JvmScriptEngineFactoryBase {

        @Override
        public ScriptEngine getScriptEngine() {
            return new KotlinJsr223JvmLocalScriptEngine(
                    this,
                    Arrays.stream(((URLClassLoader)ClassLoader.getSystemClassLoader()).getURLs()).map(URL::getFile).map(File::new).collect(Collectors.toList()),
                    KotlinStandardJsr223ScriptTemplate.class.getCanonicalName(),
                    (ctx, types) -> new ScriptArgsWithTypes(new Object[]{ctx.getBindings(ScriptContext.ENGINE_SCOPE)}, types == null ? new KClass[0] : types),
                    new KClass[] {JvmClassMappingKt.getKotlinClass(Bindings.class) }
            );
        }
    }

    public static void main(String... args) throws ScriptException {
        final ScriptEngine engine = new Factory().getScriptEngine();
        engine.getBindings(ScriptContext.ENGINE_SCOPE).put("abc", 1);
        System.out.println(engine.eval("1"));
        System.out.println(engine.eval("abc"));
    }
}
*/
/**
 * Simple GUI for jShellLab
 */
public class jshellLab
{
        static private String netbeansjLabArg;  // name of jshellLab executable .jar when starting from Netbeans
          
        static public  Dimension ScreenDim;
      
    
        public  static int xSizeMainFrame, ySizeMainFrame;
        public  int xSizeVarsFrame, ySizeVarsFrame;
        public static int xLocMainFrame, yLocMainFrame;

        
        
         private  URL watchURL;
   
        
        public GlobalValues  instanceGlobals;

        private int   horizDividerLoc; 
        private int  vertDividerLocConsole;

        // for history list handling
        private Object[] values;  // currently selected values
        private JList historyList;
        private String watchStr;

        	
   
  /*
    * This method will take a file name and try to "decode" any URL encoded characters.  For example
    * if the file name contains any spaces this method call will take the resulting %20 encoded values
    * and convert them to spaces.
    *
    */
    public static String decodeFileName(String fileName) {
        String decodedFile = fileName;

        try {
            decodedFile = URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Encounted an invalid encoding scheme when trying to use URLDecoder.decode() inside of the GroovyClassLoader.decodeFileName() method.  Returning the unencoded URL.");
            System.err.println("Please note that if you encounter this error and you have spaces in your directory you will run into issues.  Refer to GROOVY-1787 for description of this bug.");
        }

        return decodedFile;
    }

    
       
        /**Create the main graphical interface (menu, buttons, delays...).*/
        public jshellLab( String jshellLabClassPath)
        {
         
       
               String crossPlatformLookAndFeel="";
                ScreenDim = Toolkit.getDefaultToolkit().getScreenSize();
                //position the frame in the centre of the screen
                xSizeMainFrame = (int)((double)ScreenDim.width/1.2);  
                ySizeMainFrame = (int)((double)ScreenDim.height/1.4);
                xLocMainFrame = (ScreenDim.width-xSizeMainFrame) / 25;
                yLocMainFrame = (ScreenDim.height-ySizeMainFrame)/25 ; 
               
                GlobalValues.sizeX = xSizeMainFrame;
                GlobalValues.sizeY = ySizeMainFrame;
                horizDividerLoc =  (int)(0.3*xSizeMainFrame);
                vertDividerLocConsole = (int)(0.2*ySizeMainFrame);
        
                GlobalValues.figFrameSizeX = (int)((double)xSizeMainFrame/2.0);
                GlobalValues.figFrameSizeY = (int)((double)ySizeMainFrame/1.5);
          
                GlobalValues.consoleOutputWindow = new SysUtils.ConsoleWindow();
                
                  
         
              // the watchURL will serve to retrieve the name of the .jar file of the jShellLab system (i.e. "jShellLab.jar") 
       // the jShellLab's .jar file is used to load all the basic external classes and thus is very important.
       // When jShellLab is running within the Netbeans environment it is passed as the first program argument.          
            watchURL = getClass().getResource("resources/system-search.png");
            watchStr = watchURL.toString();
    
            watchStr = decodeFileName(watchStr);
            
            GlobalValues.rt = Runtime.getRuntime();
            GlobalValues.memAvailable = GlobalValues.rt.freeMemory();
       
            detectClassPaths.detectClassPaths();
            System.out.println("watchstr = "+watchStr);
           detectPaths(watchStr);
           
            
            if (GlobalValues.hostIsUnix==false)  {  // handle Windows file system naming
               int idxOfColon = watchStr.lastIndexOf(':'); 
               watchStr = watchStr.substring(idxOfColon-1, watchStr.length());
            }
            int sepIndex = watchStr.indexOf('!');
            if (sepIndex!=-1) {
                
                String fullJarPath = watchStr.substring(0, sepIndex);
                String jLabJarName = fullJarPath.substring(fullJarPath.lastIndexOf(File.separatorChar)+1, fullJarPath.length() );
                GlobalValues.jarFilePath =jLabJarName;
                GlobalValues.fullJarFilePath = fullJarPath;
            }
        
       //     System.out.println("GlobalValues.fullJarFilePath = "+GlobalValues.fullJarFilePath+"  GlobalValues.jarFilePath = "+GlobalValues.jarFilePath);
            
                  
        watchURL = getClass().getResource("resources/system-search.png");
      

         
        GlobalValues.initGlobals();
            
        GlobalValues.passPropertiesFromSettingsToWorkspace(GlobalValues.settings);
        
                
GlobalValues.myGEdit = new jshellLabEditor("Untitled", true);
           
    // GlobalAutoCompletion.initAutoCompletion();
               
         ClassLoader thisLoader = this.getClass().getClassLoader();
         
        if (GlobalValues.jshell == null) {
            
             GlobalValues.jshell = JShell.builder().executionEngine(new LocalExecutionControlProvider(), null).build();  // create a new JShell instance
         
              
       
         //  jdk.jshell.Completer.initCompleter();
     //     GlobalValues.srcAnalyzer = new jdk.jshell.SourceCodeAnalysisImpl(GlobalValues.jshell);
        //   GlobalValues.jshell.eval(GlobalValues.basicGlobalImports);
             //   any .jar file in the extraJarsForJShellClasspath folder is automatically appended to classpath 
          String  extraJarsForJShellClasspathFolder = GlobalValues.jshellLabLibPath.replace("lib", "extraJarsForJShellClasspath");
          System.out.println("appending to JShell classpath toolboxes of extraJarsForJShellClasspath folder:  "+extraJarsForJShellClasspathFolder);
          
          File [] toolboxesFolderFiles = (new java.io.File(extraJarsForJShellClasspathFolder)).listFiles();  // get the list of files at the default toolboxes folder
          if (toolboxesFolderFiles!=null) {  // extraJarsForJShellClasspath folder not empty
           int numFiles = toolboxesFolderFiles.length; 
           for (int f=0; f < numFiles;  f++) {
               String currentFileName = toolboxesFolderFiles[f].getAbsolutePath();
           
                  if (currentFileName.endsWith(".jar")) {
               
                   GlobalValues.jshell.addToClasspath(currentFileName);
                   
                   System.out.println("appending to JShell classpath from extraJarsForJShellClasspath folder toolbox: "+currentFileName);
              }  // endsWith("jar")
            }   // for all files of then extraJarsForJShellClasspath folder
          }   // extraJarsForJShellClasspath folder not empty
          
     
            
            String javaClassPath =  System.getProperty("java.class.path");
            System.out.println("javaclasspath = "+javaClassPath);
           GlobalValues.jshell.addToClasspath(javaClassPath);
      
          
            GlobalValues.jshell.addToClasspath(JavaGlobals.jarFilePath);
           
            GlobalValues.jshell.addToClasspath(JavaGlobals.javacppfile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.openblasfile);
            
           GlobalValues.jshell.addToClasspath(JavaGlobals.ApacheCommonsFile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.jsciFile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.mtjColtSGTFile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.ejmlFile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.jblasFile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.numalFile);
            
//           GlobalValues.jshell.addToClasspath(JavaGlobals.JASFile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.LAPACKFile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.ARPACKFile);
           
          for (String snippet: GlobalValues.jshellBasicGlobalImports) {
              GlobalValues.jshell.eval(snippet);
          }
         GlobalValues.jshell.onSnippetEvent(jshellLab::snippetEventHandler);
           
         
    
        }
        
        }
        
        
        public static void snippetEventHandler(SnippetEvent se) {
            String value = se.value();
            
            if (!Objects.isNull(value) && value.trim().length() > 0) 
                System.out.println(value);
            
            if (Snippet.Status.REJECTED.equals(se.status())) 
                System.out.println("Evaluation failed: "+se.snippet().toString()+"\nIgnoring execution of script");
          }        
                    
        
        
        
        public static void reinitJShell() {
       
            if (GlobalValues.jshell != null)
                GlobalValues.jshell.close();
              
      
             GlobalValues.jshell = JShell.builder().executionEngine(new LocalExecutionControlProvider(), null).build();  // create a new JShell instance
            
                  
         //  jdk.jshell.Completer.initCompleter();
     //     GlobalValues.srcAnalyzer = new jdk.jshell.SourceCodeAnalysisImpl(GlobalValues.jshell);
        //   GlobalValues.jshell.eval(GlobalValues.basicGlobalImports);
             //   any .jar file in the extraJarsForJShellClasspath folder is automatically appended to classpath 
          String  extraJarsForJShellClasspathFolder = GlobalValues.jshellLabLibPath.replace("lib", "extraJarsForJShellClasspath");
          System.out.println("appending to JShell classpath toolboxes of extraJarsForJShellClasspath folder:  "+extraJarsForJShellClasspathFolder);
          
          File [] toolboxesFolderFiles = (new java.io.File(extraJarsForJShellClasspathFolder)).listFiles();  // get the list of files at the default toolboxes folder
          if (toolboxesFolderFiles!=null) {  // extraJarsForJShellClasspath folder not empty
           int numFiles = toolboxesFolderFiles.length; 
           for (int f=0; f < numFiles;  f++) {
               String currentFileName = toolboxesFolderFiles[f].getAbsolutePath();
           
                  if (currentFileName.endsWith(".jar")) {
               
                   GlobalValues.jshell.addToClasspath(currentFileName);
                   System.out.println("appending to JShell classpath from extraJarsForJShellClasspath folder toolbox: "+currentFileName);
              }  // endsWith("jar")
            }   // for all files of then extraJarsForJShellClasspath folder
          }   // extraJarsForJShellClasspath folder not empty
          
          
            
           GlobalValues.jshell.addToClasspath(JavaGlobals.jarFilePath);
           
            GlobalValues.jshell.addToClasspath(JavaGlobals.javacppfile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.openblasfile);
            
              GlobalValues.jshell.addToClasspath(JavaGlobals.ApacheCommonsFile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.jsciFile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.mtjColtSGTFile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.ejmlFile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.jblasFile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.numalFile);
            
           GlobalValues.jshell.addToClasspath(JavaGlobals.JASFile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.LAPACKFile);
           GlobalValues.jshell.addToClasspath(JavaGlobals.ARPACKFile);
           
              
          for (String snippet: GlobalValues.jshellBasicGlobalImports) {
              GlobalValues.jshell.eval(snippet);
          }
         GlobalValues.jshell.onSnippetEvent(jshellLab::snippetEventHandler);
           
        }
       
        
        
           
                            
        
        
        
     public static void detectPaths(String watchStr) {
         if (File.pathSeparatorChar==';')  {  // handle Windows file system naming
               int idxOfColon = watchStr.lastIndexOf(':'); 
               watchStr = watchStr.substring(idxOfColon-1, watchStr.length());
            }
            int sepIndex = watchStr.indexOf('!');
                
            String fullJarPath = watchStr.substring(0, sepIndex);
     //       System.out.println("watch string = "+watchStr);
                
        //  test if jShellLab is installed in a directory containing special charactes, e..g. spaces, symbols, etc)
                //  and quit if so, displaying an appropriate message to the user
                boolean specialCharsInPath = false;
                int pathLen = fullJarPath.length();
                for (int k=0; k<pathLen; k++) {
                    char ch = fullJarPath.charAt(k);
                    if (ch != File.separatorChar && ch != '/'  && ch !=':' && ch!='.' && ch != '-' && ch != '_')
                      if ( Character.isLetterOrDigit(ch)==false ) {
                         specialCharsInPath = true;
                         break;
                  }
                }
                if (specialCharsInPath==true) {
                    JOptionPane.showMessageDialog(null, "Path where jShellLab is installed: "+fullJarPath+" , contains special characters. Please install jShellLab in a path name with no special chars and no spaces.", "Please install jShellLab in a simple path name", JOptionPane.INFORMATION_MESSAGE);
                   System.exit(1);
                }
                
                //System.out.println("fullJarPath = "+fullJarPath);
                String jLabJarName = fullJarPath.substring(fullJarPath.lastIndexOf(File.separatorChar)+1, fullJarPath.length() );
                if (jLabJarName.indexOf(File.separator)!=-1)
                    jLabJarName = jLabJarName.replaceAll(File.separator, "/");
                if (jLabJarName.contains("file:"))
                 jLabJarName = jLabJarName.replaceAll("file:", "");
                
                GlobalValues.jarFilePath = jLabJarName;
                GlobalValues.fullJarFilePath = fullJarPath;
                
                String jshellLabLibPath = GlobalValues.jarFilePath;
                String jshellLabHelpPath = GlobalValues.jarFilePath;
    
                // remove jar file name from the path name
                 int dotIndex = jshellLabLibPath.indexOf(".");
                 int lastPos = dotIndex;
                 while (jshellLabLibPath.charAt(lastPos)!='/' && jshellLabLibPath.charAt(lastPos)!='\\'  && lastPos>0)
                             lastPos--;
                 jshellLabLibPath = jshellLabLibPath.substring(0, lastPos);
                     
                // remove jar file name from the path name
                 dotIndex = jshellLabHelpPath.indexOf(".");
                 lastPos = dotIndex;
                 while (jshellLabHelpPath.charAt(lastPos)!='/' && jshellLabHelpPath.charAt(lastPos)!='\\'  && lastPos>0)
                             lastPos--;
                  jshellLabHelpPath =jshellLabHelpPath.substring(0, lastPos);
                 
                
                 if (jshellLabLibPath.length()>0) {
                    jshellLabLibPath = jshellLabLibPath+File.separator+"lib"+File.separator;
                    jshellLabHelpPath = jshellLabHelpPath+File.separator+"help"+File.separator;
                 }
                 else  {
                  jshellLabLibPath = "lib"+File.separator;
                  jshellLabHelpPath = "help"+File.separator;
                 }
                     
                 GlobalValues.jshellLabLibPath = jshellLabLibPath;
                 GlobalValues.jshellLabHelpPath = jshellLabHelpPath;
                 
                 System.out.println("GlobalValuesjshellLabLibPath = "+GlobalValues.jshellLabLibPath);
                 System.out.println("GlobalValues.jshellLabHelpPath = "+GlobalValues.jshellLabHelpPath);
            
     }
     

      
      public static void main(String[] args)
        {
          int argc;
          if (args!=null)
                argc = args.length;
          else argc = 0;

          if (argc > 0)
              netbeansjLabArg = args[0];
      
            String vers = System.getProperty("java.version");
        if (vers.compareTo("1.5") < 0) {
            System.out.println("!!!jShellLab: Swing must be run with a 1.5 or higher version VM!!!");
            System.exit(1);
        }
       
            String currentWorkingDirectory = System.getenv("PWD");
            if (currentWorkingDirectory==null)
                currentWorkingDirectory = "c:\\"; 
           String userDir = System.getProperty("user.dir");
             
             if (argc > 0)  {
                 GlobalValues.jarFilePath = args[0];
                 GlobalValues.fullJarFilePath = args[0];
             }
            if (argc>=2) {
                GlobalValues.jshellLabClassPath = args[1];
            }
            
          
            if (File.separatorChar=='/')  { // detect OS type
        	GlobalValues.hostIsUnix = true;
             }
                        else {   // Windows host
                GlobalValues.hostIsUnix = false;
                if (currentWorkingDirectory == null)   // e.g. for Windows the current working directory is undefined
                currentWorkingDirectory = "C:\\";
            }
            if (GlobalValues.workingDir==null)
                         GlobalValues.workingDir = currentWorkingDirectory;
     
            
            final jshellLab myGui = new jshellLab(GlobalValues.jshellLabClassPath);
             
           
  


              }
   
        

                
   }
   
    
