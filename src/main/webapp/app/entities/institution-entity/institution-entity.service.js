(function() {
    'use strict';
    angular
        .module('scsbhipsterApp')
        .factory('InstitutionEntity', InstitutionEntity);

    InstitutionEntity.$inject = ['$resource'];

    function InstitutionEntity ($resource) {
        var resourceUrl =  'api/institution-entities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
