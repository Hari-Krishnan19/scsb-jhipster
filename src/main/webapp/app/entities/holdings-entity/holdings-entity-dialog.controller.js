(function() {
    'use strict';

    angular
        .module('scsbhipsterApp')
        .controller('HoldingsEntityDialogController', HoldingsEntityDialogController);

    HoldingsEntityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'HoldingsEntity', 'BibliographicEntity', 'ItemEntity', 'InstitutionEntity'];

    function HoldingsEntityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, HoldingsEntity, BibliographicEntity, ItemEntity, InstitutionEntity) {
        var vm = this;

        vm.holdingsEntity = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.bibliographicentities = BibliographicEntity.query();
        vm.itementities = ItemEntity.query();
        vm.institutionentities = InstitutionEntity.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.holdingsEntity.id !== null) {
                HoldingsEntity.update(vm.holdingsEntity, onSaveSuccess, onSaveError);
            } else {
                HoldingsEntity.save(vm.holdingsEntity, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('scsbhipsterApp:holdingsEntityUpdate', result);
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
