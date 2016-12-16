// $Id: ECHOunit.java,v 1.5 1997/07/20 20:10:13 tjdonald Exp $

package edu.gmu.cds.javaecho;

/*

   An ECHOunit is a Unit that stores information about what other units
   this unit explains, is explained by, and is a cohypothesis with.

*/

/*
 * 12-14-2016  RCT:Replaced depreciated classes:
 *             - Vector and Hashtable with ArrayList and HashMap, 
 *             - Enumeration with Iterator
 *             - StringBufferInputStream with ByteArrayInputStream
*/

import edu.gmu.cds.javaecho.debug.Assert;
import java.util.*;

final class ECHOunit extends Unit {

  private final boolean invariantCheckingOn_ = false;

  //
  // these are containers of ArrayLists, each ArrayList contains one set of 
  // units corresponding to one explanation of this unit
  //
  private ArrayList explainedBy_ = new ArrayList();
  private Map explainerOf_ = new HashMap();

  private Map allExplainedByUnits_ = new HashMap();

  private Boolean flag_;

  public ECHOunit(String name, String descrip, float act)
  {
    super(name,descrip,act);
  }

  public ECHOunit(String name, String descrip, double act)
  {
    this(name,descrip,(float)act);
  }

  ECHOunit(String name, String descrip)
  {
    this(name,descrip,0.0);
  }

  ECHOunit(String name)
  {
    this(name,"",0.0);
  }

  ECHOunit(double act)
  {
    this((float)act);
  }

  ECHOunit(float act)
  {
    this("","",act);
  }

  public boolean equals(Object x)
  {
    if (x instanceof ECHOunit) {
      ECHOunit u = (ECHOunit)x;
      return super.equals(u) && u.explainerOf_.equals(explainerOf_) &&
	u.explainedBy_.equals(u.explainedBy_);
    } else
      return false;
  }

  //
  // hashCode must return the same value when two ECHOunits are equal
  //
  public int hashCode()
  {
    return name().hashCode();
  }

  //
  // POST: returns a Map of all the other units that this unit
  //       explains
  public Map explains()
  {
    return explainerOf_;
  }

  //
  // POST: returns a ArrayList of all the unit sets that this unit is explained
  //       by
  //
  public ArrayList explainedBy()
  {
    return explainedBy_;
  }

  public ECHOunit getExplainedBy(String uname)
  {
    return (ECHOunit)allExplainedByUnits_.get(uname);
  }

  public ECHOunit getExplainedBy(ECHOunit u)
  {
    return getExplainedBy(u.name());
  }

  //
  // PRE: u is non-null
  // POST: u is a member of explainerOf_
  //
  public void addExplainerOf(ECHOunit u)
  {
    if (!explainerOf_.containsKey(u.name()))
      explainerOf_.put(u.name(),u);
  }

  //
  // PRE: v is non-null and non-empty set of ECHOunits that explain 
  //      this ECHOunit
  // POST: v is a member of explainedBy_ 
  //
  public void addExplainedBy(ArrayList v)
  {
    Assert.notNull(v);
    Assert.isTrue(v.size()>0);
    addAllUnits(v);
    explainedBy_.add(v);
  }

  private void addAllUnits(ArrayList v)
  {
    Iterator e = v.iterator();
    while (e.hasNext()) {
      ECHOunit u = (ECHOunit)e.next();
      if (!allExplainedByUnits_.containsKey(u.name()))
	allExplainedByUnits_.put(u.name(),u);
    } // while
  }

  public Map allExplainedByUnits()
  {
    return allExplainedByUnits_;
  }

  //
  // PRE: u is non-null
  // POST: returns an explaination ArrayList from explainedBy_ of which 
  //       u is a member
  //
  public ArrayList explainedBy(ECHOunit u)
  {
    ArrayList v = new ArrayList(1);
    v.add(u);
    return explainedBy(v);
  }

  //
  // PRE: units and non-empty is non-null
  // POST: returns a ArrayList of some explanation contained in explainedBy_ 
  //       of which units is a subset; returns null if there is no such 
  //       ArrayList in explainedBy_
  //
  public ArrayList explainedBy(ArrayList units)
  {
    Assert.notNull(units);
    Assert.isTrue(units.size()>0,"units is non-empty");
    Iterator e = explainedBy_.iterator();
    while (e.hasNext()) {
      ArrayList v = (ArrayList)e.next();
      Iterator ve = v.iterator();
      while (ve.hasNext()) {
	ECHOunit eu = (ECHOunit)ve.next();
	Iterator unitEnum = units.iterator();
	while (unitEnum.hasNext()) {
	  ECHOunit u = (ECHOunit)unitEnum.next();
	  if (eu.name().equals(u.name()))
	    return v;
	} // while
      } // while
    } // while
    return null;
  }

  public boolean hasAsCohypotheses(ECHOunit a, ECHOunit b)
  {
    Iterator e = explainedBy_.iterator();
    while (e.hasNext()) {
      ArrayList v = (ArrayList)e.next();
      if (v.contains(a) && v.contains(b))
	return true;
    } // while
    return false;
  }

  //
  // PRE: u is non-null
  // POST: returns true if this unit is an explainer of u
  //
  public boolean explains(ECHOunit u)
  {
    return explainerOf_.containsKey(u.name());
  }


  public ArrayList someExplanationContainsBoth(ECHOunit a, ECHOunit b)
  {
    Iterator e = explainedBy_.iterator();
    while (e.hasNext()) {
      ArrayList exp = (ArrayList)e.next();
      if (exp.contains(a.name()) && exp.contains(b.name()))
	return exp;
    } // while
    return null;
  }

  public Boolean flag()
  {
    return flag_;
  }

  public void setFlag(Boolean flag)
  {
    flag_ = flag;
  }

  public void checkInvariant()
  {
    super.checkInvariant();
    if (invariantCheckingOn_) {
    
      if (explainerOf_ != null) {
	Iterator e1 = explainerOf_.keySet().iterator();
	while (e1.hasNext()) {
	  Object x = e1.next();
	  Assert.isTrue(x instanceof ECHOunit,"everything in explainerOf_"+
			" is an ECHOunit");
	} // while
      } // if

      if (explainedBy_ != null) {
	Iterator e2 = explainedBy_.iterator();
	while (e2.hasNext()) {
	  Object x = e2.next();
	  Assert.isTrue(x instanceof ArrayList,"everything in explainedBy_"+
			" is a ArrayList");
	} // while
      } // if
/*
      if (coHypotheses_ != null) {
	Enumeration e3 = coHypotheses_.elements();
	while (e3.hasMoreElements()) {
	  Object x = e3.nextElement();
	  Assert.isTrue(x instanceof ECHOunit,"everything in coHypotheses_"+
			" is an ECHOunit");
	} // while
      } // if
*/
    } // if
  }

} // class ECHOunit
