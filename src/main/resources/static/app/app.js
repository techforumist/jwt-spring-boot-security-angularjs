// Creating angular JWTDemoApp with module name "JWTDemoApp"
angular.module('JWTDemoApp', [ 'ui.router' ])


// the following method will run at the time of initializing the module. That
// means it will run only one time.
.run(function(AuthService, $rootScope, $state) {
	// For implementing the authentication with ui-router we need to listen the
	// state change. For every state change the ui-router module will broadcast
	// the '$stateChangeStart'.
	$rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
		// checking the user is logged in or not
		if (!AuthService.user) {
			// To avoiding the infinite looping of state change we have to add a
			// if condition.
			if (toState.name != 'login' && toState.name != 'register') {
				event.preventDefault();
				$state.go('login');
			}
		} else {
			// checking the user is authorized to view the states
			if (toState.data && toState.data.role) {
				var hasAccess = false;
				for (var i = 0; i < AuthService.user.roles.length; i++) {
					var role = AuthService.user.roles[i];
					if (toState.data.role == role) {
						hasAccess = true;
						break;
					}
				}
				if (!hasAccess) {
					event.preventDefault();
					$state.go('access-denied');
				}

			}
		}
	});
});