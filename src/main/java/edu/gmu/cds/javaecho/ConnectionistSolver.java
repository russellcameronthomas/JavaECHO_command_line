package edu.gmu.cds.javaecho;

import edu.gmu.cds.javaecho.debug.Assert;
import java.util.*;

/*
 * 12-14-2016  RCT:Replaced depreciated classes:
 *             - Vector and Hashtable with ArrayList and HashMap, 
 *             - Enumeration with Iterator
 *             - StringBufferInputStream with ByteArrayInputStream
*/

final class ConnectionistSolver extends ECHOsolver {

  //private ECHO echo_;
  private Map units_;
  private float initActivation_ = 0.01f;
  private float minChange_ = 0.001f;
  private int maxCycles_ = 200;
  private float thetaDecay_ = 0.05f;
  private Map netUnitInput_;
  //private ArrayList accepted_;
  //private ArrayList rejected_;
  //private ArrayList neutral_;
  private int cycleCount_;
  
  ConnectionistSolver(ECHO echo, int maxCycles, float minChange, 
		      float initActivation, float thetaDecay)
  {
    echo_ = echo;
    units_ = echo_.allUnits();
    maxCycles_ = maxCycles;
    minChange_ = minChange;
    initActivation_ = initActivation;
    thetaDecay_ = thetaDecay;
    createNetUnitInput();
  }

  ConnectionistSolver(ECHO echo)
  {
    echo_ = echo;
    units_ = echo_.allUnits();
    createNetUnitInput();
  }

  private void createNetUnitInput()
  {
    netUnitInput_ = new HashMap();
    Iterator e = units_.values().iterator();
    while (e.hasNext()) {
      ECHOunit u = (ECHOunit)e.next();
      netUnitInput_.put(u.name(),new Float(0.0f));
    } // while
  }

  //
  // POST: the standard network update algorithm is run, simultaneously
  //       updating the network units
  //
  public void solve()
  {
    cycleCount_ = 0;
    boolean someBigChange = true;
    initializeAllUnits();
    while ((cycleCount_ < maxCycles_) && someBigChange) {
      ++cycleCount_;
      calcNetUnitInput();
      someBigChange = updateAllUnits();
    } // while
    //if (!someBigChange)
      //System.out.println("someBigChange = "+someBigChange);
    tally();
  }

  //
  // POST: the activation for each unit in echo_ is updated; returns true
  //       if at least one unit changes by more than minChange_, false
  //       otherwise
  //
  private boolean updateAllUnits()
    {
      boolean someBigChange = false;
      Iterator e = units_.values().iterator();
      while (e.hasNext()) {
	ECHOunit u = (ECHOunit)e.next();
	float d = updateUnit(u);
	//System.out.println("updateUnit("+u.name()+")="+d);
	if (d>minChange_)
	  someBigChange = true;
      } // while
      return someBigChange;
  }

  //
  // POST: every ECHOunit in units_ with a negative activation is put in
  //       rejected_, positively activated units are put in accepted_,
  //       and nodes with 0 activation are put in neutral
  //
  private void tally()
  {
    accepted_ = new ArrayList();
    rejected_ = new ArrayList();
    neutral_ = new ArrayList();
    Iterator e = units_.values().iterator();
    while (e.hasNext()) {
      ECHOunit u = (ECHOunit)e.next();
      float act = u.activation();
      if (act == 0)
	neutral_.add(u);
      else if (act > 0)
	accepted_.add(u);
      else 
	rejected_.add(u);
    } // while
  }
  
  //
  // POST: every unit is set to initActivation_
  //
  private void initializeAllUnits()
  {
    Iterator e = units_.values().iterator();
    while (e.hasNext()) {
      ECHOunit u = (ECHOunit)e.next();
      u.setActivation(initActivation_);
    } // while
    echo_.getSpecialUnit().setActivation(1f);
  }

  //
  // PRE: u is non-null
  // POST: u's activation is set according to the unit update formula
  //       given in Table 4.5, p.101 of CR, and netUnitInput_ has been 
  //       called; returns the absolute value of the difference between
  //       the new and old activation
  //
  //
  // NOTE: the formula given in Table 4.5, p. 101 of CR is apparently wrong,
  //       since it can result in a unit activation outside the range
  //       [-1,1]. The formula below is borrowed from the MacECHO LISP
  //       version, which handles this range problem by using min and max
  //
  private float updateUnit(ECHOunit u)
  {
    if (u.name().equals(echo_.getSpecialUnit().name())) 
      return 0;    // special unit activation never changes
    else {
      float uNet = ((Float)netUnitInput_.get(u.name())).floatValue();
      float oldAct = u.activation();
      float newAct = Math.min(ECHOunit.max,
			      Math.max(ECHOunit.min,
				       oldAct*(1-thetaDecay_)+
				       uNet*((uNet>0) ? ECHOunit.max-oldAct :
					     oldAct-ECHOunit.min)));
      u.setActivation(newAct);

      return Math.abs(oldAct-newAct);
    } // if
  }


  //
  // POST: netInputUnit_ contains the net input for each unit
  //
  private void calcNetUnitInput()
  {
    //
    // set all the net inputs to 0
    //
    Iterator ue = units_.values().iterator();
    while (ue.hasNext()) {
      ECHOunit u = (ECHOunit)ue.next();
      netUnitInput_.put(u.name(),new Float(0.0f));
    } // while

    {
      Iterator ne = netUnitInput_.values().iterator();
      while (ne.hasNext()) {
	Float f = (Float)ne.next();
	Assert.isTrue(f.floatValue() == 0);
      } // while
    }

    Iterator e = echo_.allLinks().values().iterator();
    while (e.hasNext()) {
      Link l = (Link)e.next();
      ECHOunit A = l.endA();
      ECHOunit B = l.endB();
      float w = l.weight();
      String aname = A.name();
      String bname = B.name();

      Float a = (Float)netUnitInput_.get(aname);
      Assert.notNull(a);
      float newA = a.floatValue()+w*B.activation();
      netUnitInput_.put(aname,new Float(newA));
      
      Float b = (Float)netUnitInput_.get(bname);
      Assert.notNull(b);
      float newB = b.floatValue()+w*A.activation();
      netUnitInput_.put(bname,new Float(newB));
    } // while
  }

  public void setThetaDecay(float f)
  {
    thetaDecay_ = f;
  }

  public float getThetaDecay()
  {
    return thetaDecay_;
  }

/*
  public ArrayList accepted()
  {
    return accepted_;
  }

  public ArrayList rejected()
  {
    return rejected_;
  }
*/
  //
  // print out a report of the accepted and rejected units
  //
  public void summarizeRun()
  {
    System.out.println("Accepted units: "+summary(accepted_));
    System.out.println("Rejected units: "+summary(rejected_));
    System.out.println("Neutral units: "+summary(neutral_));
    System.out.println("");
    System.out.println("Theta decay = "+thetaDecay_+", max Cycles = "
		       +maxCycles_+", min change  = "+minChange_);
    System.out.println("#cycles = "+cycleCount_);
  }

/*
  private String summary(ArrayList v)
  {
    String result = "";
    Iterator e = v.keySet().iterator();
    while (e.hasNext()) {
      ECHOunit u = (ECHOunit)e.next();
      result += u.name()+"="+u.activation()+" ";
    } // while
    return result;
  }
*/

} // class ConnectionistSolver
