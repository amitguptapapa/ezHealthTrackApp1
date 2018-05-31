package com.ezhealthrack.api;

import com.ezhealthtrack.EzApp;
import com.ezhealthtrack.model.BranchInfo;
import com.ezhealthtrack.util.Constants;

public class APIs {

    // Below code is used for business server i.e. for testing purpose
    // Comment below code while uploading apk to production i.e. on Google Play
    // start {

    public static final String[] ezServers = {"Business", "Production", "uiv2"};

    static private String tenantUrl() {
        String url = "";
        String id = EzApp.sharedPref.getString(Constants.TENANT_ID, "");
        if (id != null && id.length() > 1) {
            url = "/tenant_id/" + id;
        }
        return url;
    }

    static public String URL() {
        int serverIndex = EzApp.sharedPref.getInt(Constants.EZ_SERVER_ID, 0);
        String url = "https://business.ezhealthtrack.com";
        switch (serverIndex) {
            case 0:
                url = "https://business.ezhealthtrack.com";
                break;
            case 1:
                url = "https://www.ezhealthtrack.com";
                break;
            case 2:
                url = "http://uiv2.ezhealthtrack.com";
                break;
        }
        // Log.v("URL()", "Index:" + serverIndex + ", URL:" + url);
        return url;
    }

    // end }

    // and

    // Below code is used for production server i.e. for Google Play
    // Uncomment below code while uploading apk to production i.e. on Google
    // Play
    // start {

//	public static final String[] ezServers = { "Production", "Business", "uiv2" };
//
//	static private String tenantUrl() {
//		String url = "";
//		String id = EzApp.sharedPref.getString(Constants.TENANT_ID, "");
//		if (id != null && id.length() > 1) {
//			url = "/tenant_id/" + id;
//		}
//		return url;
//	}
//
//	static public String URL() {
//		int serverIndex = EzApp.sharedPref.getInt(Constants.EZ_SERVER_ID, 0);
//		String url = "https://www.ezhealthtrack.com";
//		switch (serverIndex) {
//		case 0:
//			url = "https://www.ezhealthtrack.com";
//			break;
//		case 1:
//			url = "https://business.ezhealthtrack.com";
//			break;
//		case 2:
//			url = "http://uiv2.ezhealthtrack.com";
//			break;
//		}
//		// Log.v("URL()", "Index:" + serverIndex + ", URL:" + url);
//		return url;
//	}

    // end }

    static public String contextUrl() {
        return APIs.tenantUrl() + BranchInfo.branchUrl();
    }

    public static String ROOT() {
        return URL() + "/index.php";
    }

    // public static final String LOGIN = APIs.ROOT() + "/v1/account/login";
    public static String LOGIN() {
        return APIs.ROOT() + "/v1/account/login";
    }

    public static String FORGOT_PASSWORD() {
        return APIs.ROOT() + "/account/resetpassword";
    }

    public static String LOGOUT() {
        return APIs.ROOT() + "/v1/account/logout";
    }

    public static String PATIENT_LIST() {
        return APIs.URL() + "/v1/patients/list" + APIs.contextUrl();
    }

    public static String COUNTRY_LIST() {
        return APIs.ROOT() + "/v1/country/list";
    }

    public static String STATE_LIST() {
        return APIs.ROOT() + "/v1/state/list";
    }

    public static String CITY_LIST() {
        return APIs.ROOT() + "/v1/city/list";
    }

    public static String LOCALITY_LIST() {
        return APIs.ROOT() + "/v1/area/list";
    }

    public static String UPDATE_PATIENT() {
        return APIs.URL() + "/v1/patients/update/pat_id/pid/fam_id/fid";
    }

    // public static final String GET_PATIENT = APIs.ROOT() +
    // "/v1/patients/view";
    public static String SCHEDULE_PLAN() {
        return APIs.ROOT() + "/v1/doctor/schedulePlan";
        // + APIs.contextUrl();
    }

    public static String ASSISTANT_SCHEDULE_PLAN() {
        return APIs.ROOT() + "v1/assistants/schedulePlan" + APIs.contextUrl();
    }

    public static String SCHEDULE_DATA() {
        return APIs.ROOT() + "/v1/appointment/schedules" + APIs.contextUrl();
    }

    public static String APPOINTMENT_REGISTER() {
        return APIs.URL() + "/v1/task/action/appointment/start/register";
    }

    public static String APPOINTMENT_RESCHEDULE() {
        return APIs.URL() + "/v1/task/action/appointment/bk-id/reschedule";
    }

