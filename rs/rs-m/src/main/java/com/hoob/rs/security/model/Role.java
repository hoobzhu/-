package com.hoob.rs.security.model;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.hoob.rs.comm.model.BaseEntity;
import com.hoob.rs.comm.model.BaseEntity;

/**
 * @author mayjors
 * 2017年8月29日
 */
@Entity
@Table(name="role")
public class Role extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String name;
	private String description;
	private String creator; //创建者,登录用户的用户名

	private Set<Privilege> privileges;

	@Column(unique = true, nullable = false,columnDefinition="varchar(50) comment '角色名称'")
	public String getName() {
		return name;
	}

	public void setName(final String nameToSet) {
		name = nameToSet;
	}

	
	@ManyToMany( /* cascade = { CascadeType.REMOVE }, */fetch = FetchType.EAGER)
	@JoinTable(name = "role_privilege",joinColumns = { @JoinColumn(name = "role_id",
	referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "priv_id", referencedColumnName = "id") })
	public Set<Privilege> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(final Set<Privilege> privilegesToSet) {
		privileges = privilegesToSet;
	}

	@Column(columnDefinition="varchar(1000) comment '角色描述'")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(columnDefinition="varchar(50) comment '创建用户ID'")
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
            return true;
        }
		if (o == null || getClass() != o.getClass()) {
            return false;
        }
		Role role = (Role) o;
		return Objects.equals(name, role.name) &&
				Objects.equals(description, role.description) &&
				Objects.equals(creator, role.creator) &&
				Objects.equals(privileges, role.privileges);
	}

	@Override
	public int hashCode() {

		return Objects.hash(name, description, creator, privileges);
	}
}
