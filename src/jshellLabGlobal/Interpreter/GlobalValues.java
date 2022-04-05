
package jshellLabGlobal.Interpreter;

import jshellLabExec.jshellLab.jshellLab;
import jshellLabExec.jshellLab.jshellLabUtils;
import jShellLabEdit.jshellLabEditor;

import com.googlecode.funclate.internal.lazyparsec.functors.Binary;
import edu.emory.mathcs.utils.ConcurrencyUtils;
import jshellLabExec.ClassLoaders.ExtensionClassLoader;
import jshellLabExec.gui.*;
import jShellLabSci.math.array.Matrix;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import javax.swing.*;
import jdk.jshell.JShell;
import org.ejml.equation.Equation;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
//import org.jetbrains.kotlin.cli.common.repl.IReplStageState;
//import org.jetbrains.kotlin.cli.common.repl.KotlinJsr223JvmScriptEngineBase;
//import org.jetbrains.kotlin.cli.common.repl.ReplCompilerWithoutCheck;
//import org.jetbrains.kotlin.cli.common.repl.ReplFullEvaluator;


//import org.jetbrains.kotlin.cli.common.repl.KotlinJsr223JvmScriptEngineFactoryBase;
import javax.script.*;
public   class GlobalValues
{  
    
   static public   String  dateOfBuild = "5-April-2022";       // tracks the date of build    
  
    static public ScriptEngineManager manager; 
    static public ScriptEngine  kotlinEngine;
    
