package com.course.common.security;

/**
 * 证书加密
 * 
 * @author benfang
 * 
 */
public interface CredentialsDigest {
	public String digest(String plainCredentials, byte[] salt);

	public boolean matches(String credentials, String plainCredentials,
			byte[] salt);
}
