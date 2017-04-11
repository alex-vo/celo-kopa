var sampleApp = angular.module('sampleApp', ['ServicesModule', 'ngRoute', 'ngCookies', 'common',
    'spring-security-csrf-token-interceptor', 'editableTableWidgets', 'ngAnimate', 'ui.bootstrap',
    'frontendServices', 'ngDialog', 'pascalprecht.translate']);

var lang;
var value = "; " + document.cookie;
var parts = value.split("; preferredLanguage=");
if (parts.length == 2){
    lang = parts.pop().split(";").shift().replace(new RegExp("%22", 'g'), "");;
}

var globalUserData;

sampleApp.config(['$routeProvider', '$locationProvider', '$translateProvider',
    function($routeProvider, $locationProvider, $translateProvider) {
        $routeProvider.
            when('/', {
                templateUrl: '/resources/public/search.html',
                controller: 'SearchCtrl'
            }).
            when('/login', {
                templateUrl: '/resources/public/login.html'
            }).
            when('/add-ride', {
                templateUrl: '/resources/partials/add-drive.html'
            }).
            when('/edit-ride/:rideId', {
                templateUrl: '/resources/partials/add-drive.html'
            }).
            when('/register', {
                templateUrl: '/resources/public/new-user.html'
            }).
            when('/search-results', {
                templateUrl: '/resources/public/search-results.html'
            }).
            when('/my-rides', {
                templateUrl: '/resources/partials/my-rides.html'
            }).
            when('/ride/:rideId', {
                templateUrl: '/resources/partials/view-ride.html'
            }).
            when('/edit-profile', {
                templateUrl: '/resources/partials/edit-profile.html'
            }).
            when('/activated', {
                templateUrl: '/resources/public/activated.html'
            }).
            when('/not-activated', {
                templateUrl: '/resources/public/not-activated.html'
            }).
            when('/profile', {
                templateUrl: '/resources/partials/profile.html'
            }).
            when('/restore-password', {
                templateUrl: '/resources/public/restore-password.html'
            }).
            when('/change-password', {
                templateUrl: '/resources/partials/change-password.html'
            }).
            otherwise({
                redirectTo: '/'
            });
        $locationProvider.html5Mode(true);

        $translateProvider.useStaticFilesLoader({
            prefix: '/resources/public/json/',
            suffix: '.json'
        });

        if(typeof lang !== "undefined") {
            $translateProvider.preferredLanguage(lang);
        }else{
            $translateProvider.determinePreferredLanguage();
        }
        if($translateProvider.preferredLanguage() != "ru_RU"){
            $translateProvider.preferredLanguage("lv_LV");
        }
    }]);

sampleApp.config(['ngDialogProvider', function (ngDialogProvider) {
    ngDialogProvider.setDefaults({
        className: 'ngdialog-theme-default',
        plain: false,
        showClose: true,
        closeByDocument: true,
        closeByEscape: true,
        appendTo: false
    });
}]);

