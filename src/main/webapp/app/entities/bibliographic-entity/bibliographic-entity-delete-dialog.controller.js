(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .controller('BibliographicEntityDeleteController',BibliographicEntityDeleteController);

    BibliographicEntityDeleteController.$inject = ['$uibModalInstance', 'entity', 'BibliographicEntity'];

    function BibliographicEntityDeleteController($uibModalInstance, entity, BibliographicEntity) {
        var vm = this;

        vm.bibliographicEntity = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BibliographicEntity.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
