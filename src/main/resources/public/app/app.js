var app = angular.module('app', ['ngResource', 'ui.router', 'ngTable', 'fcsa-number', 'nsPopover', 'logItDown']);

app.config(function ($stateProvider, $urlRouterProvider) {
    'use strict';

    $urlRouterProvider.when('/', '/about');
    $urlRouterProvider.otherwise('/');

    $stateProvider
        .state('about', {
            url: '/about',
            templateUrl: 'partials/about-page.html'
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
            templateUrl: 'partials/clients.html',
            controller: 'ClientsCtrl'
        })
        .state('error', {
            url: '^/error',
            templateUrl: 'partials/error-page.html'
        });
});