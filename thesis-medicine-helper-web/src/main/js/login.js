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
		$http(req).then(function(response) {
			if (response.data.isDoctor) {
				sessionStorage.setItem("authToken", response.data.authToken);
				window.location="index.html"
			} else {
				alert("Само лекари могат да използват приложението")
			}
		}, function(response) {
			if (response.data != null) {
				if (response.status == '401') {
					alert("Грешни потребителско име или парола");
				}
			} else {
				alert("Няма връзка със сървъра")
			}
		});
	}
});