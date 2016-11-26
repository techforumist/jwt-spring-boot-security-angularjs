angular.module('JWTDemoApp')
// Creating the Angular Controller
.controller('LoginController', function($http, $scope, $state, AuthService, $rootScope) {
	// method for login
	$scope.login = function() {
		// requesting the token by usename and passoword
		$http({
			url : 'authenticate',
			method : "POST",
			params : {
				username : $scope.username,
				password : $scope.password
			}
		}).success(function(res) {
			$scope.password = null;
			// checking if the token is available in the response
			if (res.token) {
				$scope.message = '';
				// setting the Authorization Bearer token with JWT token
				$http.defaults.headers.common['Authorization'] = 'Bearer ' + res.token;

				// setting the user in AuthService
				AuthService.user = res.user;
				$rootScope.$broadcast('LoginSuccessful');
				// going to the home page
				$state.go('home');
			} else {
				// if the token is not present in the response then the
				// authentication was not successful. Setting the error message.
				$scope.message = 'Authetication Failed !';
			}
		}).error(function(error) {
			// if authentication was not successful. Setting the error message.
			$scope.message = 'Authetication Failed !';
		});
	};
});
