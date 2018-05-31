package com.ezhealthtrack.greendao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table CITY.
 */
public class City {

    private Long id;
    private String CITY_ID;
    private String CITY_NAME;
    private String STATE_ID;
    private String COUNTRY_ID;
    private String DATE_UPDATED;
    private String DATE_CREATED;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public City() {
    }

    public City(Long id) {
        this.id = id;
    }

    public City(Long id, String CITY_ID, String CITY_NAME, String STATE_ID, String COUNTRY_ID, String DATE_UPDATED, String DATE_CREATED) {
        this.id = id;
        this.CITY_ID = CITY_ID;
        this.CITY_NAME = CITY_NAME;
        this.STATE_ID = STATE_ID;
        this.COUNTRY_ID = COUNTRY_ID;
        this.DATE_UPDATED = DATE_UPDATED;
        this.DATE_CREATED = DATE_CREATED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCITY_ID() {
        return CITY_ID;
    }

    public void setCITY_ID(String CITY_ID) {
        this.CITY_ID = CITY_ID;
    }

    public String getCITY_NAME() {
        return CITY_NAME;
    }

    public void setCITY_NAME(String CITY_NAME) {
        this.CITY_NAME = CITY_NAME;
    }

    public String getSTATE_ID() {
        return STATE_ID;
    }

    public void setSTATE_ID(String STATE_ID) {
        this.STATE_ID = STATE_ID;
    }

    public String getCOUNTRY_ID() {
        return COUNTRY_ID;
    }

    public void setCOUNTRY_ID(String COUNTRY_ID) {
        this.COUNTRY_ID = COUNTRY_ID;
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
		return CITY_NAME;
	}

	public City(Long id, String CITY_ID, String CITY_NAME, String STATE_ID,
			String COUNTRY_ID) {
		this.id = id;
		this.CITY_ID = CITY_ID;
		this.CITY_NAME = CITY_NAME;
		this.STATE_ID = STATE_ID;
		this.COUNTRY_ID = COUNTRY_ID;
	}
    // KEEP METHODS END

}