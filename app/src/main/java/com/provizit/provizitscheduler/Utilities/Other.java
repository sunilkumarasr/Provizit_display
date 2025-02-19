package com.provizit.provizitscheduler.Utilities;

import java.io.Serializable;

public class Other implements Serializable {



    private String label;
    private String data = "Not Mentioned";
    private String model;
    private Boolean status=false;
    private Boolean active=false;
    private Boolean unique=false;
    private Boolean hidden=false;
    private Boolean depends=false;
    private Boolean delete=false;

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getDepends() {
        return depends;
    }

    public void setDepends(Boolean depends) {
        this.depends = depends;
    }

    public Boolean getDelete() {
        return delete;
    }

    public void setDelete(Boolean delete) {
        this.delete = delete;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

}
