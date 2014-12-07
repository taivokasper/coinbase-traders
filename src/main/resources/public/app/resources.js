/* global app */

app.factory('RegisterClientResource', function ($resource) {
    'use strict';

    return $resource('rest/client/register', {}, {
        register: {method: 'POST'}
    });
});

app.factory('RandomResource', function ($resource) {
    'use strict';

    return $resource('rest/client/random', {}, {
        getRandom: {method: 'GET'}
    });
});

app.factory('ClientResource', function ($resource) {
    'use strict';

    return $resource('rest/client/:apiKey:randomId', {
        apiKey: '@apiKey',
        randomId: '@randomId'
    }, {
        removeTransaction: {method: 'DELETE', params: {randomId: ''}},
        getTransactions: {method: 'GET', params: {apiKey: ''}, isArray: true}
    });
});

app.factory('StatsResources', function ($resource) {
    'use strict';

    return $resource('rest/stats/', {}, {
        getStats: {method: 'GET'}
    });
});