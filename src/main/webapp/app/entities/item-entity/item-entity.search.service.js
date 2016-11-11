(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .factory('ItemEntitySearch', ItemEntitySearch);

    ItemEntitySearch.$inject = ['$resource'];

    function ItemEntitySearch($resource) {
        var resourceUrl =  'api/_search/item-entities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
