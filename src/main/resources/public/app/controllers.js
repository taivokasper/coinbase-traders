/* jshint newcap: false */
/* global app */

app.controller('RootCtrl', function ($scope, $location) {
    'use strict';

    $scope.isActive = function(path) {
        return $location.path().substr(0, path.length) === path;
    };
});

app.controller('RegisterClientCtrl', function ($scope, $state, $log, RegisterClientResource, RandomResource) {
    'use strict';

    $scope.client = {
        type: 'buy'
    };

    RandomResource.getRandom({}).$promise.then(function (random) {
        $scope.client.randomId = random.randomId;
    });

    $scope.setClientType = function (type) {
        $scope.client.type = type;
    };

    $scope.submit = function () {
        if ($scope.newClient.$invalid) {
            $log.warn('Form is invalid');
            return;
        }
        $scope.registeringClient = true;

        RegisterClientResource.register($scope.client).$promise.then(function () {
            $log.info('Submitted registering client');
            $state.go('existing', {apiKey: $scope.client.apiKey});
        }).catch(function (error) {
            $log.error('An error occurred while registering client', error);
            $scope.errorMessage = 'Something went wrong while registering the client';
        }).finally(function () {
            $scope.registeringClient = false;
        });
    };
});

app.controller('ClientsCtrl', function ($scope, $state, $stateParams, $log, ngTableParams, ClientResource) {
    'use strict';

    $scope.search = {};

    $scope.searchClients = function () {
        if (!$scope.search.apiKey) {
            $scope.clients = [];
            return;
        }
        $scope.searchingClient = true;

        ClientResource.getTransactions({apiKey: $scope.search.apiKey}).$promise.then(function (clients) {
            $log.info(clients);
            $scope.clients = clients;
            setTableParams();
        }).catch(function (err) {
            $scope.errorMessage = 'Something went wrong while searching clients';
            $log.error('An error occurred', err);
        }).finally(function () {
            $scope.searchingClient = false;
        });
    };

    $scope.stop = function (randomId) {
        ClientResource.removeTransaction({randomId: randomId}).$promise.then(function () {
            $log.info('Successfully removed');
            $scope.searchClients();
        }).catch(function (error) {
            $scope.errorMessage = 'Something went wrong while removing the client';
            $log.error('An error occurred', error);
        });
    };

    var init = function () {
        if ($stateParams.apiKey && $stateParams.apiKey !== '') {
            $scope.search.apiKey =  $stateParams.apiKey;
            $scope.searchClients();
        }
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