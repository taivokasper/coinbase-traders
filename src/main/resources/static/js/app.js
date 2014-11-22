/*jslint devel: true */
/* global app: true, angular */

app = angular.module('app', ['ngResource', 'ui.router']);

app.config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');

    $stateProvider
        .state('front-page', {
            url: '/',
            templateUrl: 'partials/front-page.html',
            controller: 'FrontPageCtrl'
        })
        .state('error', {
            url: '^/error',
            templateUrl: 'partials/error-page.html',
            controller: 'ErrorCtrl'
        });
});