sampleApp.controller('BasicCtrl', ['$scope', '$cookieStore', 'UserService', '$timeout', '$translate', '$http', '$uibModal', '$location', '$q',
    function($scope, $cookieStore, UserService, $timeout, $translate, $http, $uibModal, $location, $q){
        $scope.vm = {
            successMessages: [],
            errorMessages: []
        };

        $scope.retrieveUserData = function(){
            UserService.getUserData().then(function(success){
                globalUserData = success;
            }, function(error){
                globalUserData = {};
            });
        };

        $scope.isLoggedIn = function(){
            if(typeof globalUserData != "undefined" && !angular.equals(globalUserData, {})){
                return true;
            }

            return false;
        };

        $scope.goToAddRide = function(){
            $http.get("/api/user/checkFullProfile").then(function(data){
                if(data.status == 200) {
                    $location.path('/add-ride');
                }else{
                    $uibModal.open({
                        animation: true,
                        ariaLabelledBy: 'modal-title',
                        ariaDescribedBy: 'modal-body',
                        templateUrl: '/resources/partials/not-full-profile.html',
                        controller: 'ModalInstanceCtrl',
                        controllerAs: '$ctrl'
                    });
                }
            });
        };

        $scope.logout = function () {
            globalUserData = undefined;
            UserService.logout();
        };

        $scope.showMessages = function(){
            $scope.errorMessage = $cookieStore.get("errorMessage");
            if(typeof $scope.errorMessage !== 'undefined'){
                $(".glyphicon.spinning").hide();
                $cookieStore.remove("errorMessage");
                $cookieStore.remove("successMessage");
                $scope.vm.errorMessages = [];
                $scope.vm.errorMessages.push({description: $scope.errorMessage});
                window.scrollTo(0, 0);
                $(".alert.alert-danger").fadeIn();
                $timeout(function () { $(".alert.alert-danger").fadeOut(); }, 20000);
                return;
            }

            $scope.successMessage = $cookieStore.get("succesMessage");
            if(typeof $scope.successMessage !== 'undefined'){
                $cookieStore.remove("succesMessage");
                $scope.vm.successMessages = [];
                $scope.vm.successMessages.push({description: $scope.successMessage});
                $(".alert.alert-success").fadeIn();
                $timeout(function () { $(".alert.alert-success").fadeOut(); }, 20000);
            }
        };

        $scope.setLang = function(langKey) {
            $cookieStore.put("preferredLanguage", langKey);

            $translate.use(langKey);
            if($scope.isLoggedIn()) {
                $http({
                    method: 'POST',
                    url: '/api/user/setLanguage',
                    data: "lang=" + langKey,
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                        "X-Login-Ajax-call": 'true'
                    }
                })
                .then(function (response) {
                    if (response.status != 200) {
                        $cookieStore.put("errorMessage", "error.change.language");
                        $scope.showMessages();
                    }
                });
            }
        };

        $scope.getLang = function(){
            return $translate.use();
        };

        if(typeof globalUserData == "undefined"){
            $scope.retrieveUserData();
        }

        $scope.showMessages();
    }
]);

sampleApp.controller('SearchCtrl', ['$scope', 'SearchService', '$cookieStore', 'UserService', '$controller',
    function($scope, SearchService, $cookieStore, UserService, $controller) {
        $controller('BasicCtrl', {$scope: $scope});

        $scope.validFromValues = [];
        $scope.validToValues = [];

        $scope.onSearch = function(){
            var from = $("#from").val().toUpperCase();
            var to = $("#to").val().toUpperCase();
            if($scope.validFromValues.indexOf(from) > -1 && $scope.validToValues.indexOf(to) > -1){
                window.location.href = "search-results?from=" + from + "&to=" + to;
            }
        };

        //TODO: create separate function
        $scope.fromLocalities = new Bloodhound({
            datumTokenizer: function (datum) {
                return Bloodhound.tokenizers.whitespace(datum.value);
            },
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '/api/autocomplete?predicate=%QUERY',
                filter: function (result) {
                    $scope.validFromValues = result.localities.map(function(value){return value.toUpperCase();});
                    return $.map(result.localities, function (locality) {
                        return {
                            value: locality
                        };
                    });
                },
                wildcard: '%QUERY'
            }
        });

        $scope.toLocalities = new Bloodhound({
            datumTokenizer: function (datum) {
                return Bloodhound.tokenizers.whitespace(datum.value);
            },
            queryTokenizer: Bloodhound.tokenizers.whitespace,
            remote: {
                url: '/api/autocomplete?predicate=%QUERY',
                filter: function (result) {
                    $scope.validToValues = result.localities.map(function(value){return value.toUpperCase();});
                    return $.map(result.localities, function (locality) {
                        return {
                            value: locality
                        };
                    });
                },
                wildcard: '%QUERY'
            }
        });

        // Initialize the Bloodhound suggestion engine
        $scope.fromLocalities.initialize();
        $scope.toLocalities.initialize();

        $('#from').typeahead(null, {
            displayKey: 'value',
            source: $scope.fromLocalities.ttAdapter()
        });

        $('#to').typeahead(null, {
            displayKey: 'value',
            source: $scope.toLocalities.ttAdapter()
        });

    }
]);

