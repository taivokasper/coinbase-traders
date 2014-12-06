/* globals app */

app.directive('loading', function () {
	'use strict';

	return {
		restrict: 'A',
		scope: {
			loading: '=loading'
		},
		transclude: true,
		templateUrl: '/partials/directives/loading.html',
		link: function (scope, elem) {
			elem.css('min-width', elem.css('width'));
		}
	};
});