$(document).ready(function() {
	
console.debug(sessionStorage.getItem("patientID"));
		$('#calendar').fullCalendar({
			defaultDate:  new Date().toJSON().slice(0,10),
			editable: true,
			eventLimit: true, // allow "more" link when too many events
			eventClick: function(event, element) {
			sessionStorage.setItem("eventId", event.id);
			sessionStorage.setItem("isForUpdate", true);
			$("#myModal").modal("show");
			$('#calendar').fullCalendar('updateEvent', event);
		},
			events: {
				url: 'http://localhost:9000/web/schedule/allFromPatients',
				headers: {
			   'Authorization': 'Bearer '+sessionStorage.getItem("authToken")
						},
				type: 'GET',
				data: {
						patientId: sessionStorage.getItem("patientID")
						
				},
				error: function(response) {
			
					console.debug(response);
				}
		}
		});
		
	});
	
var app = angular.module('myApp', []);
app.controller('loadDefaultDiagnoseTmpl', function($scope, $http) {
   $scope.submitDefault = function(form) {
	   console.debug($scope.startDate);
	   console.debug(sessionStorage.getItem("patientID"));
	   console.debug(sessionStorage.getItem("diagnoseID"));
		var req = {
			method: 'POST',
			url: 'http://localhost:9000/web/schedule/saveFromDefault',
			headers: {
				'Authorization': 'Bearer '+sessionStorage.getItem("authToken")
			},
			data: {
				startDay: $scope.startDate,
				id: sessionStorage.getItem("patientID"),
				diagnoseId: sessionStorage.getItem("diagnoseID")
			}
		}
		
		$http(req).then(function(response){
			 $scope.greeting = response.data;
			 if (response.status == '200') {
				$scope.action = null;
			 }
			 console.debug(response.status);
			 window.location="index.html"
		});
	}
});


app.controller('scheduleController', function($scope, $http) {
	$('#myModal').on('hidden.bs.modal', function(e) {
		sessionStorage.setItem("isForUpdate", false);
		$scope.scheduleId = null;
		$scope.action = null;
		$scope.startAfter = null;
		$scope.frequency = null;
		$scope.duration = null;
		$scope.startDate = null;
		$scope.$apply();
		$('#calendar').fullCalendar( 'refetchEvents' );
	
	});
	$('#myModal').on('shown.bs.modal', function(e) {
		if(sessionStorage.getItem("isForUpdate") == 'true') {
			var req = {
				method: 'GET',
				url: 'http://localhost:9000/web/schedule/get/' + sessionStorage.getItem("eventId"),
				headers: {
					'Authorization': 'Bearer '+sessionStorage.getItem("authToken")
				}
			}
			$http(req).success(function(data, status) {
				$scope.scheduleId = data.id;
				$scope.action = data.description;
				$scope.startAfter = data.startAfterValue;
				$scope.startAfterType = data.startAfterType;
				$scope.frequency = data.frequencyValue;
				$scope.frequencyType = data.frequencyType;
				$scope.duration = data.endAfterValue;
				$scope.durationType = data.endAfterType;
				$scope.startDate = new Date(data.startDate);
				
				console.debug(data);
			}).error(function(data, status) {
			//	alert(data.error);
			});
		} else {
			$scope.startAfterType = "DAY";
			$scope.frequencyType = "DAY";
			$scope.durationType = "DAY";
			$scope.$apply();
		}
	});
	$scope.delete =function()
	{
		
	var req = {
			method: 'POST',
			url: 'http://localhost:9000/web/schedule/delete',
			headers: {
				'Authorization': 'Bearer '+sessionStorage.getItem("authToken")
			},
			data: {
				id: $scope.scheduleId
			}
		}
		$http(req).success(function(data, status) {
			console.debug(data);
			if (data.success) {
				$('#myModal').modal('hide');
			} else {
				alert("else");
				alert(data.error);
			}
		}).error(function(data, status) {
			alert(data.message);
		});
	};
	$scope.submit = function(form) {
		var isUpdate = sessionStorage.getItem("isForUpdate") == 'true';
		var req = {
			method: 'POST',
			url: isUpdate?'http://localhost:9000/web/schedule/update':'http://localhost:9000/web/schedule/save',
			headers: {
				'Authorization': 'Bearer '+sessionStorage.getItem("authToken")
			},
			data: {
				id: $scope.scheduleId,
				description: $scope.action,
				startAfter: $scope.startAfter,
				startAfterType: $scope.startAfterType,
				frequency: $scope.frequency,
				frequencyType: $scope.frequencyType,
				duration: $scope.duration,
				durationType: $scope.durationType,
				diagnoseId: sessionStorage.getItem("diagnoseID"),
				startDate: $scope.startDate,
				patientId: sessionStorage.getItem("patientID")
			}
		}
		$http(req).success(function(data, status) {
			if (data.success) {
				$('#myModal').modal('hide');
			} else {
				alert(data.error);
			}
		}).error(function(data, status) {
			alert(data.message);
		});
	}
});
/*
app.controller('scheduleController', function($scope, $http) {
   $scope.submit = function(form) {
		var req = {
		 method: 'POST',
		 url: 'http://localhost:9000/web/schedule/save',
		 headers: {
		   'Authorization': 'Bearer '+sessionStorage.getItem("authToken")
		 },
		 data: { description: $scope.action, startAfter: $scope.startAfter, startAfterType: $scope.startAfterType, duration: $scope.duration,
		 durationType: $scope.durationType, frequency: $scope.frequency, frequencyType: $scope.frequencyType, diagnoseId: sessionStorage.getItem("diagnoseID")}
		}
		
		console.debug($scope.action);
		
		$http(req).then(function(response){
			 $scope.greeting = response.data;
			 if (response.status == '200') {
				$scope.action = null;
			 }
			 sessionStorage.setItem("lastname", "Smith");
			 console.debug(response.status);
			 window.location="index.html"
		});
	}
});
	*/