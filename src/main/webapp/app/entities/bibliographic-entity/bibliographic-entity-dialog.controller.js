(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .controller('BibliographicEntityDialogController', BibliographicEntityDialogController);

    BibliographicEntityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'BibliographicEntity', 'HoldingsEntity', 'InstitutionEntity'];

    function BibliographicEntityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, BibliographicEntity, HoldingsEntity, InstitutionEntity) {
        var vm = this;

        vm.bibliographicEntity = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.holdingsentities = HoldingsEntity.query();
        vm.institutionentities = InstitutionEntity.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.bibliographicEntity.id !== null) {
                BibliographicEntity.update(vm.bibliographicEntity, onSaveSuccess, onSaveError);
            } else {
                BibliographicEntity.save(vm.bibliographicEntity, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('scsbhipsterApp:bibliographicEntityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdDate = false;
        vm.datePickerOpenStatus.lastUpdatedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
