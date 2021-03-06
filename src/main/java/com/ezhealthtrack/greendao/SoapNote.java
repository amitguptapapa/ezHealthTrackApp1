package com.ezhealthtrack.greendao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table SOAP_NOTE.
 */
public class SoapNote {

    private Long id;
    private String bk_id;
    private java.util.Date date;
    private String note;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public SoapNote() {
    }

    public SoapNote(Long id) {
        this.id = id;
    }

    public SoapNote(Long id, String bk_id, java.util.Date date, String note) {
        this.id = id;
        this.bk_id = bk_id;
        this.date = date;
        this.note = note;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBk_id() {
        return bk_id;
    }

    public void setBk_id(String bk_id) {
        this.bk_id = bk_id;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
