package com.course.core.security;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.course.common.security.CredentialsDigest;
import com.course.common.security.PlainCredentialsDigest;
import com.course.common.security.UnactivatedAccountException;
import com.course.core.domain.Site;
import com.course.core.domain.User;
import com.course.core.service.UserShiroService;
import com.course.core.support.Context;

/**
 * ShiroDbRealm
 * 
 * @author benfang
 * 
 */
public class ShiroDbRealm extends AuthorizingRealm implements InitializingBean {
	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user = userShiroService.findByUsername(token.getUsername());
		// 前后台登录共用，非管理员也可登录。
		if (user != null) {
			if (user.isNormal()) {
				// byte[] salt = user.getSaltBytes();
				// return new SimpleAuthenticationInfo(new
				// ShiroUser(user.getId(),
				// user.getUsername()), user.getPassword(),
				// ByteSource.Util.bytes(salt), getName());

				PlainCredentialsDigest plaincredentialsdigest = new PlainCredentialsDigest();
				String encPass = user.getPassword() == null ? "" : user.getPassword();
				if (!plaincredentialsdigest.isPasswordValid(encPass, String.valueOf(token.getPassword()),
						user.getSalt())) {
					throw new IncorrectCredentialsException();
				}
				return new SimpleAuthenticationInfo(new ShiroUser(user.getId(), user.getUsername()), encPass,
						getName());
			} else if (user.isLocked()) {
				throw new LockedAccountException();
			} else if (user.isUnactivated()) {
				throw new UnactivatedAccountException();
			}
		}
		return null;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo auth = new SimpleAuthorizationInfo();
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		User user = userShiroService.get(shiroUser.id);
		Site site = Context.getCurrentSite();
		if (site != null && user != null && user.isNormal()) {
			Set<String> perms = user.getPerms(site.getId());
			if (!CollectionUtils.isEmpty(perms)) {
				auth.setStringPermissions(perms);
			}
			if (user.isSuper()) {
				auth.addRole("super");
			}
		}
		return auth;
	}

	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	public void afterPropertiesSet() throws Exception {
		// CredentialsMatcher matcher = new
		// CredentialsMatcherAdapter(credentialsDigest);
		// setCredentialsMatcher(matcher);
	}

	protected UserShiroService userShiroService;
	private CredentialsDigest credentialsDigest;

	@Autowired
	public void setUserShiroService(UserShiroService userShiroService) {
		this.userShiroService = userShiroService;
	}

	@Autowired
	public void setCredentialsDigest(CredentialsDigest credentialsDigest) {
		this.credentialsDigest = credentialsDigest;
	}

}
