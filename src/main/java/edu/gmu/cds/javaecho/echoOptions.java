/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.gmu.cds.javaecho;

/**
 *
 * @author russellthomas
 * 
 * This is modified from echoOptionsBox, which depends on the existence of a GUI.
 * The current code simply returns the default values.
 * 
 * TO DO: all these parameter values to be set through command line arguments
 */
public class echoOptions {
  private float simplicityImpact_;
  private float defaultWeight_;
  //private float thetaDecay_;
  private float excitationWeight_;
  private float dataExcitation_;
  private float inhibitionWeight_;
  private int maxFlips_;
  private int maxTries_;
  
  public echoOptions(){ 
    simplicityImpact_ = ECHO.defaultSimplicityImpact;
    defaultWeight_ = ECHO.defaultWeight;
    //thetaDecay_ = echo.getThetaDecay();
    excitationWeight_ = ECHO.defaultExcitationWeight;
    dataExcitation_ = ECHO.defaultDataExcitation; 
    inhibitionWeight_ = ECHO.defaultInhibitionWeight;
    maxFlips_ = ECHO.defaulMaxFlips;
    maxTries_ = ECHO.defaulMaxTries;
  }
  
  public float getSimplicityImpact(){
    return simplicityImpact_;
  }

  public float getDefaultWeight()
  {
    return defaultWeight_;
  }

  public float getExcitationWeight()
  {
    return excitationWeight_;
  }

  public float getDataExcitation()
  {
    return dataExcitation_;
  }

  public float getInhibitionWeight()
  {
    return inhibitionWeight_;
  }

  public int getMaxFlips()
  {
    return maxFlips_;
  }

  public int getMaxTries()
  {
    return maxTries_;
  }
    
} // class echoOption
