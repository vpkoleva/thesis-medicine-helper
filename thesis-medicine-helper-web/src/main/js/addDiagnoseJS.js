$(document).ready(function() {
	$('#calendar').fullCalendar({
		eventClick: function(event, element) {
			sessionStorage.setItem("eventId", event.id);
			sessionStorage.setItem("isForUpdate", true);
			$("#myModal").modal("show");
			$('#calendar').fullCalendar('updateEvent', event);
		},
		defaultDate: '1970-01-01',
		editable: true,
		eventLimit: true, // allow "more" link when too many events
		events: {
			url: baseUrl + urlWebScheduleAllDiagnose + sessionStorage.getItem("diagnoseID"),
			headers: { 'Authorization': 'Bearer ' + sessionStorage.getItem("authToken") },
			type: 'GET',
			error: handleErrorResponse
		}
	});
});

var app = angular.module('myApp', []);
app.controller('formController', function($scope, $http) {
	$('#myModal').on('hidden.bs.modal', function(e) {
		sessionStorage.setItem("isForUpdate", false);
		$scope.scheduleId = null;
		$scope.action = null;
		$scope.startAfter = null;
		$scope.frequency = null;
		$scope.duration = null;
		$scope.$apply();
		$('#calendar').fullCalendar('refetchEvents');
	});
	$('#myModal').on('shown.bs.modal', function(e) {
		if(sessionStorage.getItem("isForUpdate") == 'true') {
			$http(requestGet(urlWebScheduleGet + sessionStorage.getItem("eventId"))).then(function(response) {
				$scope.scheduleId = response.data.id;
				$scope.action = response.data.description;
				$scope.startAfter = response.data.startAfter;
				$scope.startAfterType = response.data.startAfterType;
				$scope.frequency = response.data.frequency;
				$scope.frequencyType = response.data.frequencyType;
				$scope.duration = response.data.duration;
				$scope.durationType = response.data.durationType;
			}, handleErrorResponse);
		} else {
			$scope.startAfterType = "DAY";
			$scope.frequencyType = "DAY";
			$scope.durationType = "DAY";
			$scope.$apply();
		}
	});
	$scope.delete = function() {
		if (confirm("Моля потвърдете изтриването")) {
			$http(requestDelete(urlWebScheduleDelete + $scope.scheduleId)).then(handleWebScheduleResponse, handleErrorResponse);
		}
	};
	$scope.submit = function(form) {
		var url = sessionStorage.getItem("isForUpdate") == 'true' ? urlWebScheduleUpdate : urlWebScheduleSave;
		var data = {
			id: $scope.scheduleId,
			description: $scope.action,
			startAfter: $scope.startAfter,
			startAfterType: $scope.startAfterType,
			frequency: $scope.frequency,
			frequencyType: $scope.frequencyType,
			duration: $scope.duration,
			durationType: $scope.durationType,
			diagnoseId: sessionStorage.getItem("diagnoseID")
		}
		$http(requestPost(url, data)).then(handleWebScheduleResponse, handleErrorResponse);
	}
});

var handleWebScheduleResponse = function(response) {
	if (response.data.success) {
		$('#myModal').modal('hide');
	} else {
		alert(response.data.error);
	}
}