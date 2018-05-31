package com.ezhealthtrack.greendao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table RADIOLOGY.
 */
public class Radiology {

    private Long id;
    private String category_name;
    private String category_id;
    private String type;
    private String test_name;
    private String codid;
    private String codcat;
    private String gid;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Radiology() {
    }

    public Radiology(Long id) {
        this.id = id;
    }

    public Radiology(Long id, String category_name, String category_id, String type, String test_name, String codid, String codcat, String gid) {
        this.id = id;
        this.category_name = category_name;
        this.category_id = category_id;
        this.type = type;
        this.test_name = test_name;
        this.codid = codid;
        this.codcat = codcat;
        this.gid = gid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getCodid() {
        return codid;
    }

    public void setCodid(String codid) {
        this.codid = codid;
    }

    public String getCodcat() {
        return codcat;
    }

    public void setCodcat(String codcat) {
        this.codcat = codcat;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
