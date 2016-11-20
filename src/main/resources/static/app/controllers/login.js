angular.module('AddressBook')
// Creating the Angular Controller
.controller('LoginController', function($http, $scope, $state, AuthService, $rootScope) {
	// method for login
	$scope.login = function() {
		$http({
			url : 'authenticate',
			method : "POST",
			params : {
				username : $scope.username,
				password : $scope.password
			}
		}).success(function(res) {
			$scope.password = null;
			if (res.token) {
				$scope.message = '';
				$http.defaults.headers.common['Authorization'] = 'Bearer ' + res.token;
				AuthService.user = res.user;
				$rootScope.$broadcast('LoginSuccessful');
				$state.go('home');
			} else {
				$scope.message = 'Authetication Failed !';
			}
		}).error(function(error) {
			$scope.message = 'Authetication Failed !';
		});
	};
});
