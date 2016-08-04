
var app = angular.module('myApp', ['ngAnimate']);
app.controller('addDiagnoseController', function($scope, $http) {
	
   $scope.submitDiagnose = function(form) {
		var req = {
		 method: 'POST',
		 url: 'http://localhost:9000/web/diagnose/add',
		 headers: {
		    'Authorization': 'Bearer '+sessionStorage.getItem("authToken")
		 },
		 data: { diagnoseName: $scope.diagnoseName}
		}
		
		console.debug($scope.diagnoseName);
		
		$http(req).then(function(response){
			 $scope.greeting = response.data;
			 if (response.status == '200') {
				$scope.diagnoseName = null;
				 console.debug(response.data);
			sessionStorage.setItem("diagnoseID", response.data);
			 }
			 
			 console.debug(response.status);
			 window.location="addDiagnose.html"
		});
	}	
});

app.controller('addPatientController', function($scope, $http) {

var req = {
		 method: 'GET',
		 url: 'http://localhost:9000/web/diagnose/all',
		 headers: {
		   'Authorization': 'Bearer '+sessionStorage.getItem("authToken")
		 }
		}

		
		$http(req).then(function(response){
			 if (response.status == '200') {
				$scope.myData = response.data;
				console.debug(response.data);
			 }
		},function(response){
		 console.debug(response);
	});


   $scope.submitPatient = function(form) {
		var req = {
		 method: 'POST',
		 url: 'http://localhost:9000/web/patient/add',
		 headers: {
		   'Authorization': 'Bearer '+sessionStorage.getItem("authToken")
		 },
		 data: {
					firstName: $scope.patientFirstName, lastName: $scope.patientLastName, diagnoseId: $scope.selectDiagnose.id
				}
		}
		console.debug($scope.selectDiagnose.id);
		$http(req).then(function(response){
			 if (response.status == '200') {
				console.debug($scope.selectDiagnose);
				console.debug(response.data);
				
				sessionStorage.setItem("patientID", response.data.id);
				sessionStorage.setItem("diagnoseID", $scope.selectDiagnose.id);
				window.location="addPatient.html"
			 }
			 if(response.status=='401')
			 {
				 alert("Грешно потребителско име/парола");
			 }
			 
		},function(response){
		 console.debug(response);
	});
   }
   
});

app.controller('patientCtrl', function($scope, $http) {
  	var req = {
		 method: 'GET',
		 url: 'http://localhost:9000/web/patient/all',
		 headers: {
		   'Authorization': 'Bearer '+sessionStorage.getItem("authToken")
		 }
		}

		
		$http(req).then(function(response){
			 if (response.status == '200') {
				$scope.myData = response.data;
				console.debug(response.data);
			 }
		},function(response){
		 console.debug(response);
	});
	$scope.getIndex = function(index) {
       sessionStorage.setItem("patientID", index);
	   window.location="addPatient.html";
			
	}
});

app.controller('diagnoseCtrl', function($scope, $http) {
  	var req = {
		 method: 'GET',
		 url: 'http://localhost:9000/web/diagnose/all',
		 headers: {
		   'Authorization': 'Bearer '+sessionStorage.getItem("authToken")
		 }
		}

		
		$http(req).then(function(response){
			 if (response.status == '200') {
				$scope.myData = response.data;
				console.debug(response.data);
			 }
		},function(response){
		 console.debug(response);
	});
	
	$scope.getIndex = function(index) {
       sessionStorage.setItem("diagnoseID", index);
	   window.location="addDiagnose.html";
	}
});




