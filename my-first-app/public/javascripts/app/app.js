'use strict';

var app = angular.module('app', [
    'ngAnimate',
    'ui.router'
]).run(function ($rootScope, $state, $stateParams, $window, $templateCache, $http) {
	
});


app.config(function($stateProvider, $urlRouterProvider) {
	$stateProvider.state('test',              {url: "/test",    templateUrl: 'assets/partials/test/test.html'});
});