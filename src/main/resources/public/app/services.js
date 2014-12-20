/* global app */

app.factory('$exceptionHandler', function ($log, $injector, $window, logCollector) {
    'use strict';

    var Bugsnag = $window.Bugsnag;
    Bugsnag.apiKey = '0dfe2c5146836116d5a8d289c43c2885';

    var notifyBugsnag = function (exception) {
        Bugsnag.releaseStage = getBugsnagStage();
        Bugsnag.metaData = getBugsnagMetaData();
        Bugsnag.context = $injector.get('$location').url();

        Bugsnag.notifyException(exception);
    };

    var getBugsnagMetaData = function () {
        return {
            logs: {
                log: logCollector.getHistoryAsString()
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