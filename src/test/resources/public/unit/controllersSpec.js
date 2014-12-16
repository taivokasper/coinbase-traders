describe('Root controller', function () {
    'use strict';

    var $scope, ctrl;

    beforeEach(module('app', 'templates'));

    beforeEach(inject(function ($rootScope, $controller) {
        $scope = $rootScope.$new();
        ctrl = $controller('RootCtrl', {$scope: $scope});
    }));

    describe('isActive method', function () {
        var $location;

        beforeEach(inject(function (_$location_) {
            $location = _$location_;
        }));

        it('returns true if given path is $location.path and false when not', function () {
            spyOn($location, 'path').and.returnValue('about/test');
            expect($scope.isActive('about')).toBeTruthy();
            expect($scope.isActive('clients')).toBeFalsy();
        });
    });
});

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

    it('sets client type', function () {
        $scope.setClientType('sell');
        expect($scope.client.type).toBe('sell');
    });

    describe('submitting form', function () {
        var $state, clientResources;

        beforeEach(inject(function (_$state_, RegisterClientResource) {
            $state = _$state_;
            clientResources = RegisterClientResource;

            spyOn(clientResources, 'register').and.callThrough();
            spyOn($state, 'go');
        }));

        it('should return immediately when on invalid form', function () {
            $scope.newClient = {'$invalid': true};
            $scope.submit();

            expect(clientResources.register.calls.count()).toBe(0);
        });

        it('redirects to clients page on success', function () {
            $httpBackend.expectPOST('rest/client/register').respond(200);
            $scope.newClient = {'$invalid': false};
            $scope.client.apiKey = 123;
            $scope.submit();

            $httpBackend.flush();

            expect($state.go).toHaveBeenCalledWith('existing', {apiKey: 123});
        });

        it('show error message on registration fail', function () {
            $httpBackend.expectPOST('rest/client/register').respond(500);
            $scope.newClient = {'$invalid': false};
            $scope.submit();

            $httpBackend.flush();

            expect($scope.errorMessage).toBe('Something went wrong while registering the client');
        });
    });
});

describe('Show clients controller', function () {
    'use strict';

    var $scope, $httpBackend, $state, $controller, ctrl, ClientResource;

    beforeEach(module('app', 'templates'));

    beforeEach(inject(function ($rootScope, _$controller_, _$httpBackend_, _$state_, _ClientResource_) {
        $scope = $rootScope.$new();
        $httpBackend = _$httpBackend_;
        $state = _$state_;
        $controller = _$controller_;
        ClientResource = _ClientResource_;
    }));

    it('initializes search as a empty object', function () {
        ctrl = $controller('ClientsCtrl', {$scope: $scope});
        expect($scope.search).toEqual({});
    });

    it('puts apiKey to search object and initialises tableParams', function () {
        ctrl = $controller('ClientsCtrl', {$scope: $scope, $stateParams: {apiKey: 123}});

        $httpBackend.expectGET('rest/client/123').respond([{id: 1}]);
        $httpBackend.flush();

        expect($scope.search.apiKey).toBe(123);
        expect($scope.tableParams).toBeDefined();
    });

    describe('searching clients', function () {

        beforeEach(function () {
            ctrl = $controller('ClientsCtrl', {$scope: $scope, $stateParams: {apiKey: 1}});
        });

        it('cleans clients list and returns immediatly on empty api key', function () {
            spyOn(ClientResource, 'getTransactions');

            $httpBackend.expectGET('rest/client/1').respond([{id: 1}]);
            $httpBackend.flush();

            $scope.search.apiKey = '';
            $scope.searchClients();

            expect($scope.clients).toEqual([]);
            expect(ClientResource.getTransactions).not.toHaveBeenCalled();
        });

        it('puts clients in to scope on success', function () {
            var clients = [{id: 1}, {id: 2}];

            $httpBackend.expectGET('rest/client/1').respond(clients);
            $httpBackend.flush();

            expect(angular.equals($scope.clients, clients)).toBeTruthy();
        });

        it('shows error message on search failure', function () {
            $httpBackend.expectGET('rest/client/1').respond(500);
            $httpBackend.flush();

            expect($scope.errorMessage).toBe('Something went wrong while searching clients');
        });
    });

    describe('stopping client', function () {

        beforeEach(function () {
            ctrl = $controller('ClientsCtrl', {$scope: $scope, $stateParams: {apiKey: 1}});

            $httpBackend.expectGET('rest/client/1').respond([{id: 1}, {id: 2}]);
            $httpBackend.flush();
        });

        it('removes client from clients list by searching clients again on success', function () {
            spyOn($scope, 'searchClients');

            $scope.stop(123);

            $httpBackend.expectDELETE('rest/client/123').respond(200);
            $httpBackend.flush();

            expect($scope.searchClients).toHaveBeenCalled();
        });

        it('shows error message on stop failure', function () {
            $scope.stop(123);

            $httpBackend.expectDELETE('rest/client/123').respond(500);
            $httpBackend.flush();

            expect($scope.errorMessage).toBe('Something went wrong while removing the client');
        });
    });
});