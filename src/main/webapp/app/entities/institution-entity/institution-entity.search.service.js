(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .factory('InstitutionEntitySearch', InstitutionEntitySearch);

    InstitutionEntitySearch.$inject = ['$resource'];

    function InstitutionEntitySearch($resource) {
        var resourceUrl =  'api/_search/institution-entities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
