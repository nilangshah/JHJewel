var app = angular.module('billApp', [ "ngRoute" ]);

app.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/home', {
		templateUrl : 'templates/about.html',
		controller : 'AboutController'
	}).when('/addDealer', {
		templateUrl : 'templates/add-dealer.html',
		controller : 'AddDealerController'
	}).otherwise({
		redirectTo : '/home'
	});
} ]);

app.controller('AboutController', function($scope) {
});

app.controller('AddDealerController', function($scope, $window, $http) {
	$scope.dealerTypeList = [ "buyer", "seller" ];
	$scope.addDealer = function() {
		var config = {
			headers : {
				'Content-Type' : 'application/json; charset=UTF-8'
			}
		}
		var data = $scope.dealer
		$http.post('/dealers', data, config).success(
				function(response, status, headers, config) {
					console.dir(response);
					bootbox.alert("Delear Added Successfully");
				}).error(function(response, status, header, config) {
			console.dir(response);
			bootbox.alert(response);
		});
	}
});