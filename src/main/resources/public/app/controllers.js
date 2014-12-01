/* jshint newcap: false */
/* global app, angular, ngTableParams */

app.controller('RootCtrl', function ($scope) {});

app.controller('RegisterClientCtrl', function ($scope, $state, $log, RegisterClientResource, RandomResource) {
    'use strict';

    $scope.data = {
        type: 'buy'
    };

    RandomResource.getRandom({}, function (random) {
        $scope.data.randomId = random.randomId;
    });

    $scope.submit = function (valid) {
        if (!valid) {
            return;
        }
        RegisterClientResource.register($scope.data, function () {
            $log.info('Submitted');
            $state.go('existing', {apiKey: $scope.data.apiKey});
        }, function () {
            $log.error('An error occurred');
        });
    };
});

app.controller('ClientCtrl', function ($scope, $state, $stateParams, $log, ngTableParams, ClientResource) {
    'use strict';

    $scope.formData = {};

    $scope.loadData = function () {
        ClientResource.getTransactions({apiKey: $scope.formData.apiKey}, function (clients) {
            $log.info(clients);

            if (angular.equals({}, clients)) {
                $scope.clients = null;
            } else {
                $scope.clients = clients;
                setTableParams();
            }
        }, function (err) {
            $log.error('An error occurred', err);
        });
    };

    $scope.submit = function () {
        $state.go('existing', {apiKey: $scope.formData.apiKey}, {reload: true});
    };

    $scope.stop = function (randomId) {
        ClientResource.removeTransaction({randomId: randomId}, function () {
            $log.info('Successfully removed');
            $state.go('existing', {apiKey: $scope.formData.apiKey}, {reload: true});
        }, function (err) {
            $log.error('An error occurred', err);
        });
    };

    var init = function () {
        if ($stateParams.apiKey !== '') {
            $scope.formData = {
                apiKey: $stateParams.apiKey
            };
            $scope.loadData();
        }
    };
    init();

    var setTableParams = function () {
        $scope.tableParams = new ngTableParams({
            page: 1,
            count: $scope.clients.length + 10
        }, {
            total: $scope.clients.length,
            counts: [],
            getData: function ($defer, params) {
                params.total($scope.clients.length);
                $defer.resolve($scope.clients.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
        });
    };
});