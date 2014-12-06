/* globals app, Bugsnag */

app.factory('$exceptionHandler', function ($log, $injector) {
	'use strict';

	return function (exception, cause) {
		var $location = $injector.get('$location');

		if ($location.search('localhost') || $location.search('127.0.0.1')) {
			$log.error(exception);
		} else {
			Bugsnag.notifyException(exception, {diagnostics: {cause: cause}});
		}
	};
});