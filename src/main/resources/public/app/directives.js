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

app.directive('error', function () {
    'use strict';

    return {
        restrict: 'E',
        scope: {
            message: '=?'
        },
        templateUrl: '/partials/directives/error.html'
    };
});

app.directive('stats', function () {
    'use strict';

    return {
        restrict: 'E',
        templateUrl: '/partials/directives/stats.html',
        controller: function ($scope, StatsResources) {

            StatsResources.getStats().$promise.then(function (stats) {
                $scope.stats = stats;
            });
        }
    };
});