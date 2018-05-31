package com.ezhealthtrack.model.laborder;

public class LabInfo {

	private String id;
	private String name;
	private String type;
	private String branch_id;
	private String tenant_id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTenantId() {
		return tenant_id;
	}

	public void setTenantId(String tenantId) {
		this.tenant_id = tenantId;
	}

	public String getBranchId() {
		return branch_id;
	}

	public void setBranchId(String branchId) {
		this.branch_id = branchId;
	}
}
