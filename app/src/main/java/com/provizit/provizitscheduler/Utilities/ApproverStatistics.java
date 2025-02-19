package com.provizit.provizitscheduler.Utilities;

import java.io.Serializable;

public class ApproverStatistics implements Serializable {
    private String roleid;
    private String empId;
    private Long status;

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
}