sampleApp.controller('LoginCtrl', ['$scope', '$cookieStore', '$http', '$controller',
    function ($scope, $cookieStore, $http, $controller) {
        $controller('BasicCtrl', {$scope: $scope});

        $scope.preparePostData = function () {
            var username = $scope.vm.username != undefined ? $scope.vm.username : '';
            var password = $scope.vm.password != undefined ? $scope.vm.password : '';
            var email = $scope.vm.email != undefined ? $scope.vm.email : '';

            return 'username=' + username + '&password=' + password + '&email=' + email;
        };

        $scope.login = function () {
            var postData = $scope.preparePostData();

            $http({
                method: 'POST',
                url: '/api/authenticate',
                data: postData,
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "X-Login-Ajax-call": 'true'
                }
            })
            .then(function(response) {
                if (response.status == 200) {
                    $cookieStore.put("loggedIn", (new Date()).getTime());
                    $cookieStore.put("preferredLanguage", response.data);
                    window.location.href = "/";
                } else {
                    $cookieStore.put("errorMessage", "error.authentication");
                    $scope.showMessages();
                }
            });
        };

        $scope.onLogin = function () {
            $(".glyphicon.spinning").show().css("display", "inline-block");

            if ($scope.loginForm.$invalid) {
                return;
            }

            $scope.login($scope.vm.username, $scope.vm.password);
        };

        $scope.vm = {
            errorMessages: []
        };

    }
]);

sampleApp.controller('SearchResultsCtrl', ['$scope', '$cookieStore', '$http', 'DriveService', 'UserService', '$controller', '$location',
    function($scope, $cookieStore, $http, DriveService, UserService, $controller, $location) {
        $controller('BasicCtrl', {$scope: $scope});

        $scope.viewRide = function(rideId){
            window.location.href = '/ride/' + rideId;
        };

        $scope.destination = $location.search()['to'];
        $scope.source = $location.search()['from'];

        var getAge = function(dateString){
            var today = new Date();
            var birthDate = new Date(dateString);
            var age = today.getFullYear() - birthDate.getFullYear();
            var m = today.getMonth() - birthDate.getMonth();
            if (m < 0 || (m === 0 && today.getDate() < birthDate.getDate()))
            {
                age--;
            }
            return age;
        };

        var options = {
            weekday: "long",
            year: "numeric",
            month: "long",
            day: "numeric"
        };

        var getDateString = function(date){
            var today = new Date();
            var tomorrow = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);
            if (today.getFullYear() == date.getFullYear() && today.getMonth() == date.getMonth()
                && today.getDate() == date.getDate()) {
                return "Šodien";
            }
            if (tomorrow.getFullYear() == date.getFullYear() && tomorrow.getMonth() == date.getMonth()
                && tomorrow.getDate() == date.getDate()) {
                return "Rīt";
            }
            return date.toLocaleDateString("lv", options);
        };

        var setAges = function(drives){
            for(var i = 0; i < drives.length; i++){
                drives[i].user.age = getAge(drives[i].user.birthday);
                var date = new Date(drives[i].departureTime);
                var dept = {dateString: getDateString(date), exactTime: new Date(drives[i].departureTime)};
                drives[i].departure = dept;
            }
        };

        DriveService.findRides($scope.source, $scope.destination)
        .then(function(response){
            setAges(response.drives);
            $scope.vm.foundRides = response.drives;
        });

        $scope.showRide = function (rideId) {
            $location.path('/ride/' + rideId);
        };

    }]
);

