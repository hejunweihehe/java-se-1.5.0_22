/*
 * @(#)file      JDMEnterprise.java
 * @(#)author    Sun Microsystems, Inc.
 * @(#)version   4.7
 * @(#)date      09/10/09
 *
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */


/* Generated By:JJTree: Do not edit this line. JDMEnterprise.java */

package com.sun.jmx.snmp.IPAcl;

/** 
 * @version     4.7     12/19/03 
 * @author      Sun Microsystems, Inc. 
 */ 
class JDMEnterprise extends SimpleNode {
  protected String enterprise= "";

  JDMEnterprise(int id) {
    super(id);
  }

  JDMEnterprise(Parser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(int id) {
      return new JDMEnterprise(id);
  }

  public static Node jjtCreate(Parser p, int id) {
      return new JDMEnterprise(p, id);
  }
}
