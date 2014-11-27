/* global app, angular */

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

app.controller('ClientCtrl', function ($scope, $state, $stateParams, $log, ClientResource) {
    'use strict';

    $scope.formData = {};

    $scope.submit = function () {
        ClientResource.get({apiKey: $scope.formData.apiKey}, function (data) {
            $log.info(data);

            if (angular.equals({}, data)) {
                $scope.data = null;
            } else {
                $scope.data = data;
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
});