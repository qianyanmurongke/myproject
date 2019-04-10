package com.course.common.security;

/**
 * SHA1证书加密
 * 
 * @author benfang
 * 
 */
public class SHA1CredentialsDigest extends HashCredentialsDigest {
	@Override
	protected byte[] digest(byte[] input, byte[] salt) {
		return Digests.sha1(input, salt, HASH_INTERATIONS);
	}
}
