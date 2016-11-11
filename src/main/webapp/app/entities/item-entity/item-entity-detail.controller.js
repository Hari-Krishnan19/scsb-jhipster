(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .controller('ItemEntityDetailController', ItemEntityDetailController);

    ItemEntityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ItemEntity', 'HoldingsEntity'];

    function ItemEntityDetailController($scope, $rootScope, $stateParams, previousState, entity, ItemEntity, HoldingsEntity) {
        var vm = this;

        vm.itemEntity = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('scsbhipsterApp:itemEntityUpdate', function(event, result) {
            vm.itemEntity = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
