'use strict';

app.controller('TestController', ['$scope', '$http', '$state', '$q', '$stateParams', '$filter', '$timeout',
    function($scope, $http, $state,  $q, $stateParams) {
	
	this.person = [
		{ID:'23456',Name:"Monitor"},
		{ID:'45677',Name:"Keyboard"},
		{ID:'67895',Name:"MacBook"}
	];
	
}]);