'use strict';

describe('Controller Tests', function() {

    describe('BibliographicEntity Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockBibliographicEntity, MockHoldingsEntity, MockInstitutionEntity;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockBibliographicEntity = jasmine.createSpy('MockBibliographicEntity');
            MockHoldingsEntity = jasmine.createSpy('MockHoldingsEntity');
            MockInstitutionEntity = jasmine.createSpy('MockInstitutionEntity');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'BibliographicEntity': MockBibliographicEntity,
                'HoldingsEntity': MockHoldingsEntity,
                'InstitutionEntity': MockInstitutionEntity
            };
            createController = function() {
                $injector.get('$controller')("BibliographicEntityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'scsbhipsterApp:bibliographicEntityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
