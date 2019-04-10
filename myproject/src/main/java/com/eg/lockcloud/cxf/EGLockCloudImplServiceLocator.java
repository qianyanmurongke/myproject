/**
 * EGLockCloudImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.eg.lockcloud.cxf;

public class EGLockCloudImplServiceLocator extends org.apache.axis.client.Service implements com.eg.lockcloud.cxf.EGLockCloudImplService {

    public EGLockCloudImplServiceLocator() {
    }


    public EGLockCloudImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public EGLockCloudImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for EGLockCloudImplPort
    private java.lang.String EGLockCloudImplPort_address = "http://10.101.38.183:8080/eglc/ws/EGLockCloud";

    public java.lang.String getEGLockCloudImplPortAddress() {
        return EGLockCloudImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String EGLockCloudImplPortWSDDServiceName = "EGLockCloudImplPort";

    public java.lang.String getEGLockCloudImplPortWSDDServiceName() {
        return EGLockCloudImplPortWSDDServiceName;
    }

    public void setEGLockCloudImplPortWSDDServiceName(java.lang.String name) {
        EGLockCloudImplPortWSDDServiceName = name;
    }

    public com.eg.lockcloud.cxf.IEGLockCloud getEGLockCloudImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(EGLockCloudImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getEGLockCloudImplPort(endpoint);
    }

    public com.eg.lockcloud.cxf.IEGLockCloud getEGLockCloudImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.eg.lockcloud.cxf.EGLockCloudImplServiceSoapBindingStub _stub = new com.eg.lockcloud.cxf.EGLockCloudImplServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getEGLockCloudImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setEGLockCloudImplPortEndpointAddress(java.lang.String address) {
        EGLockCloudImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.eg.lockcloud.cxf.IEGLockCloud.class.isAssignableFrom(serviceEndpointInterface)) {
                com.eg.lockcloud.cxf.EGLockCloudImplServiceSoapBindingStub _stub = new com.eg.lockcloud.cxf.EGLockCloudImplServiceSoapBindingStub(new java.net.URL(EGLockCloudImplPort_address), this);
                _stub.setPortName(getEGLockCloudImplPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("EGLockCloudImplPort".equals(inputPortName)) {
            return getEGLockCloudImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://cxf.lockcloud.eg.com/", "EGLockCloudImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://cxf.lockcloud.eg.com/", "EGLockCloudImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("EGLockCloudImplPort".equals(portName)) {
            setEGLockCloudImplPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
