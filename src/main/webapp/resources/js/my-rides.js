angular.module('myRidesApp', ['ngCookies', 'editableTableWidgets', 'frontendServices'])
    .controller('MyRidesCtrl', ['$scope', '$cookieStore', 'DriveService',
        function ($scope, $cookieStore, DriveService) {

            $scope.vm = {
                successMessages: [],
                errorMessages: []
            };

            $scope.successMessage = $cookieStore.get("succesMessage");
            if(typeof $scope.successMessage !== 'undefined'){
                $cookieStore.remove("succesMessage");
                $scope.vm.successMessages = [];
                $scope.vm.successMessages.push({description: $scope.successMessage});
            }

            var loadRides = function(){
                DriveService.getMyRides().then(function(response){
                    console.log(response.drives);
                    $scope.vm.myRides = response.drives;
                });
            };

            loadRides();
        }
    ]);