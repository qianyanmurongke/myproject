package com.eg.lockcloud.cxf;

public class IEGLockCloudProxy implements com.eg.lockcloud.cxf.IEGLockCloud {
  private String _endpoint = null;
  private com.eg.lockcloud.cxf.IEGLockCloud iEGLockCloud = null;
  
  public IEGLockCloudProxy() {
    _initIEGLockCloudProxy();
  }
  
  public IEGLockCloudProxy(String endpoint) {
    _endpoint = endpoint;
    _initIEGLockCloudProxy();
  }
  
  private void _initIEGLockCloudProxy() {
    try {
      iEGLockCloud = (new com.eg.lockcloud.cxf.EGLockCloudImplServiceLocator()).getEGLockCloudImplPort();
      if (iEGLockCloud != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iEGLockCloud)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iEGLockCloud)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iEGLockCloud != null)
      ((javax.xml.rpc.Stub)iEGLockCloud)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.eg.lockcloud.cxf.IEGLockCloud getIEGLockCloud() {
    if (iEGLockCloud == null)
      _initIEGLockCloudProxy();
    return iEGLockCloud;
  }
  
  public java.lang.String transSecXml(java.lang.String xml) throws java.rmi.RemoteException{
    if (iEGLockCloud == null)
      _initIEGLockCloudProxy();
    return iEGLockCloud.transSecXml(xml);
  }
  
  public java.lang.String transXml(java.lang.String xml) throws java.rmi.RemoteException{
    if (iEGLockCloud == null)
      _initIEGLockCloudProxy();
    return iEGLockCloud.transXml(xml);
  }
  
  public java.lang.String newTransSecXml(java.lang.String user, java.lang.String xml) throws java.rmi.RemoteException{
    if (iEGLockCloud == null)
      _initIEGLockCloudProxy();
    return iEGLockCloud.newTransSecXml(user, xml);
  }
  
  public java.lang.String getKey(java.lang.String user, java.lang.String pass) throws java.rmi.RemoteException{
    if (iEGLockCloud == null)
      _initIEGLockCloudProxy();
    return iEGLockCloud.getKey(user, pass);
  }
  
  
}