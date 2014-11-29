/* jshint newcap: false */
/* global app, angular, ngTableParams */

app.controller('RootCtrl', function ($scope) {});

app.controller('RegisterClientCtrl', function ($scope, $state, $log, RegisterClientResource) {
    'use strict';

    $scope.data = {};

    $scope.submit = function () {
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

    $scope.submit = function () {

        // TODO it should return array from BE
        ClientResource.get({apiKey: $scope.formData.apiKey}, function (clients) {
            $log.info(clients);

            if (angular.equals({}, clients)) {
                $scope.clients = null;
            } else {
                $scope.clients = [clients];
                setTableParams();
            }
        }, function (err) {
            $log.error('An error occurred', err);
        });
    };

    $scope.stop = function () {
        ClientResource.delete({apiKey: $scope.formData.apiKey}, function () {
            $log.info('Successfully removed');
            $state.go('front-page');
        }, function (err) {
            $log.error('An error occurred', err);
        });
    };

    var init = function () {
        if ($stateParams.apiKey !== '') {
            $scope.formData = {
                apiKey: $stateParams.apiKey
            };
            $scope.submit();
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