    public static String APPOINTMENT_FOLLOWUP() {
        return APIs.URL() + "/v1/task/action/appointment/bk-id/followup";
    }

    public static String CONFIRMED_LIST() {
        return APIs.ROOT() + "/v1/booking/Confirmed/cli/api"
                + APIs.contextUrl();
    }

    public static String HISTORY_LIST() {
        return APIs.ROOT() + "/v1/booking/history" + APIs.contextUrl();
    }

    public static String LAB_ORDERS_LIST() {
        return APIs.URL() + "/v1/order/labDirectOrder" + APIs.contextUrl();
    }

    public static String LAB_ORDER_DETAILS() {
        return APIs.ROOT() + "/v1/order/orderView/id/{order_id}"
                + APIs.contextUrl();
    }

    public static String NEW_PATIENT_LIST() {
        return APIs.ROOT() + "/v1/booking/newtentative" + APIs.contextUrl();
    }

    public static String PATIENT_VIEW() {
        return APIs.ROOT() + "/v1/patients/view" + APIs.contextUrl();
    }

    public static String IMAGE() {
        return APIs.URL() + "/uploads/";
    }

    public static String WORKFLOW() {
        return APIs.ROOT() + "/v1/workflow/steps" + APIs.contextUrl();
    }

    public static String APPOINTMENTREJECT() {
        return APIs.ROOT() + "/v1/task/action/appointment/booking_id/cancel";
    }

    public static String APPOINTMENTCONFIRM() {
        return APIs.URL() + "/v1/task/action/appointment/booking_id/confirm";
    }

    public static String APPOINTMENTCHECKIN() {
        return APIs.ROOT() + "/v1/task/action/appointment/booking_id/checkin";
    }

    public static String LAB_RADIOLOGY_PREF() {
        return APIs.ROOT() + "/v1/radiologytest/show" + APIs.contextUrl();
    }

    public static String LAB_PREFERENCES() {
        return APIs.URL() + "/v1/preference/doctor/cli/api" + APIs.contextUrl();
    }

    public static String UPDATE_LAB_PREF() {
        return APIs.URL() + "/v1/preference/updateLabPreference"
                + APIs.contextUrl();
    }

    public static String POST_LAB_RADIOLOGY_PREF() {
        return APIs.URL() + "/v1/radiologytest/update" + APIs.contextUrl();
    }

    public static String POST_LAB_PREF() {
        return APIs.ROOT() + "/v1/preference/doctor/cli/api"
                + APIs.contextUrl();
    }

    public static String IN_REFFERRAL_LIST() {
        return APIs.URL() + "/ReferPatient/inReferral" + APIs.contextUrl();
    }

    public static String OUT_REFFERRAL_LIST() {
        return APIs.ROOT() + "/v1/ReferPatient/outReferral" + APIs.contextUrl();
    }

    public static String INBOX_MESSAGES() {
        return APIs.URL() + "/v1/doctor/inbox" + APIs.contextUrl();
    }

    public static String OUTBOX_MESSAGES() {
        return APIs.ROOT() + "/v1/notification/outbox" + APIs.contextUrl();
    }

    public static String SENDMESSAGE() {
        return APIs.ROOT() + "/v1/notification/sendMessage" + APIs.contextUrl();
    }

    public static String VIEWMESSAGE() {
        return APIs.ROOT() + "/v1/notification/viewMessage" + APIs.contextUrl();
    }

    public static String VIEWALERT() {
        return APIs.ROOT() + "/v1/notification/viewAlert" + APIs.contextUrl();
    }

    public static String REPLYMESSAGE() {
        return APIs.ROOT() + "/v1/notification/sendReply" + APIs.contextUrl();
    }

    public static String DASHBOARD() {
        return APIs.ROOT() + "/v1/site/doctorDashboard/cli/api"
                + APIs.contextUrl();
    }

    public static String ALERTS() {
        return APIs.ROOT() + "/v1/doctor/alerts" + APIs.contextUrl();
    }

    public static String HOSPITAL() {
        return APIs.ROOT() + "/v1/hospital/index" + APIs.contextUrl();
    }

    public static String HOSPITALSEARCH() {
        return APIs.ROOT() + "/v1/hospital/search" + APIs.contextUrl();
    }

    public static String ASSISTANTS() {
        return APIs.ROOT() + "/v1/assistants/index" + APIs.contextUrl();
    }

    public static String ASSISTANTPROFILE() {
        return APIs.ROOT() + "/v1/assistants/view" + APIs.contextUrl();
    }

    public static String UPDATEASSISTANT() {
        return APIs.ROOT() + "/v1/assistants/update" + APIs.contextUrl();
    }

