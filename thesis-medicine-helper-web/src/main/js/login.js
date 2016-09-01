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
				showErrors("Само лекари могат да използват приложението")
			}
		}, function(response) {
			if (response.data != null) {
				if (response.status == '401') {
					showErrors("Грешни потребителско име или парола");
				}
			} else {
				showErrors("Няма връзка със сървъра")
			}
		});
	}
});

function showErrors(message){
	var error = document.getElementById("error")
	error.innerHTML = message;
	error.className += "alert alert-danger";
}