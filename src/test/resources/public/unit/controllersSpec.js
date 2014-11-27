/* global describe, it, beforeEach, module, inject, expect */

describe('Register client controller', function () {
    'use strict';

    var $scope, $httpBackend, $state, ctrl;

    beforeEach(module('app'));

    beforeEach(inject(function ($rootScope, $controller, _$httpBackend_, _$state_) {
        $scope = $rootScope.$new();
        $httpBackend = _$httpBackend_;
        $state = _$state_;

        ctrl = $controller('RegisterClientCtrl', {$scope: $scope});
    }));

    it('initializes data as a object', function () {
        expect($scope.data).toEqual({});
    });
});