/* jslint browser: true, devel: true */
/* global app, angular */

app.controller('RootCtrl', function ($scope, $state) {

});

app.controller('FrontPageCtrl', function ($scope, $state) {

});

app.controller('RegisterClientCtrl', function ($scope, $state, RegisterClientResource) {
    $scope.data = {};

    $scope.submit = function () {
        RegisterClientResource.register($scope.data, function () {
            console.log('Submitted');
            $state.go('existing', {apiKey: $scope.data.apiKey})
        }, function () {
            console.error("An error occurred");
        });
    };
});

app.controller('ClientCtrl', function ($scope, $state, $stateParams, ClientResource) {
    $scope.formData = {};

    $scope.submit = function () {
        ClientResource.get({apiKey: $scope.formData.apiKey}, function (data) {
            console.log(data);
            if (angular.equals({}, data)) {
                $scope.data = null;
            } else {
                $scope.data = data;
            }
        }, function (err) {
            console.error('An error occurred', err)
        });
    };

    if ($stateParams.apiKey !== '') {
        $scope.formData = {
            apiKey: $stateParams.apiKey
        };
        $scope.submit();
    }

    $scope.stop = function () {
        ClientResource.delete({apiKey: $scope.formData.apiKey}, function (data) {
            console.log('Successfully removed');
            $state.go('front-page');
        }, function (err) {
            console.error('An error occurred', err)
        })
    };
});

app.controller('ErrorCtrl', function ($scope) {

});