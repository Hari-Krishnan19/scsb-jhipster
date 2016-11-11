(function() {
    'use strict';
    angular
        .module('scsbhipsterApp')
        .factory('BibliographicEntity', BibliographicEntity);

    BibliographicEntity.$inject = ['$resource', 'DateUtils'];

    function BibliographicEntity ($resource, DateUtils) {
        var resourceUrl =  'api/bibliographic-entities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdDate = DateUtils.convertLocalDateFromServer(data.createdDate);
                        data.lastUpdatedDate = DateUtils.convertLocalDateFromServer(data.lastUpdatedDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.createdDate = DateUtils.convertLocalDateToServer(copy.createdDate);
                    copy.lastUpdatedDate = DateUtils.convertLocalDateToServer(copy.lastUpdatedDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.createdDate = DateUtils.convertLocalDateToServer(copy.createdDate);
                    copy.lastUpdatedDate = DateUtils.convertLocalDateToServer(copy.lastUpdatedDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
