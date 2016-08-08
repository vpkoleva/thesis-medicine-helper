var app = angular.module('myApp', ['ngAnimate']);
app.controller('addDiagnoseController', function($scope, $http) {
	$scope.submitDiagnose = function(form) {
		var data = { diagnoseName: $scope.diagnoseName }
		$http(requestPost(urlWebDiagnoseSave, data)).then(function(response){
			if (response.data.success) {
				sessionStorage.setItem("diagnoseID", response.data.id)
				window.location="addDiagnose.html"
			} else {
				alert(response.data.error)
				location.reload()
			}
		}, handleErrorResponse);
	}
	clearFormOnClose("#addDiagnose")
});

app.controller('addPatientController', function($scope, $http) {
	$http(requestGet(urlWebDiagnoseAll)).then(function(response) {
		$scope.myData = response.data;
	}, handleErrorResponse)
	$scope.submitPatient = function(form) {
		var data = { firstName: $scope.patientFirstName, lastName: $scope.patientLastName, diagnoseId: $scope.selectDiagnose.id }
		$http(requestPost(urlWebPatientSave, data)).then(function(response) {
			if (response.data.success) {
				sessionStorage.setItem("patientID", response.data.id)
				sessionStorage.setItem("diagnoseID", $scope.selectDiagnose.id)
				window.location="addPatient.html"
			} else {
				alert(response.data.error)
				location.reload()
			}
		}, handleErrorResponse);
	}
	clearFormOnClose("#addPatient")
});

app.controller('linkMobileUserController', function($scope, $http) {
	$('#linkMobileUser').on('show.bs.modal', function(e) {
		var patientId = $(e.relatedTarget).data('patient-id');
		$scope.submitLink = function(form) {
			var data = { patientId: patientId, code: $scope.code }
			$http(requestPost(urlWebPatientLink, data)).then(function(response) {
				if (response.data.success) {
					$('#linkMobileUserToPatientModal').modal('hide');
					alert("Потребителя беше свързан успешно!");
				} else {
					alert(response.data.error)
					location.reload()
				}
			}, handleErrorResponse);
		}
	});
	
	clearFormOnClose("#linkMobileUser")
});

app.controller('patientCtrl', function($scope, $http) {
	$http(requestGet(urlWebPatientAll)).then(function(response) {
		 $scope.myData = response.data;
	}, handleErrorResponse);
	$scope.getIndex = function(index) {
		sessionStorage.setItem("patientID", index);
		window.location="addPatient.html";
	}
});

app.controller('diagnoseCtrl', function($scope, $http) {
	$http(requestGet(urlWebDiagnoseAll)).then(function(response) {
		$scope.myData = response.data;
	}, handleErrorResponse)
	$scope.getIndex = function(index) {
		sessionStorage.setItem("diagnoseID", index);
		window.location="addDiagnose.html";
	}
});