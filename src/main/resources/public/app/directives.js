/* globals app */

app.directive('loading', function () {
    'use strict';

    return {
        restrict: 'A',
        scope: {
            loading: '='
        },
        transclude: true,
        templateUrl: 'partials/directives/loading.html',
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
        templateUrl: 'partials/directives/error.html'
    };
});

app.directive('stats', function () {
    'use strict';

    return {
        restrict: 'E',
        templateUrl: 'partials/directives/stats.html',
        controller: function ($scope, $log, StatsResources, $interval) {

            $scope.stats = {
                sellPrice: '--',
                buyPrice: '--'
            };

            var getStats = function () {
                StatsResources.getStats().$promise.then(function (stats) {
                    $scope.stats = stats;
                }).catch(function (error) {
                    $log.error('Something is wrong with stats request', error);
                });
            };
            getStats();
            $interval(getStats, 1000);
        }
    };
});

app.directive('popoverIcon', function () {
    'use strict';

    return {
        restrict: 'E',
        transclude: true,
        scope: {
            for: '@'
        },
        templateUrl: '/partials/directives/popover-icon.html'
    };
});