sampleApp.controller('NewUserCtrl', ['$scope', '$http', '$cookieStore', '$controller',
    function ($scope, $http, $cookieStore, $controller) {
        $controller('BasicCtrl', {$scope: $scope});
        $scope.uploadFile = function(event){
            var files = event.target.files;
            $scope.vm.profilePicture = files[0];
        };

        $scope.createUser = function () {
            if ($scope.form.$invalid) {
                return;
            }

            $(".glyphicon.spinning").show().css("display", "inline-block");

            var postData = {
                username: $scope.vm.username,
                plainTextPassword: $scope.vm.password,
                email: $scope.vm.email,
                preferredLanguage: $scope.getLang()
            };
            var fd = new FormData();
            fd.append('data', new Blob([angular.toJson(postData)], {
                type: "application/json"
            }));
            fd.append("profilePicture", $scope.vm.profilePicture);

            $http.post("/api/user", fd, {
                transformRequest : angular.identity,
                transformResponse: angular.identity,
                headers : {
                    'Content-Type' : undefined
                }
            })
            .then(function(data) {
                if(data.status == 200) {
                    $cookieStore.put("succesMessage", "Uz Tavu norādīto e-pastu tika nosūtīts reģistrācijas apstiprinājums. Lai turpinātu, lūdzu, pārbaudi savu e-pastu un seko instrukcijām.");
                    window.location.href = "/";
                }else{
                    $cookieStore.put("errorMessage", data.data);
                    $scope.showMessages();
                }
            });
        };

        $("#file-3").fileinput({
            showUpload: false,
            showCaption: false,
            browseClass: "btn btn-primary btn-lg",
            fileType: "any",
            previewFileIcon: "<i class='glyphicon glyphicon-king'></i>"
        });
    }
])
.directive('customOnChange', function() {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var onChangeHandler = scope.$eval(attrs.customOnChange);
            element.bind('change', onChangeHandler);
        }
    };
});

sampleApp.controller('DriveCtrl', ['$scope', 'DriveService', '$cookieStore', 'UserService', 'SearchService', '$routeParams', '$controller', '$filter', '$http', '$window',
    function ($scope, DriveService, $cookieStore, UserService, SearchService, $routeParams, $controller, $filter, $http, $window) {
        $controller('BasicCtrl', {$scope: $scope});

        $scope.validFromValues = [];
        $scope.validToValues = [];

        $scope.ride = {};

        $scope.getLocation = function(val) {
            return $http.get('/api/autocomplete?predicate=' + val, {
                params: {
                    address: val,
                    sensor: false
                }
            }).then(function(response){
                return response.data.localities;
            });
        };

        var setDates = function() {
            $('#departureDateDiv').datetimepicker({
                //defaultDate: $scope.ride.departureDate,
                viewMode: 'days',
                format: 'DD/MM/YYYY'
            }).on("dp.change", function () {
                $scope.ride.departureDateString = $("#departureDate").val();
            });

            $('#arrivalDateDiv').datetimepicker({
                //defaultDate: $scope.ride.arrivalDate,
                viewMode: 'days',
                format: 'DD/MM/YYYY'
            }).on("dp.change", function () {
                $scope.ride.arrivalDateString = $("#arrivalDate").val();
            });

        };

        var prepareRideObject = function(){
            var result = jQuery.extend(true, {}, $scope.ride);
            var depDate = moment($scope.ride.departureDateString, "DD/MM/YYYY");
            if(typeof result.departureTime !== "undefined") {
                depDate._d.setHours(result.departureTime.getHours());
                depDate._d.setMinutes(result.departureTime.getMinutes());
            }
            result.departureTime = depDate.format("YYYY-MM-DD HH:mm");
            var arrDate = moment($scope.ride.arrivalDateString, "DD/MM/YYYY");
            if(typeof result.arrivalTime !== "undefined") {
                arrDate._d.setHours(result.arrivalTime.getHours());
                arrDate._d.setMinutes(result.arrivalTime.getMinutes());
            }
            result.arrivalTime = arrDate.format("YYYY-MM-DD HH:mm");

            return result;
        };

        var parseRideObject = function(rideObject){
            $scope.ride = rideObject;
            $scope.ride.departureDateString = $filter('date')(new Date(rideObject.departureTime), 'dd/MM/yyyy');
            $scope.ride.arrivalDateString = $filter('date')(new Date(rideObject.arrivalTime), 'dd/MM/yyyy');
            $scope.ride.departureTime = new Date(rideObject.departureTime);
            $scope.ride.arrivalTime = new Date(rideObject.arrivalTime);
        };

        if(typeof $routeParams.rideId !== "undefined"){
            var promise = DriveService.getRideById($routeParams.rideId);
            promise.then(function(rideData) {
                parseRideObject(rideData);
                setDates();
            }, function(errorMessage) {
                $cookieStore.put("errorMessage", errorMessage);
                $scope.showMessages();
            });
        }else{
            $scope.ride.departureDate = new Date();
            $scope.ride.arrivalDate = new Date();

            setDates();
        }

        $scope.ride.departureDate = new Date();
        $scope.ride.arrivalDate = new Date();

        $scope.addDrive = function () {
            $scope.$broadcast('show-errors-check-validity');
            if ($scope.form.$invalid) {
                return;
            }

            $(".glyphicon.spinning").show().css("display", "inline-block");

            var rideObject = prepareRideObject();
            var promise = DriveService.addRide(rideObject);
            promise.then(function (successMessage) {
                $cookieStore.put("succesMessage", successMessage);
                window.location.href = "/my-rides";
            }, function (errorMessage) {
                $cookieStore.put("errorMessage", errorMessage);
                $scope.showMessages();
            });
        };

        $scope.cancel = function(){
            $window.history.back();
        };
    }
]);

