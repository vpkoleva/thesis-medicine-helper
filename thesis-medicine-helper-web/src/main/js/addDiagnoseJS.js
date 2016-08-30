function convert(value, duration) {
	var end
	switch(duration){
		case "MONTH":
			end = value == 1 ? "месец" : "месеца"
			break
		case "YEAR":
			end = value == 1 ? "година" : "години"
			break
		case "DAY":
		default:
			end = value == 1 ? "ден" : "дни"
	}
	return value + " " + end
}

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
		location.reload()
	});
	$('#myModal').on('shown.bs.modal', function(e) {
		if(sessionStorage.getItem("isForUpdate") == 'true') {
			$http(requestGet(urlWebScheduleGet + sessionStorage.getItem("scheduleId"))).then(function(response) {
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
		$http(requestPost(url, data)).then(function(response) {
			if (response.data.success) {
				$('#myModal').modal('hide');
			} else {
				alert(response.data.error);
			}
		}, handleErrorResponse);
	}
});

app.controller('schedulesCtrl', function($scope, $http) {
	$http(requestGet(urlWebScheduleAllDiagnose + sessionStorage.getItem("diagnoseID"))).then(function(response) {
		$scope.schedules = []
		angular.forEach(response.data, function(item) {
			item.startAfter = convert(item.startAfter, item.startAfterType)
			item.frequency = convert(item.frequency, item.frequencyType)
			item.duration = convert(item.duration, item.durationType)
			$scope.schedules.push(item)
		});
	}, handleErrorResponse);
	$scope.edit = function(index) {
		sessionStorage.setItem("scheduleId", index);
		sessionStorage.setItem("isForUpdate", true);
		$("#myModal").modal("show");
	}
	$scope.delete = function(index) {
		if (confirm("Моля потвърдете изтриването")) {
			$http(requestDelete(urlWebScheduleDelete + index)).then(function(response) {
				if (response.data.success) {
					location.reload()
				} else {
					alert(response.data.error);
				}
			}, handleErrorResponse);
		}
	}
});