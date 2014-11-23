/*jslint devel: true */
/* global app */

app.factory('RegisterClientResource', function ($resource) {
    return $resource('/rest/client/register', {}, {
        register: {method: 'POST'}
    });
});

app.factory('ClientResource', function ($resource) {
    return $resource('/rest/client/:apiKey', {
        apiKey: '@apiKey'
    });
});