sampleApp.directive('tablesawrender', function($timeout) {
    return {
       link: function(scope, element, attr) {
          $timeout(function() {
             $(document).trigger("enhance.tablesaw");
          });
       }
    }
});

sampleApp.controller('MyRidesCtrl', ['$scope', '$cookieStore', 'DriveService', 'UserService', 'ngDialog', '$controller', '$uibModal',
    function ($scope, $cookieStore, DriveService, UserService, ngDialog, $controller, $uibModal) {
        $controller('BasicCtrl', {$scope: $scope});

        var loadRides = function(){
            DriveService.getMyRides().then(function(response){
                $scope.vm.myRides = response.drives;
            });
        };

        $scope.open = function (rideId, size, parentSelector) {
            var parentElem = parentSelector ?
                angular.element($document[0].querySelector('.modal-demo ' + parentSelector)) : undefined;
            var modalInstance = $uibModal.open({
                animation: true,
                ariaLabelledBy: 'modal-title',
                ariaDescribedBy: 'modal-body',
                templateUrl: '/resources/partials/dialog.html',
                controller: 'ModalInstanceCtrl',
                controllerAs: '$ctrl',
                size: size,
                appendTo: parentElem
            });

            modalInstance.result.then(function () {
                var promise = DriveService.deleteRide(rideId);
                promise.then(function(successMessage) {
                    $cookieStore.put("succesMessage", successMessage);
                    //TODO without refresh
                    location.reload();
                }, function(errorMessage) {
                    $scope.vm.errorMessages.push({description: errorMessage});
                });
            }, function () {});
        };

        loadRides();
    }
]);

sampleApp.controller('ViewRideCtrl', ['$scope', '$cookieStore', 'DriveService', 'UserService', '$routeParams', '$controller',
    function ($scope, $cookieStore, DriveService, UserService, $routeParams, $controller) {
        $controller('BasicCtrl', {$scope: $scope});

        var promise = DriveService.getRideById($routeParams.rideId);
        promise.then(function(rideData) {
            $scope.ride = rideData;
        }, function(errorMessage) {
            $scope.vm.errorMessages.push({description: errorMessage});
        });

        $scope.shareFacebook = function(){
            window.open('https://www.facebook.com/sharer/sharer.php?u='
                + document.head.querySelector("[property='og:url']").content,
                'sharer', 'toolbar=0,status=0,width=580,height=325');
        };

        $scope.shareDraugiem = function(){
            window.open('http://www.draugiem.lv/say/ext/add.php?url='
                + document.head.querySelector("[property='og:url']").content + '&title='
                + document.head.querySelector("[property='og:title']").content,
                'sharer', 'toolbar=0,status=0,width=580,height=325');
        };
    }
]);

