/* globals angular */

var app = angular.module('app', ['ngResource', 'ui.router']);

app.config(function ($stateProvider, $urlRouterProvider) {
    'use strict';

    $urlRouterProvider.otherwise('/');

    $stateProvider
        .state('front-page', {
            url: '/',
            templateUrl: 'partials/front-page.html'
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
            templateUrl: 'partials/error-page.html'
        });
});