(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .factory('BibliographicEntitySearch', BibliographicEntitySearch);

    BibliographicEntitySearch.$inject = ['$resource'];

    function BibliographicEntitySearch($resource) {
        var resourceUrl =  'api/_search/bibliographic-entities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
