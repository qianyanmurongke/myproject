/**
 * IEGLockCloud.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.eg.lockcloud.cxf;

public interface IEGLockCloud extends java.rmi.Remote {
    public java.lang.String transSecXml(java.lang.String xml) throws java.rmi.RemoteException;
    public java.lang.String transXml(java.lang.String xml) throws java.rmi.RemoteException;
    public java.lang.String newTransSecXml(java.lang.String user, java.lang.String xml) throws java.rmi.RemoteException;
    public java.lang.String getKey(java.lang.String user, java.lang.String pass) throws java.rmi.RemoteException;
}