sampleApp.directive('noCacheSrc', function($window) {
  return {
    priority: 99,
    link: function(scope, element, attrs) {
      attrs.$observe('noCacheSrc', function(noCacheSrc) {
        noCacheSrc += '?' + (new Date()).getTime();
        attrs.$set('src', noCacheSrc);
      });
    }
  }
});

sampleApp.controller('EditProfileCtrl', ['$scope', 'UserService', '$controller', '$location', '$http', '$cookieStore',
    function ($scope, UserService, $controller, $location, $http, $cookieStore) {
        $controller('BasicCtrl', {$scope: $scope});

        $scope.submitted = false;

        $scope.days = [];
        $scope.months = [{"value": 1, "text": "Janvāris"}, {"value": 2, "text": "Februāris"}, {"value": 3, "text": "Marts"},
            {"value": 4, "text": "Aprīlis"}, {"value": 5, "text": "Maijs"}, {"value": 6, "text": "Jūnijs"},
            {"value": 7, "text": "Jūlijs"}, {"value": 8, "text": "Augusts"}, {"value": 9, "text": "Septembris"},
            {"value": 10, "text": "Oktobris"}, {"value": 11, "text": "Novembris"}, {"value": 12, "text": "Decemris"}];
        $scope.years = [];

        var initDates = function() {
            for (var i = 1; i <= 31; i++) {
                $scope.days.push(i);
            }
            for (i = 1900; i <= (new Date()).getUTCFullYear(); i++) {
                $scope.years.push(i);
            }
        }

        initDates();

        //todo: don't call user endpoint
        UserService.getUserData().then(function(userData){
            console.log(userData);
            $scope.vm = userData;
            if($scope.vm.birthday){
                $scope.vm.day = parseInt($scope.vm.birthday.split(".")[0]);
                $scope.vm.month = parseInt($scope.vm.birthday.split(".")[1]);
                $scope.vm.year = parseInt($scope.vm.birthday.split(".")[2]);
            }else{
                var now = new Date();
                $scope.vm.day = now.getDate();
                $scope.vm.month = now.getMonth() + 1;
                $scope.vm.year = now.getUTCFullYear();
            }
        });

        $scope.cancel = function(){
            $location.path('/profile');
        };

        $scope.editProfile = function(){
            if ($scope.form.$invalid) {
                return;
            }

            $(".glyphicon.spinning").show().css("display", "inline-block");

            var postData = {
                username: $scope.vm.username,
                email: $scope.vm.email,
                name: $scope.vm.name,
                surname: $scope.vm.surname,
                birthday: ("0" + $scope.vm.day).slice(-2) + "." + ("0" + $scope.vm.month).slice(-2) + "." + $scope.vm.year,
                aboutMe: $scope.vm.aboutMe,
                car: $scope.vm.car,
                carRegNumber: $scope.vm.carRegNumber
            };

//            var fd = new FormData();
//            fd.append('data', new Blob([angular.toJson(postData)], {
//                type: "application/json"
//            }));
            $http.put("/api/user", JSON.stringify(postData), {
                transformRequest : angular.identity,
                transformResponse: angular.identity,
                headers : {
                    'Content-Type' : 'application/json;charset=UTF-8'
                }
            })
            .then(function(data) {
                if(data.status == 200) {
                    $cookieStore.put("succesMessage", "success.profile.updated");
                    window.location.href = "/profile";
                }else{
                    $cookieStore.put("errorMessage", data.data);
                    $scope.showMessages();
                }
            });
        };
    }
]);

sampleApp.directive('wjValidationError', function () {
    return {
        require: 'ngModel',
        link: function (scope, elm, attrs, ctl) {
            scope.$watch(attrs['wjValidationError'], function (errorMsg) {
                elm[0].setCustomValidity(errorMsg);
                ctl.$setValidity('wjValidationError', errorMsg ? false : true);
            });
        }
    };
});

