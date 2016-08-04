var app = angular.module('myApp', []);

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
			url: 'http://localhost:9000/web/schedule/all',
			headers: {
				'Authorization': 'Bearer '+sessionStorage.getItem("authToken")
			},
			type: 'GET',
			data: {
				diagnoseId: sessionStorage.getItem("diagnoseID")
			},
			error: function (response, textStatus, errorThrown) {
				alert(response.responseText);
			}
		}
	});
});

app.controller('formController', function($scope, $http) {
	$('#myModal').on('hidden.bs.modal', function(e) {
		sessionStorage.setItem("isForUpdate", false);
		$scope.scheduleId = null;
		$scope.action = null;
		$scope.startAfter = null;
		$scope.frequency = null;
		$scope.duration = null;
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
			}).error(function(data, status) {
				alert(data.error);
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
				diagnoseId: sessionStorage.getItem("diagnoseID")
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

    