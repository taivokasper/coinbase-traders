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
}).factory('$exceptionHandler', function ($log, $injector, $window, logCollector) {
    'use strict';

    var Bugsnag = $window.Bugsnag;

    var notifyBugsnag = function (exception, cause) {
        Bugsnag.apiKey = '0dfe2c5146836116d5a8d289c43c2885';
        Bugsnag.releaseStage = getBugsnagStage();
        Bugsnag.metaData = getBugsnagMetaData();

        if (angular.isString(exception)) {
            Bugsnag.notify(exception);
        } else {
            Bugsnag.notifyException(exception, {diagnostics: {cause: cause}});
        }
    };

    var getBugsnagMetaData = function () {
        var log = logCollector.getHistoryAsString(true);
        return {
            history: {
                log: log
            }
        };
    };

    var getBugsnagStage = function () {
        var $location = $injector.get('$location');

        if ($location.search('localhost') || $location.search('127.0.0.1')) {
            return 'Development';
        }
        return 'Production';
    };

    return function (exception, cause) {
        $log.error.apply($log, arguments);
        notifyBugsnag(exception, cause);
    };
});