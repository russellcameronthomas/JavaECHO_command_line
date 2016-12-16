package edu.gmu.cds.javaecho;

import edu.gmu.cds.javaecho.debug.Assert;
import java.io.ByteArrayInputStream;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Command line JavaECHO - prints results to System.out
 * 
 * 12-14-2016  RCT - Created executable class by pulling code from ECHOgui.java
 *           Replaced depreciated classes:
 *             - Vector and Hashtable with ArrayList and HashMap, 
 *             - Enumeration with Iterator
 *             - StringBufferInputStream with ByteArrayInputStream
 *
 */
public class App {

   String echoData = new String();
   

    echoOptions eob_ = new echoOptions();
   
    ArrayList<String> accepted_ = new ArrayList();
    ArrayList<String> rejected_ = new ArrayList();
    
    ArrayList<String> titles = new ArrayList();
   
   ECHO echo;
   
   private int selectedTitle = 0;
   
public App(){
    this.selectedTitle = 0;
}

public App(int i){
    this.selectedTitle = i;
}
   
public int getSelectedTitle (){
    return selectedTitle;
}

public void setSelectedTitle(int i){
    selectedTitle = i;
}

private String arrayListToString(ArrayList a){
    String s = "";
    Iterator it = a.iterator();
    while (it.hasNext()){
        s += it.next() + "\n";
    }
    return s;
}
    
public boolean run(){
     initializeTitles();
      setEchoData();
      System.out.println("Input: " + titles.get(selectedTitle) + "\n" + echoData);
      makeECHO();
      ECHOsolver es;
      String alg = "connectionist";      // RCT added. Make this a runtime argument eventually
      if (alg.equals("connectionist")) {
	es = new ConnectionistSolver(echo);
      } else if (alg.equals("brute force")) {
	es = new BruteForceSolver(echo);
      } else  
	es = new GreedySolver(echo,eob_.getMaxFlips(),eob_.getMaxTries());
     
      runSolver(es);
      System.out.println("\nOutput:");
      System.out.println("=> Accepted:\n" + arrayListToString(accepted_));
      System.out.println("=> Rejected:\n" + arrayListToString(rejected_));
      return true;
  }

  //
  // PRE: es is an instantiated solver, and echo is instantiated
  // POST: calls the solve method of the given solver and collects the
  //       accepted and rejected units
  //
  private void runSolver(ECHOsolver es)
  {
    Assert.notNull(es);
    Assert.notNull(echo);
    es.solve();
    
    
    es.summarizeRun();
    outputEcho(es.accepted(),accepted_);
    outputEcho(es.rejected(),rejected_);
  }

  private boolean makeECHO(){
    try {
      String name = titles.get(selectedTitle);
      ByteArrayInputStream sbis = 
	new ByteArrayInputStream(echoData.getBytes("UTF-8"));
      echo = new ECHO(name,"GUI test",sbis,eob_.getSimplicityImpact(),
		       eob_.getDefaultWeight(), eob_.getExcitationWeight(),
		       eob_.getDataExcitation(), 
		       eob_.getInhibitionWeight());
    } catch (IOException e) {
      String msg2 = "    ECHO error message: "+e.getMessage();
      return false;
    } // try
    return true;
  }

  private void setEchoData(){
    String title = titles.get(selectedTitle);
    String s = echoStrings.get(title);
    if (s != null) 
      echoData = s; 
  }
    
    
   private void outputEcho(ArrayList v, ArrayList output){
    output.clear();
    Iterator e = v.iterator();
    while (e.hasNext()) {
      ECHOunit u = (ECHOunit)e.next();
      output.add(u.name()+", "+u.activation());
    } // while
  }

  //
  // based on echoStrings v1.4
  //
  private void initializeTitles(){

    titles.add("ulcers 1983");
    titles.add("ulcers 1994");
    titles.add("breadth");
    titles.add("breadth 2");
    titles.add("analogy");
    titles.add("being explained");
    titles.add("simplicity");
    titles.add("unification");
    titles.add("evidence");
    titles.add("Lavoisier");
    titles.add("Darwin");
    titles.add("Wegener");
  }
  
  
    public static void main( String[] args )
    {
        System.out.println( "JavaECHO start." );
        // create Options object
       Options options = new Options();
         // add t option
        options.addOption("d", true, "index number of dataSet (starting with 0)");
        
        CommandLineParser parser = new DefaultParser();
        int titleIndex = 0;
        try{
            CommandLine line = parser.parse( options, args);
            if( line.hasOption( "d" ) &&  line.getOptionValue( "t" ) != null) {
                 // initialise the member variable
              //System.out.println( "Option t = " + line.getOptionValue( "t" ));
              titleIndex = Integer.valueOf(line.getOptionValue( "t" ));
             }
        } catch (ParseException e){
            System.err.println("Error in command line arguments: " + e.getLocalizedMessage());
        }
        
        App app = new App(titleIndex);
        app.run();
        System.out.println( "JavaECHO run completed." );
    }
}
