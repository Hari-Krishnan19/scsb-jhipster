(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .controller('InstitutionEntityDeleteController',InstitutionEntityDeleteController);

    InstitutionEntityDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstitutionEntity'];

    function InstitutionEntityDeleteController($uibModalInstance, entity, InstitutionEntity) {
        var vm = this;

        vm.institutionEntity = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            InstitutionEntity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
