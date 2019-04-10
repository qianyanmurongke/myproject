package com.course.core.domain;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Objects;

/**
 * Org
 * 
 * @author benfang
 * 
 */
@Entity
@Table(name = "cms_org")
public class Org implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 树编码长度
	 */
	public static int TREE_NUMBER_LENGTH = 4;

	@Transient
	public static String long2hex(long num) {
		BigInteger big = BigInteger.valueOf(num);
		String hex = big.toString(Character.MAX_RADIX);
		return StringUtils.leftPad(hex, TREE_NUMBER_LENGTH, '0');
	}

	@Transient
	public static long hex2long(String hex) {
		BigInteger big = new BigInteger(hex, Character.MAX_RADIX);
		return big.longValue();
	}

	@Transient
	public String getDisplayName() {
		StringBuilder sb = new StringBuilder();
		Org org = this;
		sb.append(org.getName());
		org = org.getParent();
		while (org != null) {
			sb.insert(0, " - ");
			sb.insert(0, org.getName());
			org = org.getParent();
		}
		return sb.toString();
	}

	@Transient
	public long getTreeMaxLong() {
		BigInteger big = new BigInteger(getTreeMax(), Character.MAX_RADIX);
		return big.longValue();
	}

	@Transient
	public void addChild(Org bean) {
		List<Org> list = getChildren();
		if (list == null) {
			list = new ArrayList<Org>();
			setChildren(list);
		}
		list.add(bean);
	}

	@Transient
	public void applyDefaultValue() {
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Org)) {
			return false;
		}
		Org that = (Org) o;
		return Objects.equal(id, that.id);
	}		
	
	
	private Set<UserOrg> userOrgs = new HashSet<UserOrg>(0);
	
	private Set<User> users = new HashSet<User>(0);
	
	private List<Org> children = new ArrayList<Org>(0);
	


	/**
	 * id
	 */
	private Integer id;
	/**
	 * 上级组织
	 */
	private Org parent;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 全称
	 */
	private String fullName;
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 校长
	 */
	private String principal;
	/**
	 * 联系人
	 */
	private String contacts;
	/**
	 * 联系电话
	 */
	private String phone;
	/**
	 * 传真
	 */
	private String fax;

	/**
	 * 编码
	 */
	private String number;
	/**
	 * 树编码
	 */
	private String treeNumber;
	/**
	 * 树级别
	 */
	private Integer treeLevel;
	/**
	 * 树子节点最大编码
	 */
	private String treeMax;

	/**
	 * 英文名称
	 */
	private String ename;

	/**
	 * 电子邮箱
	 */
	private String email;

	/**
	 * 邮编
	 */
	private Integer zipcode;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 省
	 */
	private String province;

	/**
	 * 市
	 */
	private String city;

	/**
	 * 区
	 */
	private String district;

	/**
	 * 建校年月
	 */
	private Date foundingDate;

	/**
	 * 校庆日
	 */
	private Date decorationDay;

	/**
	 * 法定代表人
	 */
	private String legalRepresentative;

	/**
	 * 法人身份证号
	 */
	private String legalIdcard;

	/**
	 * 法人登记号
	 */
	private String legalNumber;

	/**
	 * 组织机构号码
	 */
	private String organizationNumber;

	/**
	 * 主页
	 */
	private String webAddress;

	/**
	 * Logo地址
	 */
	private String logoUrl;
	
	/**
	 * 微信公众号Id
	 */
	private String publicNumber;
	
	/**
	 * 公众号密码
	 * 
	 */
	private String appSecret;

	@Id
	@Column(name = "f_org_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_org", pkColumnValue = "cms_org", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_org")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "org")
	public Set<UserOrg> getUserOrgs() {
		return userOrgs;
	}

	public void setUserOrgs(Set<UserOrg> userOrgs) {
		this.userOrgs = userOrgs;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "org")
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}		

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	@OrderBy(value = "treeNumber asc, id asc")
	public List<Org> getChildren() {
		return this.children;
	}

	public void setChildren(List<Org> children) {
		this.children = children;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_parent_id")
	public Org getParent() {
		return this.parent;
	}

	public void setParent(Org parent) {
		this.parent = parent;
	}

	@Column(name = "f_name", nullable = false, length = 150)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "f_full_name", length = 150)
	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Column(name = "f_description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "f_principal")
	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	@Column(name = "f_contacts", length = 100)
	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	@Column(name = "f_phone", length = 100)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "f_fax", length = 100)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "f_address")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "f_number", length = 100)
	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Column(name = "f_tree_number", nullable = false, length = 100)
	public String getTreeNumber() {
		return this.treeNumber;
	}

	public void setTreeNumber(String treeNumber) {
		this.treeNumber = treeNumber;
	}

	@Column(name = "f_tree_level", nullable = false)
	public Integer getTreeLevel() {
		return this.treeLevel;
	}

	public void setTreeLevel(Integer treeLevel) {
		this.treeLevel = treeLevel;
	}

	@Column(name = "f_tree_max", nullable = false, length = 10)
	public String getTreeMax() {
		return this.treeMax;
	}

	public void setTreeMax(String treeMax) {
		this.treeMax = treeMax;
	}

	@Column(name = "f_ename", nullable = false)
	public String getEname() {
		return ename;
	}

	public void setEname(String ename) {
		this.ename = ename;
	}

	@Column(name = "f_email", nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "f_zip_code", nullable = false)
	public Integer getZipcode() {
		return zipcode;
	}

	public void setZipcode(Integer zipcode) {
		this.zipcode = zipcode;
	}

	@Column(name = "f_province", nullable = false)
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "f_city", nullable = false)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "f_district", nullable = false)
	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	@Column(name = "f_founding_date", nullable = false)
	public Date getFoundingDate() {
		return foundingDate;
	}

	public void setFoundingDate(Date foundingDate) {
		this.foundingDate = foundingDate;
	}

	@Column(name = "f_decoration_day", nullable = false)
	public Date getDecorationDay() {
		return decorationDay;
	}

	public void setDecorationDay(Date decorationDay) {
		this.decorationDay = decorationDay;
	}

	@Column(name = "f_legal_representative", nullable = false)
	public String getLegalRepresentative() {
		return legalRepresentative;
	}

	public void setLegalRepresentative(String legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
	}

	@Column(name = "f_legal_idcard", nullable = false)
	public String getLegalIdcard() {
		return legalIdcard;
	}

	public void setLegalIdcard(String legalIdcard) {
		this.legalIdcard = legalIdcard;
	}

	@Column(name = "f_legal_number", nullable = false)
	public String getLegalNumber() {
		return legalNumber;
	}

	public void setLegalNumber(String legalNumber) {
		this.legalNumber = legalNumber;
	}

	@Column(name = "f_organization_number", nullable = false)
	public String getOrganizationNumber() {
		return organizationNumber;
	}

	public void setOrganizationNumber(String organizationNumber) {
		this.organizationNumber = organizationNumber;
	}


	@Column(name = "f_web_address")
	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	@Column(name = "f_logo_url")
	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	@Column(name = "f_public_number")
	public String getPublicNumber() {
		return publicNumber;
	}

	public void setPublicNumber(String publicNumber) {
		this.publicNumber = publicNumber;
	}

	@Column(name = "f_app_secret")
	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	
	
}
