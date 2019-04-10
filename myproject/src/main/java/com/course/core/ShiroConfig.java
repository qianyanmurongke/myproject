package com.course.core;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.course.common.security.SHA1CredentialsDigest;
import com.course.common.util.PropertiesHelper;
import com.course.common.util.PropertiesLoader;
import com.course.common.web.JspDispatcherFilter;
import com.course.core.security.CmsAuthenticationFilter;
import com.course.core.security.CmsLogoutFilter;
import com.course.core.security.CmsUserFilter;
import com.course.core.security.ShiroDbRealm;
import com.course.core.support.BackSiteFilter;
import com.course.core.support.ClassCardJsonFilter;
import com.course.core.support.CommonJsonFilter;
import com.course.core.support.JsonFilter;

@Configuration
public class ShiroConfig {
	@Primary
	@Bean("properties")
	public Properties properties() throws IOException {
		PropertiesLoader loader = new PropertiesLoader();
		loader.setFileEncoding("UTF-8");
		loader.setValue("classpath:conf/conf.properties");
		Properties properties = loader.createProperties();
		return properties;
	}

	@Bean("propertiesHelper")
	public PropertiesHelper propertiesHelper() throws IOException {
		PropertiesHelper propertiesHelper = new PropertiesHelper();
		propertiesHelper.setProperties(properties());
		return propertiesHelper;
	}

	@Bean("credentialsDigest")
	public SHA1CredentialsDigest credentialsDigest() {
		return new SHA1CredentialsDigest();
	}

	@Bean("lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean("shiroRealm")
	public Realm shiroRealm() {
		ShiroDbRealm realm = new ShiroDbRealm();
		realm.setAuthorizationCachingEnabled(false);
		return realm;
	}

	@Bean("shiroEhcacheManager")
	public EhCacheManager shiroEhCacheManager() throws IOException {
		EhCacheManager ehCacheManager = new EhCacheManager();
		String cacheManagerConfigFile = properties().getProperty("shiroCacheManagerConfigFile");
		ehCacheManager.setCacheManagerConfigFile(cacheManagerConfigFile);
		// ehCacheManager.setCacheManagerConfigFile("classpath:ehcache/shiro-ehcache.xml");
		return ehCacheManager;
	}

	@Bean("securityManager")
	public DefaultWebSecurityManager securityManager() throws IOException {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(shiroRealm());
		securityManager.setCacheManager(shiroEhCacheManager());
		return securityManager;
	}

	@Bean("shiroFilter")
	@DependsOn("propertiesHelper")
	public ShiroFilterFactoryBean shiroFilterFactoryBean(BeanFactory beanFactory) throws IOException {
		ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
		factoryBean.setSecurityManager(securityManager());
		factoryBean.setLoginUrl("/login");
		Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
		filters.put("backSite", new BackSiteFilter(beanFactory));
		filters.put("authc", new CmsAuthenticationFilter(beanFactory));
		filters.put("user", new CmsUserFilter());
		filters.put("logout", new CmsLogoutFilter(beanFactory));
		factoryBean.setFilters(filters);
		Map<String, String> filterChainDefinitionMap = propertiesHelper()
				.getSortedMap("shiroFilterChainDefinitionMap.");
		// Map<String, String> filterChainDefinitionMap = new
		// LinkedHashMap<String, String>();
		// filterChainDefinitionMap.put("/*", "anon");
		// filterChainDefinitionMap.put("*", "anon");
		// filterChainDefinitionMap.put("*.jsp", "anon");
		// filterChainDefinitionMap.put("/login", "authc");
		// filterChainDefinitionMap.put("/logout", "logout");
		// filterChainDefinitionMap.put("/my", "user");
		// filterChainDefinitionMap.put("/my/**", "user");
		// filterChainDefinitionMap.put("/admin/", "backSite,anon");
		// filterChainDefinitionMap.put("/admin/index.do", "backSite,anon");
		// filterChainDefinitionMap.put("/admin/login.do", "backSite,authc");
		// filterChainDefinitionMap.put("/admin/logout.do", "backSite,logout");
		// filterChainDefinitionMap.put("/admin/**", "backSite,user");
		factoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return factoryBean;
	}

	// @Bean
	// public FilterRegistrationBean timerFilterRegistrationBean() {
	// FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
	// filterRegistration.setFilter(new TimerFilter());
	// filterRegistration.setEnabled(true);
	// filterRegistration.addUrlPatterns("/*");
	// filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
	// return filterRegistration;
	// }

	@Bean
	public FilterRegistrationBean jspDispatcherFilterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new JspDispatcherFilter());
		filterRegistration.setEnabled(true);
		filterRegistration.addInitParameter("prefix", "/jsp");
		filterRegistration.addUrlPatterns("*.jsp");
		filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
		return filterRegistration;
	}

	@Bean
	public FilterRegistrationBean openEntityManagerInViewFilterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new OpenEntityManagerInViewFilter());
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/*");
		return filterRegistration;
	}

	@Bean
	public FilterRegistrationBean shiroFilterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
		filterRegistration.setEnabled(true);
		filterRegistration.addInitParameter("targetFilterLifecycle", "true");
		filterRegistration.addUrlPatterns("/*");
		return filterRegistration;
	}

	@Bean
	public FilterRegistrationBean classCardJsonFilterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new ClassCardJsonFilter());
		filterRegistration.setEnabled(true);
		filterRegistration.addUrlPatterns("/site-classcard/api/*");
		return filterRegistration;
		
	}
	
	@Bean
	public FilterRegistrationBean jsonFilterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new JsonFilter());
		filterRegistration.setEnabled(true);		
		filterRegistration.addUrlPatterns("/api/*");
		
		return filterRegistration;
		
	}
	

	
	@Bean
	public FilterRegistrationBean commonJsonFilterRegistrationBean() {
		FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
		filterRegistration.setFilter(new CommonJsonFilter());
		filterRegistration.setEnabled(true);		
		filterRegistration.addUrlPatterns("/common-api/*");
		
		return filterRegistration;
		
	}
}