sampleApp.directive('showErrors', function ($timeout, showErrorsConfig) {
        var getShowSuccess, linkFn;
        getShowSuccess = function (options) {
            var showSuccess;
            showSuccess = showErrorsConfig.showSuccess;
            if (options && options.showSuccess != null) {
                showSuccess = options.showSuccess;
            }
            return showSuccess;
        };
        linkFn = function (scope, el, attrs, formCtrl) {
            var blurred, inputEl, inputName, inputNgEl, options, showSuccess, toggleClasses;
            blurred = false;
            options = scope.$eval(attrs.showErrors);
            showSuccess = getShowSuccess(options);
            inputEl = el[0].querySelector('[name]');
            inputNgEl = angular.element(inputEl);
            inputName = inputNgEl.attr('name');
            if (!inputName) {
                throw 'show-errors element has no child input elements with a \'name\' attribute';
            }
            inputNgEl.bind('blur', function () {
                blurred = true;
                return toggleClasses(formCtrl[inputName].$invalid);
            });
            scope.$watch(function () {
                return formCtrl[inputName] && formCtrl[inputName].$invalid;
            }, function (invalid) {
                if (!blurred) {
                    return;
                }
                return toggleClasses(invalid);
            });
            scope.$on('show-errors-check-validity', function () {
                return toggleClasses(formCtrl[inputName].$invalid);
            });
            scope.$on('show-errors-reset', function () {
                return $timeout(function () {
                    el.removeClass('has-error');
                    el.removeClass('has-success');
                    return blurred = false;
                }, 0, false);
            });
            return toggleClasses = function (invalid) {
                el.toggleClass('has-error', invalid);
                if (showSuccess) {
                    return el.toggleClass('has-success', !invalid);
                }
            };
        };
        return {
            restrict: 'A',
            require: '^form',
            compile: function (elem, attrs) {
                if (!elem.hasClass('form-group')) {
                    throw 'show-errors element does not have the \'form-group\' class';
                }
                return linkFn;
            }
        };
    }
);

sampleApp.provider('showErrorsConfig', function () {
    var _showSuccess;
    _showSuccess = false;
    this.showSuccess = function (showSuccess) {
        return _showSuccess = showSuccess;
    };
    this.$get = function () {
        return { showSuccess: _showSuccess };
    };
});

sampleApp.controller('ModalInstanceCtrl', function ($uibModalInstance) {
    var $ctrl = this;

    $ctrl.ok = function () {
        $uibModalInstance.close();
    };

    $ctrl.cancel = function () {
        $uibModalInstance.dismiss('cancel');
    };

    $ctrl.gotoProfile = function(){
        window.location.href = "/edit-profile";
    };

});

sampleApp.directive('tablesawrender', function($timeout) {
    return {
        link: function(scope, element, attr) {
            $timeout(function() {
                $(document).ready(function() {
                    $('#example').DataTable( {
                        bFilter: false,
                        bInfo: false,
                        "bSort": false,
                        "bLengthChange": false,
                        "language": {
                            "processing": "Uzgaidiet...",
                            "search": "Meklēt:",
                            "lengthMenu": "Radīt _MENU_ ierakstus",
                            "info": "Parādīti _START_. līdz _END_. no _TOTAL_ ierakstiem",
                            "infoEmpty": "Nav ierakstu",
                            "infoFiltered": "(atlasīts no pavisam _MAX_ ierakstiem)",
                            "infoPostFix": "",
                            "loadingRecords": "Iekraušanas ieraksti ...",
                            "zeroRecords": "Nav atrasti vaicājumam atbilstoši ieraksti",
                            "emptyTable:": "Tabulā nav datu",
                            "paginate": {
                                "first": "Pirma",
                                "previous": "Iepriekšēja",
                                "next": "Nākoša",
                                "last": "Pēdēja"
                            },
                            "aria": {
                                "sortAscending": ": aktivizēt kolonnu, lai kārtotu augošā",
                                "sortDescending": ": aktivizēt kolonnu, lai kārtotu dilstošā"
                            }
                        },
                        rowReorder: {
                            selector: 'td:nth-child(2)'
                        },
                        responsive: true
                    });
                } );
            });
        }
    }
});