    static public Bindings   globalBindings = new SimpleBindings();
    static public Bindings  engineBindings = new SimpleBindings();
    static public java.awt.Cursor waitCursor =new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR);
    static public java.awt.Cursor defaultCursor = new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR);
    
    static public Equation  eq = new Equation();   // EJML equation object
    
    static public JShell jshell=null;   // evaluate expressions with JShell
    static public int mxLenToDisplay = 300; // controls how much chars to output, i.e. for a large array
    static public TreeMap<String,Object> jshellBindingValues=new TreeMap<String,Object>();
    
    static public TreeMap<String,Object> jshellBindingTypes=new TreeMap<String,Object>();
       // the executor service is used to exploit Java multithreading for asynchronous computation operations and for
    // other tasks, as e.g. matrix multiplication 
    static public ExecutorService execService =   Executors.newFixedThreadPool(ConcurrencyUtils.getNumberOfProcessors());
    
    static public boolean AutoCompletionInitialized = false;
    static public ServerSocket jshellLabServerSocket;    // socket for JShellLab's server
    
    // variables for multithreaded operations
    static public   int multithreadingOpLimit  = 1000;  // a matrix with more elements than this is conidered large, thus use multithreading
     // a multiplication that involves more than that number of elements, is large thus use multithreading
    // if this parameter is not specified by a property, it is computed dynamically at run time
    static public   int mulMultithreadingLimit = -1; 
    static public String  mulMultithreadingLimitProp="undefined";
      // the pendingThreads class allows to cancel task started with the Shift-F6 keystroke
     // however, cancelling Java threads that are not designed for interruption, is an involved and problematic issue
    static public jshellLabGlobal.Interpreter.PendingThreads pendingThreads = new jshellLabGlobal.Interpreter.PendingThreads();
    
    static public   String  jdkTarget = "1.12";
 
  // variables for implementing code completion using Java reflection
   static public JFrame completionFrame;  // the open completion Frame, it is used in order to dispose it with ESC
   static public String  textForCompletion;  // replace this text with the user selection at completion
   static public int selectionStart;
   static public int selectionEnd;
   static public boolean methodNameSpecified = false;   // method name specified at completion
   static public boolean performPackageCompletion = false;
      
   // at code completion (with F12) static members are denoted in bold font
   static public Font staticsFont = new Font("Arial", Font.BOLD, 11);
   // at code completion (with F12) instance members are denoted in plain font text
   static public Font instancesFont = new Font("Arial", Font.PLAIN, 11);
   static public Font fontForCompletionListItem = instancesFont; // font to use for rendering the current item
   static public String staticsMarker = "#####";
   static public String nonStaticsMarker = "%%%%%";
   static public boolean [] isStaticMarks;
   static public int maxItemsToDisplayAtCompletions = 15;
  
   static public RTextScrollPane   bufferedImportsScrollPane;
    
   static public boolean  rememberSizesFlag = true;   // remembers the configured sizes for the frames
   static public boolean useAlwaysDefaultImports = true;

  
   static public Desktop  desktop;
   static public boolean useSystemBrowserForHelp = true;

   static public String  userPathsFileName = "JShellLabUserPaths.txt";  
   static public  JScrollPane  outputPane;     // the System Console output scroll pane

   static public RSyntaxTextArea   globalEditorPane;   // the rsyntaxarea component based jShellLab editor

   static public String  smallNameFullPackageSeparator = "-->";  
          
                 
   static  public String paneFontName = "Times New Roman";
   static  public int paneFontSize = 16;
   static  public boolean paneFontSpecified = true;
        
        // for main menus
   static  public String uiFontName = "Times New Roman";
   static  public String  uiFontSize = "14";
   static public Font uifont = new Font(uiFontName, Font.PLAIN, Integer.parseInt(uiFontSize));
    
        // for popup menus
   static  public String puiFontName = "Times New Roman";
   static  public String  puiFontSize = "14";
   static public Font puifont = new Font(puiFontName, Font.PLAIN, Integer.parseInt(puiFontSize));
    
        // for rest gui drawing
    static  public String guiFontName = "Times New Roman";
    static  public String  guiFontSize = "14";
    static public Font guifont = new Font(guiFontName, Font.PLAIN, Integer.parseInt(guiFontSize));
    
        // for buttons drawing
        static  public String buiFontName = "Times New Roman";
        static  public String  buiFontSize = "14";
        static public Font buifont = new Font(guiFontName, Font.PLAIN, Integer.parseInt(buiFontSize));
    
        // for Help html
        static  public String htmlFontName = "Times New Roman";
        static  public String htmlFontSize = "16";
        static public Font htmlfont = new Font(htmlFontName, Font.PLAIN, Integer.parseInt(htmlFontSize));
    
        static  public String outConsoleFontName = "Times New Roman";
        static  public String  outConsoleFontSize = "14";

        static  public String  gLabConsoleFontName = "Times New Roman";
        static  public String  gLabConsoleFontSize = "14";
        
        
   static public boolean pathUpdateOnSaves = false;
     // Defaults for Main Help
    
    static public  String toolboxStartUpcode = "";
    static public String selectedExplorerPath; 

    static public double log2Conv = Math.log(2.0);   
    
    static public String currentExpression;   // holds the text of the current expression in execution
    
    public static SysUtils.ConsoleWindow  consoleOutputWindow;
     
    public static Dimension ScreenDim;

    
    static public boolean truncateOutput  = false;   // displays all the output results from the jshellLab interpreter without truncating
    static public boolean globalVerboseOff = false;
    static public boolean displayAtOutputWindow = false;   // controls displaying at output window
    static public int maxNumsOutputToString = 1000;  // restricts output of toString()
        
        static public   DecimalFormat fmtString = new  DecimalFormat("0.0000");
        static public   DecimalFormat fmtMatrix = new DecimalFormat("0.000"); // format Matrix results
        static public   int  doubleFormatLen = 4; // how many digits to display for doubles, Matrix
        // these colors of the console input window  indicate the corresponding jshellLab operating modes
        static public  Color  ColorJShellLabSci = new Color(255, 255, 255);
        static public Map desktophints;
    
        static public boolean effectsEnabled = false;
        static public boolean mainToolbarVisible = true;
        static public float alphaComposite = 0.5f;

         /**Constant with the application title.*/
        static public String TITLE=buildTitle();

        static public long  timeForTic; // save the current time in milliseconds to implement tic-toc functionality
        
        static public  java.util.LinkedList<String>  JShellLabShellPathsList = null;  // the current paths list with which the JShell's classloader is inited
        static public boolean retrieveAlsoMethods = false; // retrieve also declared methods from toolboxes
        static public ExtensionClassLoader extensionClassLoader = null;
        static public Vector bindingVarValues = new Vector();  // bindings for scripting interfacing 
        static public Vector bindingVarNames = new Vector();
        static public int bindingCnt = 0; // count of binded variables
            
          static public String [] jshellBasicGlobalImports={
                   "import jShellLabSci.math.array.Vec; ",  // Vector class
                   "import static jShellLabSci.math.array.Vec.*; ",
        
                    "import jShellLabSci.math.array.Matrix; ",  // Matrix class
                    "import static jShellLabSci.math.array.Matrix.*; ",  
                    "import jShellLabSci.math.array.Mat1D; ",  // Matrix class
                    "import static jShellLabSci.math.array.Mat1D.*; ",  // Matrix class
                    
                    "import  static  jShellLabSci.math.array.DoubleArray.*;  ",
                
                    "import jShellLabSci.math.array.CCMatrix; ",  // Matrix class
                    "import static jShellLabSci.math.array.CCMatrix.*; ",  
                    "import jShellLabSci.math.array.Sparse; ",  // Sparse Matrix class
                    "import static jShellLabSci.math.array.Sparse.*; ",  
                    "import jShellLabSci.math.array.JILapack; ",  // JLapack Matrix class
                     "import static jShellLabSci.math.array.JILapack.*; ",  
                     "import Jama.*;",
                     "import static jShellLabSci.math.plot.plot.*;  ",     // plotting routines 
                     "import static jShellLabSciCommands.BasicCommands.*;   ",
    
                      "import static jShellLabSci.math.array.MatrixConvs.*;   ", 
                
                       "import org.jblas.*; ",  
        
                // JFreeChart imports
                  "import JFreePlot.*;   ",
                  "import static JFreePlot.jFigure.*;   ",
                  "import static JFreePlot.jPlot.*;  ",
                
                   "import java.awt.*;   ",
                   "import javax.swing.*;   ",   // Java standard UI and graphics support
                   "import java.util.function.Function; ",
                   "import java.util.function.*;",
                   "import java.util.*;",
                   "import java.util.concurrent.*;",
                   "import static java.util.Arrays.*;",
                   "import java.util.stream.*;",
                   "import java.util.stream.Collectors;",
                   "import static java.util.stream.Collectors.*;",
                           
                    "import java.awt.event.*;   ", 
                    "import java.text.DecimalFormat;   ",
             
                     "import jShellLabSci.FFT.ApacheFFT;   ",
                     "import static jShellLabSci.FFT.ApacheFFT.*;  ",
                     "import static jShellLabSci.FFT.FFTCommon.*;  ", 
                     "import static java.util.List.*;",
                     "import java.util.Set;",
                     "import static java.util.Set.*;"
                // EJML imports
               /*      "import org.ejml.*;",
                     "import org.ejml.concurrency.*;",
                     "import pabeles.concurrency.*;",
                     "import org.ejml.data.*;",
                     "import org.ejml.dense.block.*;",
                     "import org.ejml.dense.block.decomposition.bidiagonal.*;",
                     "import org.ejml.dense.block.decomposition.chol.*;",
                     "import org.ejml.dense.block.decomposition.hessenberg.*;",
                     "import org.ejml.dense.block.decomposition.qr.*;",
                     "import org.ejml.dense.block.linsol.chol.*;",
                     "import org.ejml.dense.block.linsol.qr.*;",
                     "import org.ejml.dense.blockd3.*;",
                     "import org.ejml.dense.densed2.mult.*;",
                     "import org.ejml.dense.fixed.*;",
                     "import org.ejml.dense.row.*;",
                     "import org.ejml.dense.row.decompose.*;",
                     "import org.ejml.dense.row.decompose.chol.*;",
                     "import org.ejml.dense.row.decompose.hessenberg.*;",
                     "import org.ejml.dense.row.decompose.lu.*;",
                     "import org.ejml.dense.row.decomposition.*;",
                     "import org.ejml.dense.row.decomposition.bidiagonal.*;",
                     "import org.ejml.dense.row.decomposition.chol.*;",
                     "import org.ejml.dense.row.decomposition.eig.*;",
                     "import org.ejml.dense.row.decomposition.eig.symm.*;",
                     "import org.ejml.dense.row.decomposition.eig.watched.*;",
                     "import org.ejml.dense.row.decomposition.hessenberg.*;",
                     "import org.ejml.dense.row.decomposition.lu.*;",
                     "import org.ejml.dense.row.decomposition.qr.*;",
                     "import org.ejml.dense.row.decomposition.svd.*;",
                     "import org.ejml.dense.row.decomposition.svd.implicitqr.*;",
                     "import org.ejml.dense.row.factory.*;",
                     "import org.ejml.dense.row.linsol.*;",
                     "import org.ejml.dense.row.linsol.chol.*;",
                     "import org.ejml.dense.row.linsol.lu.*;",
                     "import org.ejml.dense.row.linsol.qr.*;",
                     "import org.ejml.dense.row.linsol.svd.*;",
                     "import org.ejml.dense.row.misc.*;",
                     "import org.ejml.dense.row.mult.*;",
                     "import org.ejml.equation.*;",
                     "import org.ejml.generic.*;",
                     "import org.ejml.interfaces.*;",
                     "import org.ejml.interfaces.decomposition.*;",
                     "import org.ejml.interfaces.linsol.*;",
                     "import org.ejml.ops.*;",
                     "import org.ejml.simple.*;",
                     "import org.ejml.simple.ops.*;",
                     "import org.ejml.sparse.*;",
                     "import org.ejml.sparse.csc.*;",
                     "import org.ejml.sparse.csc.decomposition.chol.*;",
                     "import org.ejml.sparse.csc.decomposition.lu.*;",
                     "import org.ejml.sparse.csc.decomposition.qr.*;",
                     "import org.ejml.sparse.csc.factory.*;",
                     "import org.ejml.sparse.csc.linsol.chol.*;",
                     "import org.ejml.sparse.csc.linsol.lu.*;",
                     "import org.ejml.sparse.csc.linsol.qr.*;",
                     "import org.ejml.sparse.csc.misc.*;",
                     "import org.ejml.sparse.csc.mult.*;",
                     "import org.ejml.sparse.triplet.*;",
                    "import static org.ejml.ops.MatrixIO.*;"
  */
};
        
           // Console Configuration
        static String defaultFontName = "Times New Roman";
        static String  defaultFontSize = "16";
        static public Font   defaultTextFont; 
        
        static public String detailHelpStringSelected="";
     
        static public boolean hostIsUnix = File.pathSeparatorChar == ':'?  true  :  false;   // Unix like system or Windows?
        static public boolean hostIsWin = !hostIsUnix;
        static public boolean hostIsWin64 = hostIsWin && System.getProperty("os.arch").toLowerCase().contains("amd64");
        static public boolean hostIsLinux = System.getProperty("os.name").toLowerCase().contains("linux");
        static public boolean hostIsLinux64 = hostIsLinux && System.getProperty("os.arch").toLowerCase().contains("amd64");
        static public boolean hostIsFreeBSD = System.getProperty("os.name").toLowerCase().contains("freebsd");
        static public boolean hostIsSolaris = System.getProperty("os.name").toLowerCase().contains("sunos");
        static public boolean hostIsMac =    System.getProperty("os.name", "").toLowerCase().contains("mac");
      
        
        static public boolean hostNotWinNotLinux = ( (hostIsUnix==true)  &&  (hostIsLinux==false) ); // Unix-like OS, not Linux  e.g. FreeBSD, MacOS, Solaris etc.
        
        static public boolean startingFromNetbeans = false;
        static public String jarFilePath;  // the path that contains the main jar file
        static public String fullJarFilePath;
        static public String jshellLabLibPath="";
        static public String jshellLabHelpPath = "";
        
        
        
        
