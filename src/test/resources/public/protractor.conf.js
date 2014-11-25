/* global require, exports, jasmine */

var ScreenShotReporter = require('protractor-screenshot-reporter');

exports.config = {
    baseURL: 'http://localhost:8080',
    capabilities: {
        'browserName': 'chrome',
        'chromeOptions': {
            args: ['--test-type']
        }
    },
    directConnect: true,
    specs: ['e2e/**/*.js'],

    jasmineNodeOpts: {
        showColors: true,
        defaultTimeoutInterval: 30000
    },
    framework: 'jasmine',
    onPrepare: function() {
        'use strict';

        require('jasmine-reporters');

        jasmine.getEnv().addReporter(
            new ScreenShotReporter({
                baseDirectory: 'logs/screenshots',
                takeScreenShotsOnlyForFailedSpecs: true
            })
        );
        jasmine.getEnv().addReporter(
            new jasmine.JUnitXmlReporter('logs', true, true, 'e2e-test-results', true)
        );
    }
};
