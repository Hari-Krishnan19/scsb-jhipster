(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .controller('InstitutionEntityDialogController', InstitutionEntityDialogController);

    InstitutionEntityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstitutionEntity'];

    function InstitutionEntityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, InstitutionEntity) {
        var vm = this;

        vm.institutionEntity = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.institutionEntity.id !== null) {
                InstitutionEntity.update(vm.institutionEntity, onSaveSuccess, onSaveError);
            } else {
                InstitutionEntity.save(vm.institutionEntity, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('scsbhipsterApp:institutionEntityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
