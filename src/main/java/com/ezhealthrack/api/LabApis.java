package com.ezhealthrack.api;

public class LabApis {
	public static final String CONFIRMED_LIST = APIs.ROOT()
			+ "/v1/booking/appointmentsv2" + APIs.contextUrl();
	public static final String DASHBOARD = APIs.ROOT()
			+ "/v1/site/LabMemberDashboard" + APIs.contextUrl();
	public static final String INBOX = APIs.ROOT() + "/v1/notification/inbox"
			+ APIs.contextUrl();
	public static final String OUTBOX = APIs.ROOT() + "/v1/notification/outbox"
			+ APIs.contextUrl();
	public static final String ACCOUNTVIEW = APIs.ROOT()
			+ "/v1/labUser/account" + APIs.contextUrl();
	public static final String UPDATEACCOUNT = APIs.ROOT()
			+ "/v1/labUser/updateUserAccountAjax" + APIs.contextUrl();
	public static final String SENDMESSAGE = APIs.ROOT()
			+ "/v1/notification/sendMessage" + APIs.contextUrl();
	public static final String REJECTAPPOINTMENT = APIs.ROOT()
			+ "/v1/task/labAction/appointment/{booking_Id}/cancel";
	public static final String ACCEPTAPPOINTMENT = APIs.ROOT()
			+ "/v1/task/labAction/appointment/{booking_Id}/confirm";
	public static final String REGISTERAPPOINTMENT = APIs.URL()
			+ "/v1/task/labAction/appointment/start/register";
	public static final String RESCHEDULEAPPOINTMENT = APIs.URL()
			+ "/v1/task/labAction/appointment/{booking_Id}/reschedule";
	public static final String FOLLOWUPAPPOINTMENT = APIs.URL()
			+ "/v1/task/labAction/appointment/{booking_Id}/followup";
	public static final String ACTVDOCDETAILS = APIs.ROOT()
			+ "/v1/doctor/listWithAddress" + APIs.contextUrl();
	public static final String GETCREATEORDER = APIs.URL()
			+ "/v1/lab/orderCreate/checkid/{booking_Id}";
	public static final String CREATEORDER = APIs.URL()
			+ "/lab/orderCreate/cli/api/checkid/";
	public static final String VIEWORDER = APIs.URL()
			+ "/v1/lab/orderView/cli/api/id/";
	public static final String ORDERPAYMENTHISTORY = APIs.URL()
			+ "/v1/lab/billHistory/cli/api/id/";
	public static final String ORDERLIST = APIs.URL() + "/v1/lab/order"
			+ APIs.contextUrl();
	public static final String BILLLIST = APIs.URL() + "/v1/lab/bill"
			+ APIs.contextUrl();
	public static final String TECHNICIANLIST = APIs.URL()
			+ "/v1/labUser/techniciansList" + APIs.contextUrl();
	public static final String ORDERSAMPLING = APIs.URL()
			+ "/v1/lab/orderSampling/cli/api/id/";
	public static final String ORDERSAMPLINGDONE = APIs.URL()
			+ "/v1/lab/orderSamplingDone/cli/api/id/";
	public static final String GENERATEBILL = APIs.URL()
			+ "/v1/lab/generateBill/";
	public static final String PRINTLABEL = APIs.URL()
			+ "/v1/api/barcode/id/order_display_id/text/order_display_id/format/json";
	public static final String REPORTVALUES = APIs.URL()
			+ "/v1/lab/reportFill/order_id/{orderid}/report_id/{reportid}/cli/api";
	public static final String MAKEPAYMENT = APIs.URL()
			+ "/v1/lab/billPayment/cli/api/id/";
	public static final String REPORTERROR = APIs.URL()
			+ "/v1/lab/reportError/order_id/{order_id}/report_id/{lab_order_report_id}/cli/api";
	public static final String BILLREFUNDSUBMIT = APIs.URL()
			+ "/v1/lab/billRefund/id/{order_id}/report_id/{lab_order_report_id}/cli/api";
	public static final String DIRECTAPPOINTMENT = APIs.URL()
			+ "/task/directAppointment" + APIs.contextUrl();
	public static final String PUBLISHREPORT = APIs.URL()
			+ "/v1/lab/publishReport/id/{order_id}/status/publish";
	public static final String UNPUBLISHREPORT = APIs.URL()
			+ "/v1/lab/publishReport/id/{order_id}/status/unpublish";
	public static final String REPORTAPPROVAL = APIs.URL()
			+ "/v1/workList/action/lab-report/{data_id}/";
	public static final String ORDERCANCEL = APIs.URL()
			+ "/lab/orderCancel/cli/api/id/";
	public static final String GET_APPOINTMENT = APIs.URL()
			+ "/v1/booking/appointmentsv2/format/json/id/";
	public static final String GET_LAB_WORK_FLOW = APIs.URL()
			+ "/workflow/userWorkflow/cli/api" + APIs.contextUrl();
	public static final String GET_LAB_WORK_FLOW_ID = APIs.URL()
			+ "/workflow/getWorkflow/id/";

}