// jLabClassPath is the directory that serves as the "root" for external class retrieval. 
 // This directory and all its subdirectories are searched for both .j script files and .class files        
        static public String jshellLabClassPath="";
        
  // GroovySciClassPath is the directory that serves as the "root" for Groovy Class Loader class retrieval. 
 // This directory and all its subdirectories are searched for both .groovy script files and .class files        
        static public String JShellLabSciClassPath; 
        static public Vector jshellLabSciClassPathComponents=new Vector();
        
        static public Vector  favouriteElements = new Vector();


        
        static public String jshellLabPropertiesFile;  // the file for obtaining configuration properties
        static public String workingDir;  // the current working directory
        static public String homeDir;  // the user's home directory
        
        static public Vector jartoolboxes =new Vector();  // load Java classes for Groovy mode from these toolboxes 
        static public HashMap<String, Boolean>  jartoolboxesLoadedFlag;  // associates each jar toolbox name with a flag that indicates whether it was loaded or not
        static public int sizeX = 600;  // the jshellLab's main console window jFrame size
        static public int sizeY = 400;  
        static public int locX = 100;   // location of jshellLab's main window
        static public int locY = 100; 
        
        static public int rsizeX = 600;  // the jshellLab's RSyntaxEditor jFrame size
        static public int rsizeY = 400;  
        static public int rlocX = 10;   // location 
        static public int rlocY = 10; 
        
        static public double figAreaRelSize = 0.9;  // the relative area of the figure plot area
        
    static public int threadCnt = 0;  // the number of threads created

    static public int maxNumberOfRecentFiles = 20;
    static public String jshellLabRecentFilesList = "jshellLabRecentFiles.txt";   // the file for storing list of recent files
    
  
    public static String [] loadedToolboxesNames;  // the names of the toolboxes
    public static int currentToolboxId = 0; 
    public static int maxNumOfToolboxes = 30;

    public static final double nearZeroValue = 1.0e-10;
    
      
    
    // Graphics Configuration
    public static int  maxPointsToPlot = 40;  // limit on the number of points to plot when in point plot mode
    public static int  plotPointWidth = 2;    // control the size of the point at the plots 
    public static int  plotPointHeight =2; 
    public static int  markLineSize = 5;
    public static int  figGridSizeX = 30; 
    public static int  figGridSizeY = 30;     
    public static int  figFrameSizeX = 800; 
    public static int  figFrameSizeY = 600;     
    public static int  limitForLargeRangeOfValues = 10;
    public static double figZoomScaleFactor = 0.5;
    public static int currentMaxNumberOfZooms = 5;

    // global variables that change dynamically during a working session
    static public String  jShellLabCommandHistoryFile = "jShellLabCommandHistory.log";
    static public String jShellLabFavoritePathsFile = "jShellLabFavoritePaths.log";
    static public int  numOfHistoryCommandsToKeep  = 10;  // size of the command history list 
    static public String DirHavingFile;  // directory having the currently requested file
    static public Properties settings;  // for load/save global properties
    static public String selectedStringForAutoCompletion;
    
    public static AutoCompletion  AutoCompletionJShell;
    public static AutoCompletionWorkspace autoCompletionWorkspace;
    
    public static Hashtable  autoCompletionDescr = new Hashtable(300);
    public static Hashtable autoCompletionDetails = new Hashtable(300);
    public static int numTokTypes = 0;   // counts the number of token types that the lexical analyzer returns
    
    // keep main objects
    public static jshellLab   gLabMainFrame = null;
        public static int xSizeTab;
    public static int ySizeTab;
    public static AutoCompletionFrame autoCompletionFrame = null;
    public static  jshellLabEditor  myGEdit = null;
  
    public static Runtime rt;  // the runtime for observing available memory
    public static long memAvailable;  // free memory available
    public static double helpMagnificationFactor=1.0;
    public static File forHTMLHelptempFile;
   
    
     static public boolean displayLatexOnEval = true;
    static public int FONT_SIZE_TEX = 18;
  
      
 
  static public Socket sclient = null;     // client socket
  static public InputStream   clientReadStream = null;   // client's read stream
  static public OutputStream  clientWriteStream =  null;   // client's write stream 
  static public DataInputStream    reader = null;   
  static public DataOutputStream     writer = null;
 
    
   // codes for JShellLab server computations
    static public final int exitCode = -1;   // code for server to exit
    static public final int svdCode = 1;   // code to perform an SVD computation

    static public String serverIP = "127.0.0.1";  // the IP address of the server
    static public int jshellLabServerPort = 8000;   // port on which jshellLab server is listening
    public static int consoleCharsPerLine;


    
    static public String buildTitle() {
      String mainFrameTitle =        "JShellLab based on JShell "+
                   ",   "+System.getProperty("java.vm.name", "").toLowerCase()+",  "+ System.getProperty("os.name", "").toLowerCase()+
                   "  "+ System.getProperty("os.arch", "").toLowerCase()+" ,   "+ "( "+ dateOfBuild+" ) ";
    return mainFrameTitle;
    }
    
    public static void initGlobals()
    {
        //  ScriptEngineManager kfactory = new ScriptEngineManager();
         // create a JavaScript engine
         //  ScriptEngine kotlinEngine  = kfactory.getEngineByName("kotlin");
         
         // ScriptEngineManager mgr = new ScriptEngineManager();
         //System.out.println(mgr);
         
         kotlinEngine = new ScriptEngineManager().getEngineByName("kotlin");
        try {
         for (String str: GlobalValues.jshellBasicGlobalImports) {
             str = str.trim().replace("static", " ");
             if (str.contains("import") ) {
                 System.out.println("evaluating: "+str);
             
               kotlinEngine.eval(str);
         }
         }
                 
        }
        catch (ScriptException e)  {
            e.printStackTrace();
        }
                 
                 
                 
                 
        // kotlinEngine.eval("var ff=8; ff");
      //   System.out.println(kotlinEngine);
         /* java.util.List<ScriptEngineFactory> factories = mgr.getEngineFactories();
         System.out.println(factories);
         
         for (ScriptEngineFactory factory : factories) {
         
         System.out.println("ScriptEngineFactory Info");
         
         String engName = factory.getEngineName();
         String engVersion = factory.getEngineVersion();
         String langName = factory.getLanguageName();
         String langVersion = factory.getLanguageVersion();
         
         System.out.println("Script Engine: "+engName+", engVersion = "+engVersion);
         
         java.util.List<String> engNames = factory.getNames();
         for(String name : engNames) {
         System.out.println("Engine Alias: "+ name);
         }
         
         System.out.println("Language: "+ langName + " version = "+ langVersion);
         
         }
         */
         //    if (kotlinEngine==null) {
         //       JOptionPane.showMessageDialog(null, "kotlin engine init error");
         //  }
         
         new GlobalValues();    // it is required to init properly
         
         
         if (Desktop.isDesktopSupported())
             desktop = Desktop.getDesktop();
         else
             useSystemBrowserForHelp = false;  // cannot use system browser
         
         GlobalValues.fmtString.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
         
         Toolkit tk =Toolkit.getDefaultToolkit();
         desktophints = (Map)(tk.getDesktopProperty("awt.font.desktophints"));
         
         hostIsUnix = true;
         if (File.separatorChar!='/')
             hostIsUnix=false;
         
         myGEdit = null;
         loadedToolboxesNames = new String[maxNumOfToolboxes];
         
         jartoolboxes= new Vector();
         jartoolboxesLoadedFlag = new HashMap<String, Boolean>();
         
         GlobalValues.fmtMatrix.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
         GlobalValues.fmtString.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
         
         java.util.Map<String, String>  userEnv = System.getenv();
         
         int idx = GlobalValues.jarFilePath.lastIndexOf(File.separatorChar);
         if (idx==-1) {
             GlobalValues.homeDir = ".";
         }
         else
             GlobalValues.homeDir = GlobalValues.jarFilePath.substring(0, idx);
         
         System.out.println("homeDir= "+GlobalValues.homeDir);
         
         
         GlobalValues.workingDir = System.getProperty("user.dir");
         
         GlobalValues.DirHavingFile = GlobalValues.workingDir;
         //   GlobalValues.pascalWorkingDir = homeDir;
         
         hostIsUnix = true;
         if (File.separatorChar!='/')
             hostIsUnix=false;
         
         myGEdit = null;
         loadedToolboxesNames = new String[maxNumOfToolboxes];
         
         boolean foundConfigFileFlag = false;   //exists configuration file?
         try
         {
             settings = new Properties();
             
             FileInputStream in = null;
             
             // the jShellLab's configuration file
             String configFileName = workingDir+File.separatorChar+"JShellLab.props";
             File configFile = new File(configFileName);
             if (configFile.exists())   {  // configuration file exists
                 in = new FileInputStream(configFile);
                 settings.load(in);   // load the settings
                 foundConfigFileFlag = true;
                 GlobalValues.jshellLabPropertiesFile = configFileName;
             }
         }
         
         catch (IOException e)
         {
             e.printStackTrace();
         }
         ScreenDim = Toolkit.getDefaultToolkit().getScreenSize();
         
         if (foundConfigFileFlag == false)   { // configuration file not exists, thus pass default configuration
             
             rememberSizesFlag = false;   // since configured sizes for frames do not exist use the default sizes
             
             //position the frame in the centre of the screen
             int xSizeMainFrame = (int)((double)ScreenDim.width/1.4);
             int ySizeMainFrame = (int)((double)ScreenDim.height/1.4);
             GlobalValues.locX  = (int)((double)ScreenDim.width/10.0);
             GlobalValues.locY = (int)((double)ScreenDim.height/10.0);
             GlobalValues.rlocX  = 10;
             GlobalValues.rlocY = 10;
             
             GlobalValues.sizeX = xSizeMainFrame;
             GlobalValues.sizeY = ySizeMainFrame;
             
             GlobalValues.rsizeX = xSizeMainFrame;
             GlobalValues.rsizeY = ySizeMainFrame;
             
             
             
             
             if (GlobalValues.useAlwaysDefaultImports)
                 settings.put("useAlwaysDefaultImportsProp", "true");
             else
                 settings.put("useAlwaysDefaultImportsProp", "false");
             
             
             settings.put("widthProp",  String.valueOf(xSizeMainFrame));
             settings.put("heightProp", String.valueOf(ySizeMainFrame));
             settings.put("xlocProp",  String.valueOf(GlobalValues.locX));
             settings.put("ylocProp",  String.valueOf(GlobalValues.locY));
             
             settings.put("rwidthProp",  String.valueOf(xSizeMainFrame));
             settings.put("rheightProp", String.valueOf(ySizeMainFrame));
             settings.put("rxlocProp",  String.valueOf(GlobalValues.rlocX));
             settings.put("rylocProp",  String.valueOf(GlobalValues.rlocY));
             
             
             settings.setProperty("uiFontNameProp","Times New Roman");
             settings.setProperty("uiFontSizeProp", "14");
             settings.setProperty("outConsFontNameProp", "Lucida");
             settings.setProperty("outConsFontSizeProp", "14");
             
             String  prefix = "/";
             if (hostIsUnix==false)
                 prefix = "C:\\";
             
             String initialgLabPath  =prefix;
             
             String userDir = workingDir;
             
             
             settings.put("gLabWorkingDirProp", userDir);
             
         } // configuration file not exists
         
         
         GlobalValues.fmtMatrix.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
         GlobalValues.fmtString.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
         myGEdit = null;
         
         
    
        
        
    }   
    
    // Initialises the global values 
    public GlobalValues()
    {
        ClassLoader parentClassLoader = getClass().getClassLoader();
        extensionClassLoader = new  ExtensionClassLoader(GlobalValues.jshellLabClassPath+File.separator+"."+File.separator+GlobalValues.jarFilePath, parentClassLoader);
                   
    }   

    
    
    public static void incrementToolboxCount() {
        currentToolboxId++;
        if (currentToolboxId > maxNumOfToolboxes)  { 
            JOptionPane.showMessageDialog(null, "Maximum toolbox count exceeded", "Cannot load additional toolboxes", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // approximates the matrix size for which multithreading becomes faster than serial implementation
    public static int computeProperMatrixSizeThresholdForMultithreading() {
    
        int testedSize  = 50;  // starting value
        
        while (true)  {
            Matrix m = Matrix.rand(testedSize, testedSize);  // create a matrix to test the serial case vs the multithreaded case
            
            jShellLabSciCommands.BasicCommands.tic();
            
            // benchmark serial multiplication
            Matrix mm = m.multiplyTest(m, false).multiplyTest(m, false).multiplyTest(m, false).multiplyTest(m, false);  // perform a multiplication in order to time it
            double delaySerial = jShellLabSciCommands.BasicCommands.toc();
            
            // benchmark parallel multiplication
            jShellLabSciCommands.BasicCommands.tic();
            Matrix mm2 = m.multiplyTest(m, true).multiplyTest(m, true).multiplyTest(m, true).multiplyTest(m, true);  // perform a multiplication in order to time it
            double delayParallel = jShellLabSciCommands.BasicCommands.toc();
            
            if (delayParallel > delaySerial)   // increment to larger size
                testedSize += testedSize;
            else break;  // exit the loop
                  
        }
        
        return testedSize;  // return size about at which parallel implementation becomes faster
        
        
    } 
    
    
// pass properties readed from settings Property String to the jshellLab workspace structures
    public static void passPropertiesFromSettingsToWorkspace(Properties settings)
     {
          
        String locXstr = settings.getProperty("xlocProp");
        if (locXstr != null)      locX = Integer.parseInt(locXstr);  // locX specified
        String locYstr = settings.getProperty("ylocProp");
        if (locYstr != null)      locY = Integer.parseInt(locYstr);  // locY specified
        
       String sizeXstr = settings.getProperty("widthProp");
        if (sizeXstr != null)      sizeX = Integer.parseInt(sizeXstr);  // sizeX specified
        String sizeYstr = settings.getProperty("heightProp");
        if (sizeYstr != null)      sizeY = Integer.parseInt(sizeYstr);  // sizeY specified
        
        // RSyntaxArea editor frame size
        String rlocXstr = settings.getProperty("rxlocProp");
        if (rlocXstr != null)      rlocX = Integer.parseInt(rlocXstr);  // rlocX specified
        String rlocYstr = settings.getProperty("rylocProp");
        if (rlocYstr != null)      rlocY = Integer.parseInt(rlocYstr);  // rlocY specified
        
       String rsizeXstr = settings.getProperty("rwidthProp");
        if (rsizeXstr != null)      rsizeX = Integer.parseInt(rsizeXstr);  // rsizeX specified
        String rsizeYstr = settings.getProperty("rheightProp");
        if (rsizeYstr != null)      rsizeY = Integer.parseInt(rsizeYstr);  // rsizeY specified
             
        // main menus
        String uiFontName = settings.getProperty("uiFontNameProp");
        if (uiFontName==null) uiFontName= GlobalValues.uiFontName;
        String uiFontSize = settings.getProperty("uiFontSizeProp");
        if (uiFontSize==null) uiFontSize= GlobalValues.uiFontSize;
        GlobalValues.uiFontName = uiFontName;
        GlobalValues.uiFontSize = uiFontSize;
        GlobalValues.uifont = new Font(GlobalValues.uiFontName, Font.PLAIN, Integer.parseInt(GlobalValues.uiFontSize));
                
          // pop-up menus
        String puiFontName = settings.getProperty("puiFontNameProp");
        if (puiFontName==null)  puiFontName= GlobalValues.puiFontName;
        String puiFontSize = settings.getProperty("puiFontSizeProp");
        if (puiFontSize==null) puiFontSize= GlobalValues.puiFontSize;
        GlobalValues.puiFontName = puiFontName;
        GlobalValues.puiFontSize = puiFontSize;
        GlobalValues.puifont = new Font(GlobalValues.puiFontName, Font.PLAIN, Integer.parseInt(GlobalValues.puiFontSize));
        
          // general GUI components
        String guiFontName = settings.getProperty("guiFontNameProp");
        if (guiFontName==null)  guiFontName= GlobalValues.guiFontName;
        String guiFontSize = settings.getProperty("guiFontSizeProp");
        if (guiFontSize==null) guiFontSize= GlobalValues.guiFontSize;
        GlobalValues.guiFontName = guiFontName;
        GlobalValues.guiFontSize = guiFontSize;
        GlobalValues.guifont = new Font(GlobalValues.guiFontName, Font.PLAIN, Integer.parseInt(GlobalValues.guiFontSize));
        
        
          // html components
        String htmlFontName = settings.getProperty("htmlFontNameProp");
        if (htmlFontName==null)  guiFontName= GlobalValues.htmlFontName;
        String htmlFontSize = settings.getProperty("htmlFontSizeProp");
        if (htmlFontSize==null) htmlFontSize= GlobalValues.htmlFontSize;
        GlobalValues.htmlFontName = htmlFontName;
        GlobalValues.htmlFontSize = htmlFontSize;
        GlobalValues.htmlfont = new Font(GlobalValues.htmlFontName, Font.PLAIN, Integer.parseInt(GlobalValues.htmlFontSize));
    
              // buttons components
        String buiFontName = settings.getProperty("buiFontNameProp");
        if (buiFontName==null)  buiFontName= GlobalValues.buiFontName;
        String buiFontSize = settings.getProperty("buiFontSizeProp");
        if (buiFontSize==null) buiFontSize= GlobalValues.buiFontSize;
        GlobalValues.buiFontName = buiFontName;
        GlobalValues.buiFontSize = buiFontSize;
        GlobalValues.buifont = new Font(GlobalValues.buiFontName, Font.PLAIN, Integer.parseInt(GlobalValues.buiFontSize));
        
        String outConsFontName = settings.getProperty("outConsFontNameProp");
        if (outConsFontName==null) outConsFontName= GlobalValues.outConsoleFontName;
        String outConsFontSize = settings.getProperty("outConsFontSizeProp");
        if (outConsFontSize==null) outConsFontSize= GlobalValues.outConsoleFontSize;
        GlobalValues.outConsoleFontName = outConsFontName;
        GlobalValues.outConsoleFontSize = outConsFontSize;
        
        
        String gLabConsFontName = settings.getProperty("gLabConsFontNameProp");
        if (gLabConsFontName==null) gLabConsFontName= GlobalValues.gLabConsoleFontName;  
        String gLabConsFontSize = settings.getProperty("gLabConsFontSizeProp");
        if (gLabConsFontSize==null)  gLabConsFontSize= GlobalValues.gLabConsoleFontSize;
        GlobalValues.gLabConsoleFontName = gLabConsFontName;
        GlobalValues.gLabConsoleFontSize = gLabConsFontSize;
        
        //  Decimal digit formatting properties
        String vecDigitsSetting = settings.getProperty("VecDigitsProp");
        if (vecDigitsSetting != null) {
            int vprec = Integer.parseInt(vecDigitsSetting);
            jShellLabSci.PrintFormatParams.setVecDigitsPrecision(vprec);
          }
         
        String matDigitsSetting = settings.getProperty("MatDigitsProp");
        if (matDigitsSetting != null) {
            int mprec = Integer.parseInt(matDigitsSetting);
            jShellLabSci.PrintFormatParams.setMatDigitsPrecision(mprec);
        }
         
         String mxRowsSetting = settings.getProperty("mxRowsProp");
         if (mxRowsSetting != null) {
            int mxrows = Integer.parseInt(mxRowsSetting);
            jShellLabSci.PrintFormatParams.setMatMxRowsToDisplay(mxrows);
         }
        
         String mxColsSetting = settings.getProperty("mxColsProp");
         if (mxColsSetting != null)  {
            int mxcols = Integer.parseInt(mxColsSetting);
            jShellLabSci.PrintFormatParams.setMatMxColsToDisplay(mxcols);
         }
         
         String verboseOutputSetting = settings.getProperty("verboseOutputProp");
         if (verboseOutputSetting!=null)
           if (verboseOutputSetting.equalsIgnoreCase("true"))
              jShellLabSci.PrintFormatParams.setVerbose(true);
           else
              jShellLabSci.PrintFormatParams.setVerbose(false);
        
          
        boolean paneFontSpecified = true;
        String paneFontName = settings.getProperty("paneFontNameProp");
        if (paneFontName!=null)  
             GlobalValues.paneFontName = paneFontName;
        else
            paneFontSpecified = false;
        String paneFontSize = settings.getProperty("paneFontSizeProp");
        if (paneFontSize!=null)   
            GlobalValues.paneFontSize =  Integer.valueOf(paneFontSize);
        else
            paneFontSpecified = false;
      
        GlobalValues.paneFontSpecified = paneFontSpecified;
      
    
        
       
        
         
    } 
    
 // updates the paths of targetVector by adding additionalPaths and any subdirectories
public static void updatePathVectors(Vector targetVector,  String additionalPaths, boolean recurse) {
    if (targetVector != null)  {
            StringTokenizer  tokenizer;
            if (hostIsUnix) tokenizer = new StringTokenizer(additionalPaths, "\n:\t ");
            else tokenizer = new StringTokenizer(additionalPaths,"\n;\t ");
            while (tokenizer.hasMoreTokens())  {  // construct full paths to search for j-files
                String nextToken = tokenizer.nextToken()+File.separatorChar;
                if (recurse == false)
                    targetVector.add(nextToken);
                else
                    jshellLabUtils.appendAllSubDirectories(nextToken, targetVector);
          }
          
    }
}
    
// pass properties from the jshellLab workspace structures to the settings Property String to 
    public static void passPropertiesFromWorkspaceToSettings(Properties settings)
     {
         
     
         settings.setProperty("mulMultithreadingLimitProp", Integer.toString(GlobalValues.mulMultithreadingLimit));
         
        
        settings.setProperty("widthProp", String.valueOf(globalEditorPane.getSize().width));
        settings.setProperty("heightProp", String.valueOf(globalEditorPane.getSize().height));
        int xloc = globalEditorPane.getLocation().x;
        int yloc = globalEditorPane.getLocation().y;
        settings.setProperty("xlocProp", String.valueOf(xloc));
        settings.setProperty("ylocProp", String.valueOf(yloc));
        
        // RSyntaxArea editor
        settings.setProperty("rwidthProp", String.valueOf(jshellLabEditor.currentFrame.getWidth()));
        settings.setProperty("rheightProp", String.valueOf(jshellLabEditor.currentFrame.getHeight()));
        int rxloc = jshellLabEditor.currentFrame.getLocation().x;
        int ryloc = jshellLabEditor.currentFrame.getLocation().y;
        settings.setProperty("rxlocProp", String.valueOf(rxloc));
        settings.setProperty("rylocProp", String.valueOf(ryloc));
        
     settings.setProperty("paneFontNameProp", String.valueOf(GlobalValues.globalEditorPane.getFont().getName()));
     int paneFontSize =   GlobalValues.globalEditorPane.getFont().getSize();
     settings.setProperty("paneFontSizeProp", String.valueOf(paneFontSize)); 
        
     
        Font outConsFont =  GlobalValues.outputPane.getFont();
        settings.setProperty("outConsFontNameProp", outConsFont.getName());
        settings.setProperty("outConsFontSizeProp", String.valueOf(outConsFont.getSize()));
        
        // main menus
        settings.setProperty("uiFontNameProp", GlobalValues.uifont.getName());
        settings.setProperty("uiFontSizeProp", String.valueOf(GlobalValues.uifont.getSize()));
        
        // popup menus
        settings.setProperty("puiFontNameProp", GlobalValues.puifont.getName());
        settings.setProperty("puiFontSizeProp", String.valueOf(GlobalValues.puifont.getSize()));
        
        // html help
        settings.setProperty("htmlFontNameProp", GlobalValues.htmlfont.getName());
        settings.setProperty("htmlFontSizeProp", String.valueOf(GlobalValues.htmlfont.getSize()));
        
        // rest GUI components
        settings.setProperty("guiFontNameProp", GlobalValues.guifont.getName());
        settings.setProperty("guiFontSizeProp", String.valueOf(GlobalValues.guifont.getSize()));
        
        // GUI buttons
        settings.setProperty("buiFontNameProp", GlobalValues.buifont.getName());
        settings.setProperty("buiFontSizeProp", String.valueOf(GlobalValues.buifont.getSize()));
        
        settings.setProperty("outConsFontNameProp", String.valueOf(GlobalValues.consoleOutputWindow.output.getFont().getName()));
        settings.setProperty("outConsFontSizeProp", String.valueOf(GlobalValues.consoleOutputWindow.output.getFont().getSize()));
        
        
        //  Decimal digit formatting properties
        
         int vprec = jShellLabSci.PrintFormatParams.getVecDigitsPrecision();
         settings.setProperty("VecDigitsProp", String.valueOf(vprec));
         
         int mprec = jShellLabSci.PrintFormatParams.getMatDigitsPrecision();
         settings.setProperty("MatDigitsProp", String.valueOf(vprec));
         
         int mxrows = jShellLabSci.PrintFormatParams.getMatMxRowsToDisplay();
         settings.setProperty("mxRowsProp", String.valueOf(mxrows));
         
         int mxcols = jShellLabSci.PrintFormatParams.getMatMxColsToDisplay();
         settings.setProperty("mxColsProp", String.valueOf(mxcols));
         
         
         if (jShellLabSci.PrintFormatParams.getVerbose()==true)
             settings.setProperty("verboseOutputProp", "true");
         else
             settings.setProperty("verboseOutputProp", "false");
         
        
     }

       
   
    // read the user defined  classpath components 
    public static void readUserPaths() {
     try {
      File file = new File(GlobalValues.userPathsFileName);  // the file name that keeps the user paths
      FileReader fr = new FileReader(file);
      BufferedReader in = new BufferedReader(fr);
      String currentLine;
      GlobalValues.jshellLabSciClassPathComponents.clear();
      
      while ( (currentLine = in.readLine())!= null)  {
          if (GlobalValues.jshellLabSciClassPathComponents.contains(currentLine)==false)
             GlobalValues.jshellLabSciClassPathComponents.add(currentLine);
       }
     }
            catch (IOException ioe) {
                System.out.println("Exception trying to read "+GlobalValues.userPathsFileName);
                 return;
            }
        
    }
    
    
    
    public static void writeUserPaths() {
    
        StringBuffer sb = new StringBuffer();
        
        // handle any specified additional user specified paths
        if (GlobalValues.jshellLabSciClassPathComponents !=null)  {
            int userSpecPathsCnt = GlobalValues.jshellLabSciClassPathComponents.size();
            String userPathsSpecString="";
            for (int k=0; k<userSpecPathsCnt; k++) {
                String currentToolbox  = GlobalValues.jshellLabSciClassPathComponents.elementAt(k).toString().trim();
                sb.append(currentToolbox+"\n");
             }
          }
         
        try {
                // take the program's text and save it to a temporary file
                File tempFile = new File(GlobalValues.userPathsFileName);
                FileWriter fw = new FileWriter(tempFile);
                fw.write(sb.toString(), 0, sb.length());
                fw.close();
             }
            catch (IOException ioe) {
                System.out.println("Exception trying to write Glab user paths ");
                 return;
            }
    }
    
    
// clear the properties 
    public static void clearProperties()  {
       settings.setProperty("GroovySciClassPathProp", homeDir);
       settings.setProperty("gLabWorkingDirProp","");
       }
       
    /** @return actual working directory */
    protected String getWorkingDirectory()
    {
        return workingDir;
    }

    /** @param set working directory */
    protected void setWorkingDirectory(String _workingDir)
    {
        workingDir = _workingDir;
        
    }

    
          
 }

