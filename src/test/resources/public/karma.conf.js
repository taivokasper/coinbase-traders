/* globals module */

module.exports = function (config) {
    'use strict';

    config.set({

        basePath: '../../../../',
        port: 9876,
        frameworks: ['jasmine'],

        files: [
            'src/main/resources/public/lib/angular/angular.js',
            'src/main/resources/public/lib/angular-*/*.js',
            'src/main/resources/public/lib/ng-*/*.js',
            'src/main/resources/public/lib/nsPopover/*.js',
            'src/main/resources/public/lib/ui-router/*.js',
            'src/main/resources/public/lib/log-it-down/*.js',
            'src/main/resources/public/lib/bugsnag/*.js',

            'src/main/resources/public/app/**/*.js',
            'src/test/resources/public/unit/**/*.js',
            'src/main/resources/public/**/*.html'
        ],
        exclude: [],
        plugins: [
            'karma-phantomjs-launcher',
            'karma-jasmine',
            'karma-ng-html2js-preprocessor',
            'karma-junit-reporter',
            'karma-coverage'
        ],
        reporters: ['progress', 'junit', 'coverage'],
        junitReporter: {
            outputFile: 'logs/karma-test-results.xml'
        },
        coverageReporter: {
            type: 'lcov',
            dir: 'logs/coverage/',
            subdir: '.'
        },
        preprocessors: {
            'src/main/resources/public/**/*.html': 'ng-html2js',
            'src/main/resources/public/app/**/*.js': 'coverage'
        },
        ngHtml2JsPreprocessor: {
            stripPrefix: 'src/main/resources/public/',
            moduleName: 'templates'
        },
        colors: true,
        logLevel: config.LOG_INFO,
        autoWatch: false,
        browsers: ['PhantomJS'],
        singleRun: true
    });
};
