var baseUrl = 'http://localhost:9000'
var urlWebDiagnoseAll = '/web/diagnose/all'
var urlWebDiagnoseSave = '/web/diagnose/save'
var urlWebDiagnoseDelete = '/web/diagnose/delete/'
var urlWebPatientAll = '/web/patient/all'
var urlWebPatientSave = '/web/patient/save'
var urlWebPatientLink = '/web/patient/link'
var urlWebPatientDelete = '/web/patient/delete/'
var urlWebScheduleAllDiagnose = '/web/schedule/all/diagnose/'
var urlWebScheduleAllPatient = '/web/schedule/all/patient/'
var urlWebScheduleGet = '/web/schedule/get/'
var urlWebScheduleDelete = '/web/schedule/delete/'
var urlWebScheduleSave = '/web/schedule/save'
var urlWebScheduleSaveDefault = '/web/schedule/save/default'
var urlWebScheduleUpdate = '/web/schedule/update'

var authToken = sessionStorage.getItem("authToken")
if (authToken === undefined || authToken === null) {
	window.location="login.html"
}

var handleErrorResponse = function(response) {
	console.debug(response)
	if (response.status == 401) {
		window.location="login.html"
	}
	if (response.data === null) {
		alert("Няма връзка със сървъра")
		window.location="login.html"
	}
	if (response.status == 500) {
		alert("Възникна грешка на сървъра")
	}
}

var requestGet = function(url){
	var request = {}
	request['method'] = 'GET',
	request['url'] = baseUrl + url,
	request['headers'] = { 'Authorization': 'Bearer '+sessionStorage.getItem("authToken") }
	return request
}

var requestPost = function(url, data){
	var request = {}
	request['method'] = 'POST',
	request['url'] = baseUrl + url,
	request['headers'] = { 'Authorization': 'Bearer '+sessionStorage.getItem("authToken") }
	request['data'] = data
	return request
}

var requestDelete = function(url){
	var request = {}
	request['method'] = 'DELETE',
	request['url'] = baseUrl + url,
	request['headers'] = { 'Authorization': 'Bearer '+sessionStorage.getItem("authToken") }
	return request
}

var clearFormOnClose = function(formId){
	$(formId).on("hidden.bs.modal", function(){
		$(this).find('form').trigger('reset');
	});
}

