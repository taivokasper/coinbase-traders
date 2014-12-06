/* jshint newcap: false */
/* globals app */

app.controller('RootCtrl', function ($scope) {});

app.controller('RegisterClientCtrl', function ($scope, $state, $log, RegisterClientResource, RandomResource) {
    'use strict';

    $scope.client = {
        type: 'buy'
    };

    RandomResource.getRandom({}).$promise.then(function (random) {
        $scope.client.randomId = random.randomId;
    });

    $scope.submit = function () {
        if ($scope.newClient.$invalid) {
            return;
        }
        RegisterClientResource.register($scope.client).$promise.then(function () {
            $log.info('Submitted registering client');
            $state.go('existing', {apiKey: $scope.client.apiKey});
        }).catch(function () {
            $log.error('An error occurred while registering client');
        });
    };
});

app.controller('ClientCtrl', function ($scope, $state, $stateParams, $log, ngTableParams, ClientResource) {
    'use strict';

    $scope.search = {};

    $scope.submit = function () {
        $state.go('existing', {apiKey: $scope.search.apiKey}, {reload: true});
    };

    $scope.stop = function (randomId) {
        ClientResource.removeTransaction({randomId: randomId}).$promise.then(function () {
            $log.info('Successfully removed');
            $state.go('existing', {apiKey: $scope.search.apiKey}, {reload: true});
        }).catch(function (error) {
            $log.error('An error occurred', error);
        });
    };

    var init = function () {
        if ($stateParams.apiKey !== '') {
            $scope.search.apiKey =  $stateParams.apiKey;
            loadClient();
        }
    };

    var loadClient = function () {
        ClientResource.getTransactions({apiKey: $scope.search.apiKey}).$promise.then(function (clients) {
            $log.info(clients);

            if (angular.equals({}, clients)) {
                $scope.clients = null;
            } else {
                $scope.clients = clients;
                setTableParams();
            }
        }).catch(function (err) {
            $log.error('An error occurred', err);
        });
    };

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
    init();
});