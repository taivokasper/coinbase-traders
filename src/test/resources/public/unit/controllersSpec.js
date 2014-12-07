describe('Register client controller', function () {
    'use strict';

    var $scope, $httpBackend, $state, ctrl;

    beforeEach(module('app', 'templates'));

    beforeEach(inject(function ($rootScope, $controller, _$httpBackend_, _$state_) {
        $scope = $rootScope.$new();
        $httpBackend = _$httpBackend_;
        $state = _$state_;

        ctrl = $controller('RegisterClientCtrl', {$scope: $scope});

        $httpBackend.expectGET('rest/client/random').respond({randomId: 456});
    }));

    it('initializes client as a object with type Buy', function () {
        expect($scope.client).toEqual({type: 'buy'});
    });

    it('gets client a random it at initialization', function () {
        $httpBackend.flush();
        expect($scope.client.randomId).toEqual(456);
    });

    describe('submitting form', function () {
        var clientResources;

        beforeEach(inject(function (RegisterClientResource) {
            clientResources = RegisterClientResource;
            spyOn(clientResources, 'register');
        }));

        it('should return immediately when on invalid form', function () {
            $scope.newClient = {'$invalid': true};
            $scope.submit();

            expect(clientResources.register.calls.count()).toBe(0);
        });
    });
});

describe('Show clients controller', function () {
    'use strict';

    var $scope, $httpBackend, $state, ctrl;

    beforeEach(module('app', 'templates'));

    beforeEach(inject(function ($rootScope, $controller, _$httpBackend_, _$state_) {
        $scope = $rootScope.$new();
        $httpBackend = _$httpBackend_;
        $state = _$state_;

        ctrl = $controller('ClientsCtrl', {$scope: $scope});
    }));

    it('initializes search as a empty object', function () {
        expect($scope.search).toEqual({});
    });
});