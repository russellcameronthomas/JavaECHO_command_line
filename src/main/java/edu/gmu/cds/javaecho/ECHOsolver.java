package edu.gmu.cds.javaecho;

/*

   Abstract base class for ECHO solvers.

*/

/*
 * 12-14-2016  RCT:Replaced depreciated classes:
 *             - Vector and Hashtable with ArrayList and HashMap, 
 *             - Enumeration with Iterator
 *             - StringBufferInputStream with ByteArrayInputStream
*/

import edu.gmu.cds.javaecho.debug.Assert;
import java.util.*;

abstract class ECHOsolver {

  protected ECHO echo_;
  protected ArrayList accepted_;
  protected ArrayList rejected_;
  protected ArrayList neutral_;

  //
  // PRE: v is not null
  // POST: returns a nicely formatted summary of v
  //
  protected String summary(ArrayList v)
  {
    Assert.notNull(v);
    String result = "";
    Iterator e = v.iterator();
    while (e.hasNext()) {
      ECHOunit u = (ECHOunit)e.next();
      result += u.name()+"="+u.activation()+" ";
    } // while
    return result;
  }
  
  abstract public void solve();
  abstract public void summarizeRun();

  public ArrayList accepted()
  {
    return accepted_;
  }

  public ArrayList rejected()
  {
    return rejected_;
  }



} // class ECHOsolver
