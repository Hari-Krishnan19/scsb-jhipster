(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .controller('ItemEntityDeleteController',ItemEntityDeleteController);

    ItemEntityDeleteController.$inject = ['$uibModalInstance', 'entity', 'ItemEntity'];

    function ItemEntityDeleteController($uibModalInstance, entity, ItemEntity) {
        var vm = this;

        vm.itemEntity = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ItemEntity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