sampleApp.controller('MyProfileCtrl', function ($scope, $controller, UserService, DriveService, $cookieStore, $http) {
    $controller('BasicCtrl', {$scope: $scope});

    DriveService.getMyRides().then(function(response){
        $scope.myRides = response.drives;
    });

    UserService.getUserData().then(function(userData){
        $scope.user = userData;
        if(!$scope.user.profileImage){
            $scope.user.profileImage = "/resources/static/no-avatar.png";
        }
    });

    $scope.uploadFile = function(event){
        var files = event.target.files;
        $scope.profilePicture = files[0];
    };

    $(document).on('change', ':file', function() {
        var input = $(this),
            numFiles = input.get(0).files ? input.get(0).files.length : 1,
            label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
        input.trigger('fileselect', [numFiles, label]);
    });

    $(document).ready( function() {
        $(':file').on('fileselect', function(event, numFiles, label) {
            var fd = new FormData();
            fd.append("profilePicture", $scope.profilePicture);
            $http.post("/api/user/changePicture", fd, {
                transformRequest : angular.identity,
                transformResponse: angular.identity,
                headers : {
                    'Content-Type' : undefined
                }
            })
            .then(function(data) {
                if(data.status == 200) {
                    $cookieStore.put("succesMessage", "success.profile.picture.updated");
                    window.location.href = "/profile";
                }else{
                    $cookieStore.put("errorMessage", data.data);
                    $scope.showMessages();
                }
            });
        });
    });

    $scope.removePicture = function(){
        $http.post("/api/user/removePicture", {
            transformRequest : angular.identity,
            transformResponse: angular.identity,
            headers : {
                'Content-Type' : undefined
            }
        })
        .then(function(data) {
            if(data.status == 200) {
                $cookieStore.put("succesMessage", "success.profile.picture.deleted");
                window.location.href = "/profile";
            }else{
                $cookieStore.put("errorMessage", data.data);
                $scope.showMessages();
            }
        });
    };

});

sampleApp.controller('RestorePasswordCtrl', ['$scope', '$cookieStore', '$http', '$controller',
    function ($scope, $cookieStore, $http, $controller) {
        $controller('BasicCtrl', {$scope: $scope});

        $scope.restorePassword = function () {
            if ($scope.form.$invalid) {
                return;
            }

            $(".glyphicon.spinning").show().css("display", "inline-block");

            $http({
                method: 'POST',
                url: '/api/password/restore',
                data: "id=" + $scope.loginOrEmail,
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "X-Login-Ajax-call": 'true'
                }
            })
            .then(function(response) {
                if (response.status == 200) {
                    $cookieStore.put("succesMessage", "Uz tavu e-pastu tika atsūtīta jauna parole.");
                    window.location.href = "/";
                } else {
                    $cookieStore.put("errorMessage", "error.restoring-password");
                    $scope.showMessages();
                }
            });
        };

    }
]);

sampleApp.controller('ChangePasswordCtrl', ['$scope', '$cookieStore', '$http', '$controller', '$window',
    function ($scope, $cookieStore, $http, $controller, $window) {
        $controller('BasicCtrl', {$scope: $scope});

        $scope.changePassword = function () {
            if ($scope.form.$invalid) {
                return;
            }
            $http({
                method: 'POST',
                url: '/api/password/change',
                data: "oldPassword=" + $scope.oldPassword  + "&newPassword=" + $scope.newPassword,
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "X-Login-Ajax-call": 'true'
                }
            })
            .then(function(response) {
                if (response.status == 200) {
                    $cookieStore.put("succesMessage", "Parole tika nomainīta.");
                    window.location.href = "/profile";
                } else {
                    $cookieStore.put("errorMessage", "error.changing-password");
                    $scope.showMessages();
                }
            });
        };

        $scope.cancel = function(){
            $window.history.back();
        };

    }
]);
