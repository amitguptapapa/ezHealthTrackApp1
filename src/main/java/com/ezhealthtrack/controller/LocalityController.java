package com.ezhealthtrack.controller;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.greendao.Area;
import com.ezhealthtrack.greendao.AreaDao.Properties;
import com.ezhealthtrack.greendao.City;
import com.ezhealthtrack.greendao.Country;
import com.ezhealthtrack.greendao.State;

public class LocalityController {

	public interface GetData {
		public void onDataReceivedListner(Object obj);
	};

	public static List<Area> getAreaList(String s, City c) {
		try {
			return EzApp.areaDao
					.queryBuilder()
					.where(Properties.AREA_NAME.like("%" + s + "%"),
							Properties.CITY_ID.eq(c.getCITY_ID())).limit(30)
					.list();
		} catch (Exception e) {
			return new ArrayList<Area>();
		}

	}

	public static List<City> getCityList(final String s, final State st) {
		try {
			return EzApp.cityDao
					.queryBuilder()
					.where(com.ezhealthtrack.greendao.CityDao.Properties.CITY_NAME
							.like("%" + s + "%"),
							com.ezhealthtrack.greendao.CityDao.Properties.STATE_ID
									.eq(st.getSTATE_ID())).limit(30).list();
		} catch (Exception e) {
			return new ArrayList<City>();
		}
	}

	public static List<State> getStateList(String s, Country c) {
		try {
			return EzApp.stateDao
					.queryBuilder()
					.where(com.ezhealthtrack.greendao.StateDao.Properties.STATE_NAME
							.like("%" + s + "%"),
							com.ezhealthtrack.greendao.StateDao.Properties.COUNTRY_ID
									.eq(c.getCOUNTRY_ID())).limit(30).list();
		} catch (Exception e) {
			return new ArrayList<State>();
		}
	}

	public static List<Country> getCountryList(String s) {
		Log.i(s,
				""
						+ EzApp.countryDao
								.queryBuilder()
								.where(com.ezhealthtrack.greendao.CountryDao.Properties.COUNTRY_NAME
										.like("%" + s + "%")).count());

		return EzApp.countryDao
				.queryBuilder()
				.where(com.ezhealthtrack.greendao.CountryDao.Properties.COUNTRY_NAME
						.like("%" + s + "%")).limit(30).list();
	}

}
