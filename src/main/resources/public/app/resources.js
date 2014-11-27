/* global app */

app.factory('RegisterClientResource', function ($resource) {
    'use strict';

    return $resource('rest/client/register', {}, {
        register: {method: 'POST'}
    });
});

app.factory('ClientResource', function ($resource) {
    'use strict';

    return $resource('rest/client/:apiKey', {
        apiKey: '@apiKey'
    });
});