(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .factory('HoldingsEntitySearch', HoldingsEntitySearch);

    HoldingsEntitySearch.$inject = ['$resource'];

    function HoldingsEntitySearch($resource) {
        var resourceUrl =  'api/_search/holdings-entities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
