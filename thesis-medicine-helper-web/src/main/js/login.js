var app = angular.module('Login', []);
app.controller('loginController', function($scope, $http) {
   $scope.submit = function(form) {
		var req = {
		 method: 'POST',
		 url: 'http://localhost:9000/token',
		 headers: {
		   'Content-Type': 'application/x-www-form-urlencoded'
		 },
		 data: $.param({grant_type: "password", username: $scope.username, password: $scope.password})
		 
		}
		
		$http(req).then(function(response){
			 	console.debug(response);
			 if (response.status == '200') {
		
			 }
			 sessionStorage.setItem("authToken", response.data.authToken);
			 window.location="index.html"
		},  function(response){
		 console.debug(response);
	});
	}
});

