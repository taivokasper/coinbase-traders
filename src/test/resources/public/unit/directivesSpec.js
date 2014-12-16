describe('Stats directive', function () {
    'use strict';

    var $scope, $httpBackend, $log, $compile;

    beforeEach(module('app', 'templates'));

    beforeEach(inject(function (_$compile_, $rootScope, _$httpBackend_, _$log_) {
        $scope = $rootScope;
        $httpBackend = _$httpBackend_;
        $log = _$log_;
        $compile = _$compile_;
    }));

    describe('GET is success', function () {
        beforeEach(function () {
            $httpBackend.expectGET('rest/stats').respond({
                sellPrice: 100,
                buyPrice: 50
            });
            $compile(angular.element('<stats></stats>'))($scope);
            $scope.$digest();
        });

        it('initialises stats object with -- values', function () {
            expect($scope.stats.sellPrice).toBe('--');
            expect($scope.stats.buyPrice).toBe('--');
        });

        it('gets buy and sell price values into stats object', function () {
            $httpBackend.flush();

            expect($scope.stats.sellPrice).toBe(100);
            expect($scope.stats.buyPrice).toBe(50);
        });
    });

    describe('GET is fail', function () {
        beforeEach(function () {
            $httpBackend.expectGET('rest/stats').respond(500);
            $compile(angular.element('<stats></stats>'))($scope);
            $scope.$digest();
        });

        it('logs error down when requesting stats fails', function () {
            spyOn($log, 'error');
            $httpBackend.flush();

            expect($log.error).toHaveBeenCalled();
        });
    });
});
