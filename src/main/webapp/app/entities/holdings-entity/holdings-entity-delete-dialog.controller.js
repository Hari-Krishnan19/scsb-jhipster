(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .controller('HoldingsEntityDeleteController',HoldingsEntityDeleteController);

    HoldingsEntityDeleteController.$inject = ['$uibModalInstance', 'entity', 'HoldingsEntity'];

    function HoldingsEntityDeleteController($uibModalInstance, entity, HoldingsEntity) {
        var vm = this;

        vm.holdingsEntity = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            HoldingsEntity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
