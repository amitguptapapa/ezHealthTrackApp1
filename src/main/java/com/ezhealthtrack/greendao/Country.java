package com.ezhealthtrack.greendao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table COUNTRY.
 */
public class Country {

    private Long id;
    private String COUNTRY_ID;
    private String COUNTRY_NAME;
    private String COUNTRY_CODE;
    private String DATE_UPDATED;
    private String DATE_CREATED;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Country() {
    }

    public Country(Long id) {
        this.id = id;
    }

    public Country(Long id, String COUNTRY_ID, String COUNTRY_NAME, String COUNTRY_CODE, String DATE_UPDATED, String DATE_CREATED) {
        this.id = id;
        this.COUNTRY_ID = COUNTRY_ID;
        this.COUNTRY_NAME = COUNTRY_NAME;
        this.COUNTRY_CODE = COUNTRY_CODE;
        this.DATE_UPDATED = DATE_UPDATED;
        this.DATE_CREATED = DATE_CREATED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCOUNTRY_ID() {
        return COUNTRY_ID;
    }

    public void setCOUNTRY_ID(String COUNTRY_ID) {
        this.COUNTRY_ID = COUNTRY_ID;
    }

    public String getCOUNTRY_NAME() {
        return COUNTRY_NAME;
    }

    public void setCOUNTRY_NAME(String COUNTRY_NAME) {
        this.COUNTRY_NAME = COUNTRY_NAME;
    }

    public String getCOUNTRY_CODE() {
        return COUNTRY_CODE;
    }

    public void setCOUNTRY_CODE(String COUNTRY_CODE) {
        this.COUNTRY_CODE = COUNTRY_CODE;
    }

    public String getDATE_UPDATED() {
        return DATE_UPDATED;
    }

    public void setDATE_UPDATED(String DATE_UPDATED) {
        this.DATE_UPDATED = DATE_UPDATED;
    }

    public String getDATE_CREATED() {
        return DATE_CREATED;
    }

    public void setDATE_CREATED(String DATE_CREATED) {
        this.DATE_CREATED = DATE_CREATED;
    }

    // KEEP METHODS - put your custom methods here
    @Override
	public String toString() {
		// TODO Auto-generated method stub
		return COUNTRY_NAME;
	}
    
    public Country(Long id, String COUNTRY_ID, String COUNTRY_NAME, String COUNTRY_CODE) {
        this.id = id;
        this.COUNTRY_ID = COUNTRY_ID;
        this.COUNTRY_NAME = COUNTRY_NAME;
        this.COUNTRY_CODE = COUNTRY_CODE;
    }
    // KEEP METHODS END

}
