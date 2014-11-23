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
        .state('new', {
            url: '/new',
            templateUrl: 'partials/register-client.html',
            controller: 'RegisterClientCtrl'
        })
        .state('existing', {
            url: '/existing/{apiKey}',
            params: {
                apiKey: {value: ''}
            },
            templateUrl: 'partials/client.html',
            controller: 'ClientCtrl'
        })
        .state('error', {
            url: '^/error',
            templateUrl: 'partials/error-page.html',
            controller: 'ErrorCtrl'
        });
});