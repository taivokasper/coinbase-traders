describe('$exceptionHandler factory', function() {
    'use strict';

    var $exceptionHandler, $log, $location, Bugsnag;

    beforeEach(module('app'));

    beforeEach(inject(function(_$exceptionHandler_, _$log_, $window, _$location_, logCollector) {
        $exceptionHandler = _$exceptionHandler_;
        $log = _$log_;
        $location = _$location_;
        Bugsnag = $window.Bugsnag;

        spyOn(Bugsnag, 'notifyException');
        spyOn($log, 'error').and.stub();
        spyOn(logCollector, 'getHistoryAsString').and.returnValue('previous log');
        spyOn($location, 'url').and.returnValue('url');
    }));

    it('configs Bugsnag as Development stage and notifies', function () {
        $exceptionHandler('hello', 'world');

        expect(Bugsnag.apiKey).toBe('0dfe2c5146836116d5a8d289c43c2885');
        expect(Bugsnag.releaseStage).toBe('Development');
        expect(Bugsnag.metaData).toEqual({logs: {log: 'previous log'}});
        expect(Bugsnag.context).toBe('url');

        expect(Bugsnag.notifyException).toHaveBeenCalled();
    });

    it('configs Bugsnag as Production stage', function () {
        spyOn($location, 'search').and.returnValue(false);
        $exceptionHandler('hello', 'world');

        expect(Bugsnag.releaseStage).toBe('Production');
    });
});
