var app = angular.module('login', []);
app
		.controller(
				'loginControl',
				function($scope, $http, $window) {
					$scope.submitForm = function() {
						var data = $.param({
							username : $scope.username,
							password : $scope.password
						});
						var config = {
							headers : {
								'Content-Type' : 'application/x-www-form-urlencoded; charset=UTF-8'
							}
						}
						$http
								.post('/api/authenticate', data, config)
								.success(
										function(data, status, headers, config) {
											$scope.PostDataResponse = data;
											$window.location.href = '/private/home.html';
										}).error(
										function(data, status, header, config) {
											$scope.ResponseDetails = "Data: "
													+ data + "<hr />status: "
													+ status
													+ "<hr />headers: "
													+ header + "<hr />config: "
													+ config;
										});
					}

				});