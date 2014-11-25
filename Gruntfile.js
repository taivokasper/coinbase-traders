/* global module */

var conf = {
    appDir: 'src/main/resources/public',
    testDir: 'src/test/resources/public'
};

module.exports = function (grunt) {
    'use strict';

    grunt.initConfig({

        conf: conf,

        clean: ['logs/'],

        bower: {
            install: {
                options: {
                    targetDir: '<%= conf.appDir %>/lib'
                }
            }
        },

        sass: {
            dist: {
                files: [{
                    expand: true,
                    cwd: '<%= conf.appDir %>/styles/',
                    src: ['sass/*.scss'],
                    dest: '<%= conf.appDir %>/styles/',
                    flatten: true,
                    ext: '.css',
                    update: true
                }]
            }
        },

        wiredep: {
           app: {
               src: ['<%= conf.appDir %>/index.html'],
               directory: '<%= conf.appDir %>/lib',
               exclude: '/angular-mocks/'
           }
        },

        includeSource: {
            options: {
                basePath: '<%= conf.appDir %>',
                baseUrl: '/'
            },
            myTarget: {
                files: {
                    '<%= conf.appDir %>/index.html': '<%= conf.appDir %>/index.html'
                }
            }
        },

        karma: {
            unit: {
                configFile: '<%= conf.testDir %>/karma.conf.js'
            }
        },

        protractor: {
            options: {
                configFile: '<%= conf.testDir %>/protractor.conf.js',
                keepAlive: true,
                noColor: false
            },
            all: {}
        },

        watch: {
            bower: {
                files: ['bower.json'],
                tasks: ['bower', 'wiredep']
            },
            stylesheets: {
                files: ['<%= conf.appDir %>/**/*.scss', '!<%= conf.appDir %>/lib/**/*.scss'],
                tasks: ['includeSource']
            },
            scripts: {
                files: ['<%= conf.appDir %>/**/*.js', '!<%= conf.appDir %>/lib/**/*.js'],
                tasks: ['includeSource']
            }
        }
    });
    grunt.option('force', true);

    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-wiredep');
    grunt.loadNpmTasks('grunt-bower-task');
    grunt.loadNpmTasks('grunt-include-source');
    grunt.loadNpmTasks('grunt-karma');
    grunt.loadNpmTasks('grunt-notify');
    grunt.loadNpmTasks('grunt-protractor-runner');
    grunt.loadNpmTasks('grunt-npm-install');
    grunt.loadNpmTasks('grunt-contrib-clean');
    grunt.loadNpmTasks('grunt-contrib-sass');

    grunt.registerTask('default', ['clean', 'npm-install', 'bower', 'wiredep', 'includeSource', 'karma', 'watch']);
    grunt.registerTask('build', ['clean', 'npm-install', 'bower', 'wiredep', 'sass', 'includeSource']);

    grunt.registerTask('test', ['clean', 'karma', 'protractor']);
    grunt.registerTask('test:unit', ['clean', 'karma']);
    grunt.registerTask('test:e2e', ['clean', 'protractor']);
};