(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .controller('BibliographicEntityDetailController', BibliographicEntityDetailController);

    BibliographicEntityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'BibliographicEntity', 'HoldingsEntity', 'InstitutionEntity'];

    function BibliographicEntityDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, BibliographicEntity, HoldingsEntity, InstitutionEntity) {
        var vm = this;

        vm.bibliographicEntity = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('scsbhipsterApp:bibliographicEntityUpdate', function(event, result) {
            vm.bibliographicEntity = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