    public static String ACCOUNTVIEW() {
        return APIs.ROOT() + "/v1/doctor/accountView" + APIs.contextUrl();
    }

    public static String UPDATEACCOUNT() {
        return APIs.ROOT() + "/v1/doctor/accountUpdate" + APIs.contextUrl();
    }

    public static String PROFILE() {
        return APIs.ROOT() + "/v1/Profile/doctor" + APIs.contextUrl();
    }

    public static String EDUCATION_ADD_DELETE() {
        return APIs.ROOT() + "/v1/Profile/processEducation" + APIs.contextUrl();
    }

    public static String CERTIFICATION_ADD_DELETE() {
        return APIs.ROOT() + "/v1/Profile/processCertifications"
                + APIs.contextUrl();
    }

    public static String UPDATE_PROFESSIONAL() {
        return APIs.ROOT() + "/v1/Profile/updateProfessional"
                + APIs.contextUrl();
    }

    public static String PUBLICATION_ADD_DELETE() {
        return APIs.ROOT() + "/v1/Profile/processPublications"
                + APIs.contextUrl();
    }

    public static String INSURANCE_ADD_DELETE() {
        return APIs.ROOT() + "/v1/Profile/processInsurance" + APIs.contextUrl();
    }

    public static String UPDATE_SCHEDULE_PLAN() {
        return APIs.ROOT() + "/v1/doctor/upadteSchedulePlan"
                + APIs.contextUrl();
    }

    public static String UPDATE_SCHEDULE_PLAN_ASSISTANT() {
        return APIs.ROOT() + "/v1/assistants/upadteSchedulePlan"
                + APIs.contextUrl();
    }

    public static String ADD_DELETE_VACATIONS() {
        return APIs.ROOT() + "/v1/doctor/processVacation" + APIs.contextUrl();
    }

    public static String ADD_DELETE_ASST_VACATIONS() {
        return APIs.ROOT() + "/v1/assistants/processVacation"
                + APIs.contextUrl();
    }

    public static String UPDATE_LANGUAGES() {
        return APIs.ROOT() + "v1/Profile/updateLanguage" + APIs.contextUrl();
    }

    public static String UPDATE_CONSULTATION_CHARGES() {
        return APIs.ROOT() + "v1/Profile/updateConsultationCharges"
                + APIs.contextUrl();
    }

    public static String PRESCRIPTION() {
        return APIs.ROOT() + "/v1/doctor/prescription" + APIs.contextUrl();
    }

    public static String ADD_ALLERGIES() {
        return APIs.ROOT() + "/v1/SOAPInstance/addAllergy" + APIs.contextUrl();
    }

    public static String DELETE_DOCUMENT() {
        return APIs.URL() + "/documents/deleteDoc/id/";
    }

    public static String DOCTOR_AUTOSUGGEST() {
        return APIs.URL() + "/v1/doctor/listWithAddress" + APIs.contextUrl();
    }

    public static String PATIENT_AUTOSUGGEST() {
        return APIs.URL() + "/v1/patients/autoSuggest" + APIs.contextUrl()
                + "/name/";
    }

    public static String LABTEST_AUTOSUGGEST() {
        return APIs.URL() + "/v1/labTest/query" + APIs.contextUrl();
    }

    public static String LABPANEL_AUTOSUGGEST() {
        return APIs.URL() + "/v1/lab/queryLabPanel" + APIs.contextUrl();
    }

    public static String REFER_PATIENT_CREATE() {
        return APIs.URL() + "/referPatient/create" + APIs.contextUrl();
    }

    public static String SEND_REGISTRATION() {
        return APIs.URL() + "/v1/pushRegister";
    }

    public static String DRUG_NAME() {
        return APIs.URL() + "/rxnconso/drug/" + APIs.contextUrl() + "name/";
    }

    public static String DOCTOR_LIST() {
        return APIs.URL() + "/doctor/list" + APIs.contextUrl();
    }

    public static String CREATE_SOAP() {
        return APIs.URL() + "/SOAPInstance/create" + APIs.contextUrl();
    }

    public static String SOAP_HISTORY() {
        return APIs.URL() + "/SOAPInstance/history" + APIs.contextUrl();
    }

    public static String SOAP_SHOW() {
        return APIs.URL() + "/SOAPInstance/show/id/";
    }

    public static String PAST_VISIT() {
        return APIs.URL() + "/SOAPInstance/pastVisit/cli/api"
                + APIs.contextUrl();
    }

    public static String REFER_PATIENT_DETAIL() {
        return APIs.URL() + "/referPatient/detail" + APIs.contextUrl();
    }

