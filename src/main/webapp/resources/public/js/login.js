angular.module('loginApp', ['common', 'spring-security-csrf-token-interceptor', 'editableTableWidgets'])
    .controller('LoginCtrl', ['$scope', '$http', function ($scope, $http) {

        $scope.onLogin = function () {

            $scope.vm.submitted = true;

            if ($scope.form.$invalid) {
                return;
            }

            $scope.login($scope.vm.userName, $scope.vm.password);

        };

    }]);