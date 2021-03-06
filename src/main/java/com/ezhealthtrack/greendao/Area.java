package com.ezhealthtrack.greendao;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table AREA.
 */
public class Area {

    private Long id;
    private String AREA_ID;
    private String AREA_NAME;
    private String CITY_ID;
    private String STATE_ID;
    private String COUNTRY_ID;
    private String DATE_UPDATED;
    private String DATE_CREATED;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Area() {
    }

    public Area(Long id) {
        this.id = id;
    }

    public Area(Long id, String AREA_ID, String AREA_NAME, String CITY_ID, String STATE_ID, String COUNTRY_ID, String DATE_UPDATED, String DATE_CREATED) {
        this.id = id;
        this.AREA_ID = AREA_ID;
        this.AREA_NAME = AREA_NAME;
        this.CITY_ID = CITY_ID;
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

    public String getAREA_ID() {
        return AREA_ID;
    }

    public void setAREA_ID(String AREA_ID) {
        this.AREA_ID = AREA_ID;
    }

    public String getAREA_NAME() {
        return AREA_NAME;
    }

    public void setAREA_NAME(String AREA_NAME) {
        this.AREA_NAME = AREA_NAME;
    }

    public String getCITY_ID() {
        return CITY_ID;
    }

    public void setCITY_ID(String CITY_ID) {
        this.CITY_ID = CITY_ID;
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
		return AREA_NAME;
	};

	public Area(Long id, String AREA_ID, String AREA_NAME, String CITY_ID,
			String STATE_ID, String COUNTRY_ID) {
		this.id = id;
		this.AREA_ID = AREA_ID;
		this.AREA_NAME = AREA_NAME;
		this.CITY_ID = CITY_ID;
		this.STATE_ID = STATE_ID;
		this.COUNTRY_ID = COUNTRY_ID;

	}
    // KEEP METHODS END

}