    public static final String PAST_FOLLOWUP() {
        return APIs.URL() + "/SOAPInstance/pastFollowup/bk-id/";
    }

    public static String SOAP_UPDATE() {
        return APIs.URL() + "/SOAPInstance/update/id/";
    }

    public static String REFER_VIEW() {
        return APIs.URL() + "/SOAPInstance/referView/id/";
    }

    public static String PAST_VISIT_VIEW() {
        return APIs.URL() + "/SOAPInstance/pastVisitView" + APIs.contextUrl();
    }

    public static String SOAP_CREATE() {
        return APIs.URL() + "/SOAPInstance/create/cli/api" + APIs.contextUrl();
    }

    public static String MASTERPLAN_UPDATE() {
        return APIs.URL() + "/masterplan/update/cli/api/id/";
    }

    public static String VIEW() {
        return APIs.URL() + "/documents/show/id/";
    }

    public static String LABS_FOR_LAB_TESTS() {
        return APIs.URL() + "/v1/labTest/checkMasterLabTests"
                + APIs.contextUrl();
    }

    public static String LAB_TEST_ORDER_CREATE() {
        return APIs.URL() + "/v1/order/labDirectOrderCreate"
                + APIs.contextUrl();
    }

    public static String DIRECTCHECKIN() {
        return APIs.URL()
                + "/v1/task/directAppointment/pat_id/{patid}/fam_id/{famid}/cli/api"
                + APIs.contextUrl();
    }

    public static String UNIVERSITY() {
        return APIs.URL() + "/doctor/autoSuggestUniversity" + APIs.contextUrl()
                + "/name/";
    }

    public static String CURRENT_ASSISTANTS() {
        return APIs.ROOT() + "/v1/receptionist/availableReceptionist";
    }

    public static String MED_REC_VISIT_NOTES() {
        return APIs.ROOT() + "/v1/doctor/visitNotes" + APIs.contextUrl()
                + "/cli/api/Booking_page/";
    }

    public static String MED_REC_LIST() {
        return APIs.ROOT() + "/v1/doctor/patientMedicalRecord"
                + APIs.contextUrl()
                + "/type/{type}/cli/api/UserPatientRelation_page/";
    }

    public static String MED_REC_DETAIL() {
        return APIs.ROOT() + "/v1/doctor/medicalRecordDetail"
                + APIs.contextUrl() + "/cli/api";
    }

    public static String DELETE_ALLERGY() {
        return APIs.ROOT() + "/v1/patients/deleteAllergy" + APIs.contextUrl();
    }

    public static String ADD_NEW_PATIENT() {
        return APIs.ROOT() + "/v1/booking/addNewPatient" + APIs.contextUrl();
    }

    public static String ADD_NEW_PATIENT_SUBMIT() {
        return APIs.ROOT() + "/v1/booking/newPatientSubmit" + APIs.contextUrl();
    }

    public static String COPY_PATIENT_ADDRESS() {
        return APIs.ROOT() + "/booking/addNewPatient/cli/api"
                + APIs.contextUrl();
    }

    public static String SEND_COMPOSE_MESSAGE() {
        return APIs.ROOT() + "/v1/notification/Compose" + APIs.contextUrl()
                + "/cli/api";
    }

    public static String PATIENT_BARCODE() {
        return APIs.URL()
                + "/v1/api/barcode/id/display_id/text/display_id/format/json";
    }

    public static String REMOVE_SIGNATURE() {
        return APIs.URL() + "/v1/documents/imageRemove" + APIs.contextUrl();
    }

    public static String CONFIGURATIONS() {
        return APIs.ROOT() + "/v1/business/getConfiguration"
                + APIs.contextUrl();
    }

    public static String ORDERSETS() {
        return APIs.URL() + "/v1/orderSets/search" + APIs.contextUrl();
    }

    public static String GET_GALLERY() {
        return APIs.URL() + "/v1/SOAPInstance/gallery" + APIs.contextUrl();
    }

    public static String POST_GALLERY_PHOTO() {
        return APIs.URL() + "/v1/gmb/ajaxUpload" + APIs.contextUrl();
    }

    public static String DELETE_GALLERY_PHOTO() {
        return APIs.URL() + "/v1/gmb/delete" + APIs.contextUrl();
    }

    public static String EDIT_GALLERY_PHOTO() {
        return APIs.URL() + "/v1/gmb/changeData" + APIs.contextUrl();
    }

    public static String NO_IMAGE() {
        return APIs.URL() + "/img/patient.jpg?D=thumb";
    }

}
