(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .controller('HoldingsEntityDetailController', HoldingsEntityDetailController);

    HoldingsEntityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'HoldingsEntity', 'BibliographicEntity', 'ItemEntity', 'InstitutionEntity'];

    function HoldingsEntityDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, HoldingsEntity, BibliographicEntity, ItemEntity, InstitutionEntity) {
        var vm = this;

        vm.holdingsEntity = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('scsbhipsterApp:holdingsEntityUpdate', function(event, result) {
            vm.holdingsEntity = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
